import json
import random
from django.db import models
# from django.contrib.contenttypes.fields import GenericRelation

from users.models import Employee, Employer

class Question(models.Model):
    text = models.TextField(max_length=255)

    class Meta:
        abstract = True

# class WellBeingQuestion(Question):
#     user_answers = models.ForeignKey('UserAnswer', on_delete=models.CASCADE, related_name='wellbeing_question')

class PersonalityQuestion(models.Model):
    class DiscCategory(models.TextChoices):
        D = "Dominance"
        I = "Influence"
        S = "Steadiness"
        C = "Conscientiousness"
    question = models.TextField(default="question")
    category = models.CharField(max_length=255, choices=DiscCategory.choices, default="Category", null=True)
    # user_answers = models.ForeignKey('UserAnswer', on_delete=models.CASCADE, related_name='personality_question')

# class UserAnswer(models.Model):
#     user = models.ForeignKey(Employee, on_delete=models.CASCADE, null=True)
#     # wellbeing_question = models.ForeignKey(WellBeingQuestion, on_delete=models.CASCADE, null=True, blank=True)
#     # personality_question = models.ForeignKey(PersonalityQuestion, on_delete=models.CASCADE, null=True, blank=True)
#     WELL_BEING_OPTIONS = ["Excellent", "Good", "Fair", "Poor", "Very Poor"]
#     PERSONALITY_OPTIONS = [(i, str(i)) for i in range(1, 6)]  # Answer options from 1 to 5
#     well_being_answer = models.CharField(max_length=10, choices=[(o, o) for o in WELL_BEING_OPTIONS], null=True, blank=True)
#     personality_answer = models.IntegerField(choices=PERSONALITY_OPTIONS, null=True, blank=True)


class Quiz(models.Model):
    user = models.ForeignKey(Employee, on_delete=models.CASCADE)
    assigned_by = models.ForeignKey(Employer, on_delete=models.CASCADE, null=True, related_name="%(class)s_assigned_quizzes")
    week_number = models.IntegerField(null=True)
    completed = models.BooleanField(default=False)
    timestamp = models.DateTimeField(auto_now_add=True, null=True)

    class Meta:
        abstract = True

# class WeeklyQuiz(Quiz):
#     well_being_questions = models.ManyToManyField(WellBeingQuestion, blank=True)

# class PersonalityQuiz(Quiz):
#     personality_questions = models.ManyToManyField(PersonalityQuestion, blank=True)

class MultipleChoiceQuestions(models.Model):
    question_text = models.CharField(max_length=200)
    answer_options = models.JSONField()  # Store possible answers as JSON
    class Category(models.TextChoices):
        c1 = "Physical Health"
        c2 = "Mental Health"
        c3 = "Work Health"
    category = models.CharField(max_length=200)  # New field for question category

class WeeklyQuiz(models.Model):
    user = models.ForeignKey(Employee, on_delete=models.CASCADE, null=True)  # Quiz based on user personality
    week_number = models.IntegerField()  # Week number of the quiz
    questions = models.ManyToManyField(MultipleChoiceQuestions)  # Set of questions for the quiz

    @classmethod
    def generate_weekly_quiz(cls, user):
        # Load questions from JSON file
        with open('questions.json', 'r') as f:
            questions_data = json.load(f)

        # Create MCQuestion instances for each question
        for question_data in questions_data:
            question, created = MultipleChoiceQuestions.objects.get_or_create(
                question_text=question_data['question'],
                defaults={'answer_options': question_data['options'], 'category': question_data['category']}
            )

        # Get all questions.
        questions = MultipleChoiceQuestions.objects.all()

        # Categorize questions based on personality type.
        categories = {
            'Dominance': questions.filter(category='Dominance'),
            'Influence': questions.filter(category='Influence'),
            'Steadiness': questions.filter(category='Steadiness'),
            'Conscientiousness': questions.filter(category='Conscientiousness'),
        }

        # Determine the user's personality type or interaction type.
        user_category = user.get_category() if cls.objects.filter(user=user).exists() else None

        # Select 5-8 random questions from the appropriate category.
        if user_category:
            selected_questions = random.sample(list(categories[user_category]), random.randint(5, 8))
        else:
            # If it's the first quiz, select questions randomly from all categories.
            selected_questions = random.sample(list(questions), random.randint(5, 8))

        # Calculate the week number.
        week_number = cls.objects.filter(user=user).count() + 1

        # Create a new quiz.
        quiz = cls(user=user, week_number=week_number)
        quiz.save()

        # Set the questions for the quiz.
        quiz.questions.set(selected_questions)
        quiz.save()