from rest_framework import serializers
from . import PersonalityQuestion, PersonalityQuiz, Question, Quiz, MultipleChoiceQuestion, WeeklyQuiz

class WellBeingQuestionSerializer(serializers.ModelSerializer):
    class Meta:
        model = WellBeingQuestion
        fields = '__all__'

class PersonalityQuestionSerializer(serializers.ModelSerializer):
    class Meta:
        model = PersonalityQuestion
        fields = '__all__'

# class UserAnswerSerializer(serializers.ModelSerializer):
#     wellbeing_question = WellBeingQuestionSerializer(read_only=True)
#     personality_question = PersonalityQuestionSerializer(read_only=True)

#     class Meta:
#         model = UserAnswer
#         fields = '__all__'

class WeeklyQuizSerializer(serializers.ModelSerializer):
    well_being_questions = WellBeingQuestionSerializer(many=True, read_only=True)

    class Meta:
        model = WeeklyQuiz
        fields = '__all__'

class PersonalityQuizSerializer(serializers.ModelSerializer):
    personality_questions = PersonalityQuestionSerializer(many=True, read_only=True)

    class Meta:
        model = PersonalityQuiz
        fields = '__all__'
