from rest_framework import serializers
from .models import Content, ContentVideo


class ContentVideoSerializer(serializers.ModelSerializer):
    class Meta:
        model = ContentVideo
        fields = ['title', 'video_link', 'runtime', 'content']


class ContentSerializer(serializers.ModelSerializer):
    # Assuming videos are read-only and nested
    videos = ContentVideoSerializer(many=True, read_only=True)

    class Meta:
        model = Content
        fields = ['title', 'description', 'audio_link', 'videos']
