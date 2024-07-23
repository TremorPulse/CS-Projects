# tasks.py in your Django app

from celery import shared_task
import requests
import json
from datetime import datetime, timedelta
from django.core.cache import cache
from django.http import JsonResponse
# Assuming you abstract the logic into a callable function
from .views import get_steps_logic


@shared_task
def fetch_google_fit_data():
    print("Fetching Google Fit data...")
    data = get_steps_logic()  # This function needs to handle the fetching and processing
    # Cache the latest data for an hour
    cache.set('latest_steps_data', data, timeout=3600)
    return data


def get_start_end_times():
    now = datetime.now()
    start_of_today = datetime(now.year, now.month, now.day)
    end_of_today = start_of_today + timedelta(days=1)
    # Convert to timestamps in milliseconds
    start_time_millis = int(start_of_today.timestamp() * 1000)
    end_time_millis = int(end_of_today.timestamp() * 1000)

    return start_time_millis, end_time_millis


start_time, end_time = get_start_end_times()


def get_steps_logic():
    user_id = 1  # This should be dynamically determined based on your app's context
    token = cache.get(f"google_fit_token_{user_id}")
    if not token:
        return {'error': 'Authentication token not found'}
    if 'access_token' in token:
        headers = {
            'Authorization': f"Bearer {token['access_token']}", 'Content-Type': 'application/json'}
        data = {
            "aggregateBy": [{
                "dataTypeName": "com.google.step_count.delta",
                "dataSourceId": "derived:com.google.step_count.delta:com.google.android.gms:estimated_steps"
            }],
            # Bucket data by days
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
            # Parsing the step data from the response
            steps_data = response_data['bucket'][0]['dataset'][0]['point'][0]['value'][0]['intVal']
            return JsonResponse({'steps': steps_data})
        except Exception as e:
            return JsonResponse({'error': 'Failed to fetch data', 'details': str(e)}, status=400)
    else:
        return JsonResponse({'error': 'Not authenticated'}, status=401)
    # This is a pseudo-function that needs to encapsulate your get_steps logic
    # Ensure this function does not take `request` as a parameter but manages the token and API calls internally
    return {"steps": 100}  # Example return value
