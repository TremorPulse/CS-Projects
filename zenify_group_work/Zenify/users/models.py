from datetime import timezone
from django.db import models
from django.contrib.auth.models import AbstractUser
from django.apps import apps
from django.db.models import Sum

class User(AbstractUser):
    company = models.ForeignKey('Company', on_delete=models.CASCADE)
    department = models.ForeignKey('Department', on_delete=models.CASCADE)
    is_employee = models.BooleanField(default=False)
    is_employer = models.BooleanField(default=False)
    
    def __str__(self):
        return self.username

class Department(models.Model):
    name = models.CharField(max_length=255, null=True, blank=True)
    
    def __str__(self):
        return self.name

class Company(models.Model):
    name = models.CharField(max_length=255, null=True, blank=True)
    departments = models.ManyToManyField(Department, related_name='departments', blank=False)

    def __str__(self):
        return self.name

class Employer(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=True)
    position = models.CharField(max_length=255, default='employer')

class Employee(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=True)
    dob = models.DateField(null=True)
    class DiscCategory(models.TextChoices):
        D = "Dominance"
        I = "Influence"
        S = "Steadiness"
        C = "Conscientiousness"
    personality_type = models.CharField(max_length=255, choices=DiscCategory.choices, default="Category", null=True)

    def calculate_personality_category(self):
        UserAnswer = apps.get_model('app', 'UserAnswer')
        Quiz = apps.get_model('app', 'Quiz')  # Import Quiz dynamically
        category_scores = UserAnswer.objects.filter(
            user=self,
            personality_question__isnull=False
        ).values(
            'personality_question__category'
        ).annotate(
            total=Sum('personality_answer')
        )
        return max(category_scores, key=lambda x: x['total'])['personality_question__category']

    @property
    def quizzes(self):
        Quiz = apps.get_model('app', 'Quiz')
        return Quiz.objects.filter(useranswer__user=self)