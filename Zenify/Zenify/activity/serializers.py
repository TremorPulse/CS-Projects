from rest_framework import serializers
from activity.models import Compliment, Badge, Goal, SleepData, DailyStep, Achievement
from users.models import Employee


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = Employee
        fields = ['id', 'username', 'email', 'company', 'department']


class ComplimentSerializer(serializers.ModelSerializer):
    sender = UserSerializer(read_only=True)
    receiver = UserSerializer(read_only=True)

    class Meta:
        model = Compliment
        fields = ['id', 'message', 'sender', 'receiver', 'created_at']


class BadgeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Badge
        fields = ['id', 'name', 'description', 'badge_type', 'image', 'points']


class GoalSerializer(serializers.ModelSerializer):
    employee = UserSerializer(read_only=True)

    class Meta:
        model = Goal
        fields = ['id', 'description', 'points', 'completed', 'employee']


class SleepDataSerializer(serializers.ModelSerializer):
    employee = UserSerializer(read_only=True)

    class Meta:
        model = SleepData
        fields = ['id', 'date', 'hours_slept', 'sleep_quality', 'employee']


class DailyStepSerializer(serializers.ModelSerializer):
    class Meta:
        model = DailyStep
        fields = ('id', 'steps', 'date')
        read_only_fields = ('date',)


class AchievementSerializer(serializers.ModelSerializer):
    class Meta:
        employee = UserSerializer(read_only=True)
        goal = GoalSerializer(read_only=True)
        model = Achievement
        fields = ['employee', 'goal']
