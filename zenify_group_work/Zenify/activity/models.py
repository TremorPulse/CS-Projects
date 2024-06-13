import json
import random
from django.db import models
from users.models import Employee, Employer
from django.contrib.contenttypes.fields import GenericRelation


class Compliment(models.Model):
    message = models.TextField()
    sender = models.ForeignKey(
        Employee, related_name='sent_compliments', on_delete=models.CASCADE)
    receiver = models.ForeignKey(
        Employee, related_name='received_compliments', on_delete=models.CASCADE)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.message


class Badge(models.Model):
    BADGE_TYPES = [
        ('BRONZE', 'Bronze'),
        ('SILVER', 'Silver'),
        ('GOLD', 'Gold'),
    ]
    name = models.CharField(max_length=200)
    description = models.TextField()
    badge_type = models.CharField(
        max_length=6, choices=BADGE_TYPES, default='BRONZE')
    image = models.ImageField(upload_to='badges/')
    points = models.IntegerField()
    employees = models.ManyToManyField(Employee, related_name='badges')

    def __str__(self):
        return self.name


class UserToken(models.Model):
    user = models.OneToOneField('users.Employee', on_delete=models.CASCADE)
    access_token = models.CharField(max_length=255)
    refresh_token = models.CharField(max_length=255)
    expires_at = models.DateTimeField()

    def __str__(self):
        return f"Token for {self.user.username}"


class DailyStep(models.Model):
    user = models.ForeignKey(Employee, on_delete=models.CASCADE)
    steps = models.IntegerField(default=0)
    date = models.DateField()

    def __str__(self):
        return f"{self.user.username} - {self.steps} steps on {self.date}"


class Goal(models.Model):
    description = models.TextField()
    points = models.IntegerField()
    steps = models.IntegerField(default=0)
    completed = models.BooleanField(default=False)

    def __str__(self):
        return self.description


class Achievement(models.Model):
    employee = models.ForeignKey(Employee, on_delete=models.CASCADE)
    Goal = models.ForeignKey(Goal,  on_delete=models.CASCADE)


class SleepData(models.Model):
    date = models.DateField(auto_now_add=True)
    hours_slept = models.DecimalField(max_digits=4, decimal_places=2)
    sleep_quality = models.DecimalField(max_digits=4, decimal_places=2)
    employee = models.ForeignKey(Employee, on_delete=models.CASCADE)

    def __str__(self):
        return f"{self.employee.username}'s sleep data for {self.date}"

# the get_category method in the User model needs to be implemented. Should return the user's category
# based on their personality or interactions with the web app. method should return None if the user has not
# taken a quiz yet.

# the needs_new_quiz method in the WeeklyQuiz model needs to be implemented. this method should
# return True if the user needs a new quiz (for example, if a week has passed since their last quiz),
# and False otherwise.

# the questions.json file needs to be updated to include the category for each question. since
# the initial quiz does not have a specific category, you can assign a general category to the initial questions.
