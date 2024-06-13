from django.shortcuts import render, redirect
import requests
from .serializers import DailyStepSerializer
from django.utils.timezone import now
from rest_framework import views
from rest_framework.views import APIView
from rest_framework import status, generics, viewsets
from rest_framework.response import Response
from rest_framework.renderers import BrowsableAPIRenderer
from rest_framework.parsers import FormParser, MultiPartParser
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse
from django.views import View
from requests_oauthlib import OAuth2Session
import requests_oauthlib
from django.conf import settings
import logging
import json
import redis
from datetime import datetime, timedelta
from django.core.cache import cache
from rest_framework import viewsets
from activity.models import Goal, Achievement, Employee
from activity.serializers import GoalSerializer, AchievementSerializer
# from google.oauth2.credentials import Credentials
# from google_auth_oauthlib.flow import Flow
# from googleapiclient.discovery import build
import os


os.environ['OAUTHLIB_INSECURE_TRANSPORT'] = '1'

tokens = []


def homeview(request):
    return HttpResponse("Hello, World!")


def get_start_end_times():
    now = datetime.now()
    start_of_today = datetime(now.year, now.month, now.day)
    end_of_today = start_of_today + timedelta(days=1)
    start_time_millis = int(start_of_today.timestamp() * 1000)
    end_time_millis = int(end_of_today.timestamp() * 1000)

    return start_time_millis, end_time_millis


start_time, end_time = get_start_end_times()


SCOPES = ['https://www.googleapis.com/auth/fitness.activity.read']


def google_fit_login(request):
    oauth = OAuth2Session(
        settings.GOOGLE_FIT_CLIENT_ID,
        redirect_uri='http://127.0.0.1:8000/google/callback/',
        scope=SCOPES
    )
    authorization_url, state = oauth.authorization_url(
        'https://accounts.google.com/o/oauth2/auth',
        access_type='offline'
    )
    request.session['oauth_state'] = state
    return redirect(authorization_url)


def google_fit_callback(request):
    oauth = OAuth2Session(
        settings.GOOGLE_FIT_CLIENT_ID,
        state=request.session.get('oauth_state'),
        redirect_uri='http://127.0.0.1:8000/google/callback/'
    )
    try:
        token = oauth.fetch_token(
            'https://oauth2.googleapis.com/token',
            authorization_response=request.build_absolute_uri(),
            client_secret=settings.GOOGLE_FIT_CLIENT_SECRET
        )
        request.session['google_fit_token'] = token
        request.session.modified = True
        request.session.save()
        logging.debug(f"Token successfully set in session: {token}")
        cache.set(f"google_fit_token_{request.user.id}", token, timeout=3600)
        return HttpResponseRedirect('http://127.0.0.1:3000/stepData')
    except Exception as e:
        error_detail = str(e)
        if hasattr(e, 'response') and e.response is not None:
            error_detail += ' Response: ' + e.response.text

        logging.error(f"Token exchange failed: {error_detail}")
        return JsonResponse({'error': 'Token exchange failed', 'details': error_detail}, status=400)


def check_signin_status(request):
    is_signed_in = 'google_fit_token' in request.session
    if is_signed_in:
        token_info = request.session['google_fit_token']
        return JsonResponse({'isSignedIn': True, 'token_info': token_info})
    else:
        return JsonResponse({'isSignedIn': False, 'session_keys': list(request.session.keys())})


MAX_REQUESTS_PER_MINUTE = 10


def is_rate_limited(user_id):
    rate_limit_key = f"rate_limit_{user_id}"
    with redis.Redis() as client:
        requests_count = client.incr(rate_limit_key)
        if requests_count == 1:
            client.expire(rate_limit_key, 60)
        if requests_count > MAX_REQUESTS_PER_MINUTE:
            return True
    return False


def get_steps(request):
    token = request.session.get('google_fit_token', {})
    if 'access_token' in token:
        cache_key = f"steps_{request.user.id}"
        headers = {
            'Authorization': f"Bearer {token['access_token']}", 'Content-Type': 'application/json'}
        data = {
            "aggregateBy": [{
                "dataTypeName": "com.google.step_count.delta",
                "dataSourceId": "derived:com.google.step_count.delta:com.google.android.gms:estimated_steps"
            }],
            "bucketByTime": {"durationMillis": 86400000},
            "startTimeMillis": start_time,
            "endTimeMillis": end_time
        }
        try:
            response = requests.post(
                'https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate',
                headers=headers,
                data=json.dumps(data)
            )
            response_data = response.json()
            steps_data = response_data['bucket'][0]['dataset'][0]['point'][0]['value'][0]['intVal']
            cache.set(cache_key, steps_data, timeout=300)
            return JsonResponse({'steps': steps_data})
        except Exception as e:
            logging.error(f"Failed to fetch steps data: {str(e)}")
            return JsonResponse({'error': 'Failed to fetch data', 'details': str(e)}, status=400)
    else:
        return JsonResponse({'error': 'Not authenticated'}, status=401)


class StepDataView(views.APIView):
    def post(self, request):
        serializer = DailyStepSerializer(data=request.data)
        if serializer.is_valid():
            # You might want to add checks here to prevent duplicate entries for the same day
            serializer.save(user=request.user, date=now().date())
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class GoalListView(generics.ListAPIView):
    queryset = Goal.objects.all()
    serializer_class = GoalSerializer


class CreateAchievementView(views.APIView):
    def post(self, request):
        serializer = AchievementSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save(employee=request.user.employee)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
