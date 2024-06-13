import base64
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework import status
from users.serializers import UserSerializer
from users.models import Company, Department, User
from rest_framework.response import Response
from rest_framework.exceptions import AuthenticationFailed
import jwt, datetime
from django.core.mail import send_mail
from django.contrib.auth.tokens import default_token_generator
from django.utils.encoding import force_bytes
from django.contrib.sites.shortcuts import get_current_site
from rest_framework.exceptions import APIException
from rest_framework.views import exception_handler
from django.contrib.auth.tokens import default_token_generator
from django.utils.http import urlsafe_base64_encode, urlsafe_base64_decode
from django.utils.encoding import force_str
from django.template.loader import render_to_string
from django_otp.oath import totp
import logging
import pyotp
from django_otp.util import random_hex
import secrets
import time

def custom_exception_handler(exc, context):
    response = exception_handler(exc, context)
    if response is not None:
        if 'detail' in response.data:
            response.data['error'] = response.data['detail']
            del response.data['detail']
    return response

class HomeView(APIView):
    permission_classes = (IsAuthenticated, )

    def get(self, request):
        content = {'message': 
                'Authentication test page'}
        
        return Response(content)
    
class DashboardView(APIView):
    def get(self, request, *args, **kwargs):
        resources = [
            {'name': 'Personality Quiz', 'url': 'personality-quiz/'},
            {'name': 'Weekly Quiz', 'url': 'weekly-quiz/'},
            {'name': 'Social Hub', 'url': 'social-hub'}
        ]

        return Response({'resources': resources})

def generate_otp():
    base32_key = generate_base32_key()
    totp = pyotp.TOTP(base32_key, interval=120)
    otp = totp.now()
    otp_time = time.time()  # save the time the OTP was generated
    logger.info(f"Generated OTP: {otp} at time: {otp_time}")
    return otp, otp_time

def is_otp_expired(otp_time):
    current_time = time.time()
    logger.info(f"Current time: {current_time}, OTP time: {otp_time}")
    return time.time() - otp_time > 60

logger = logging.getLogger(__name__)

def generate_base32_key():
    random_bytes = secrets.token_bytes(20)
    base32_encoded_key = base64.b32encode(random_bytes).decode('utf-8')
    return base32_encoded_key

def totp(base32_key):
    logger.info(f"Generating OTP using key: {base32_key}")
    totp_generator = pyotp.TOTP(base32_key)  # Directly use the base32 key
    otp = totp_generator.now()
    return otp

class RegisterView(APIView):
    def post(self, request):
        logger.info("Received registration request with data: {}".format(request.data))

        serializer = UserSerializer(data=request.data)
        if not serializer.is_valid():
            logger.error(f"Serializer validation errors: {serializer.errors}")
            return Response({"errors": serializer.errors}, status=400)

        if User.objects.filter(email=serializer.validated_data['email']).exists():
            logger.error("Attempt to register with an existing email: {}".format(serializer.validated_data['email']))
            return Response({"error": "Email already exists"}, status=400)
        
        company_name = serializer.validated_data.pop('company')
        department_name = serializer.validated_data.pop('department')
        company, _ = Company.objects.get_or_create(name=company_name)
        department, _ = Department.objects.get_or_create(name=department_name)
        serializer.validated_data['company'] = company
        serializer.validated_data['department'] = department

        user = User.objects.create_user(is_active=False, **serializer.validated_data)

        base32_key = generate_base32_key()
        otp = totp(base32_key) # GENERATED OTP CODE

        otp_time = time.time()

        request.session[str(user.id)] = {'otp': otp, 'key': base32_key, 'otp_time': otp_time} 
        uid = urlsafe_base64_encode(force_bytes(user.pk))

        mail_subject = 'Activate your account.'
        message = render_to_string('email_msg.html', {
            'user': user,
            'domain': get_current_site(request).domain,
            'uid': uid,
            'otp': otp,
        })
        send_mail(mail_subject, message, 'org.zenify@gmail.com', [user.email])

        logger.info("Verification email sent to {}".format(user.email))
        return Response({
            "msg": "Verification email sent",
            "uid": uid,
            "otp": otp # GENERATED OTP CODE
            }, status=201)


class VerifyEmailView(APIView):
    def post(self, request):
        uidb64 = request.data.get('uid')
        otp = request.data.get('otp') # THIS IS THE OTP CODE THAT THE USER INPUTS

        print("OTP", otp)

        if not uidb64 or not otp:
            logger.error(f"Missing data: UID - {uidb64}, OTP - {otp}")
            return Response({"error": "Missing UID or OTP"}, status=status.HTTP_400_BAD_REQUEST)

        try:
            uid = force_str(urlsafe_base64_decode(uidb64))
            session_data = request.session.get(uid)

            if not session_data:
                logger.error(f"No session data for UID: {uid}")
                return Response({"error": "Session data not found"}, status=status.HTTP_400_BAD_REQUEST)

            print("SESSION DATA", session_data)
            key = session_data['key']

            otp_time = session_data['otp_time']  # get the OTP time from the session
            
            valid_otp = session_data["otp"]

            print(f"Validating OTP: Received - {otp}, Expected - {valid_otp}")

            if otp == valid_otp:
                if time.time() - otp_time > 120:  # check if more than 120 seconds have passed
                    print('expired')
                    return Response({"error": "OTP expired"}, status=status.HTTP_400_BAD_REQUEST)
                user = User.objects.get(pk=uid)
                user.is_active = True
                user.save()
                return Response({"message": "Email verified successfully"}, status=status.HTTP_200_OK)
            else:
                logger.error(f"OTP mismatch: Received - {otp}, Expected - {valid_otp}")
                return Response({"error": "Invalid OTP"}, status=status.HTTP_400_BAD_REQUEST)

        except (TypeError, ValueError, OverflowError, User.DoesNotExist) as e:
            logger.exception(f"Error during verification: {e}")
            return Response({"error": "Invalid request"}, status=status.HTTP_400_BAD_REQUEST)


class LoginView(APIView):
    def post(self, request):
        email = request.data['email']
        password = request.data['password']

        user = User.objects.filter(email=email).first()

        if user is None:
            raise AuthenticationFailed('User not found!')

        if not user.check_password(password):
            raise AuthenticationFailed('Incorrect password!')

        payload = {
            'id': user.id,
            'exp': datetime.datetime.now() + datetime.timedelta(minutes=60),
            'iat': datetime.datetime.now()
        }

        token = jwt.encode(payload, 'secret', algorithm='HS256')

        response = Response()

        response.set_cookie(key='jwt', value=token, httponly=True)
        response.data = {
            'jwt': token
        }
        return response
    
class LogoutView(APIView):
    def post(self, request):
        response = Response()
        response.delete_cookie('jwt')
        response.data = {
            'message': 'success'
        }
        return response
    
class UserView(APIView):

    def get(self, request):
        token = request.COOKIES.get('jwt')

        if not token:
            raise AuthenticationFailed('Unauthenticated!')

        try:
            payload = jwt.decode(token, 'secret', algorithm=['HS256'])
        except jwt.ExpiredSignatureError:
            raise AuthenticationFailed('Unauthenticated!')

        user = User.objects.filter(id=payload['id']).first()
        serializer = UserSerializer(user)
        return Response(serializer.data)