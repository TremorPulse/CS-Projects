from rest_framework import serializers
from . import Forum, Post, Comment, Message, Notification, Video, Blog, Employee

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = Employee
        fields = ['id', 'username', 'email', 'company', 'department']

class ForumSerializer(serializers.ModelSerializer):
    class Meta:
        model = Forum
        fields = ['id', 'title', 'description']

class PostSerializer(serializers.ModelSerializer):
    author = UserSerializer(read_only=True)
    class Meta:
        model = Post
        fields = ['id', 'title', 'content', 'created_at', 'author', 'forum', 'likes']

class CommentSerializer(serializers.ModelSerializer):
    author = UserSerializer(read_only=True)
    class Meta:
        model = Comment
        fields = ['id', 'content', 'created_at', 'author', 'post']

class MessageSerializer(serializers.ModelSerializer):
    sender = UserSerializer(read_only=True)
    receiver = UserSerializer(read_only=True)
    class Meta:
        model = Message
        fields = ['id', 'content', 'created_at', 'sender', 'receiver']

class NotificationSerializer(serializers.ModelSerializer):
    user = UserSerializer(read_only=True)
    class Meta:
        model = Notification
        fields = ['id', 'content', 'created_at', 'user']

class VideoSerializer(serializers.ModelSerializer):
    uploader = UserSerializer(read_only=True)
    class Meta:
        model = Video
        fields = ['id', 'title', 'url', 'uploaded_at', 'uploader']

class BlogSerializer(serializers.ModelSerializer):
    author = UserSerializer(read_only=True)
    class Meta:
        model = Blog
        fields = ['id', 'title', 'content', 'created_at', 'author']
