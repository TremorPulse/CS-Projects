from django.shortcuts import render
from rest_framework import generics
from .models import Content
from .serializers import ContentSerializer, Content
from rest_framework.response import Response
from rest_framework.views import APIView
# Create your views here.


class ContentView(APIView):
    def get(self, request):
        contents = Content.objects.all()
        serializer = ContentSerializer(contents, many=True)
        return Response(serializer.data)
