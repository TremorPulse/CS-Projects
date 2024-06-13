from django.test import TestCase
from django.core.exceptions import ValidationError
from .models.user_models import Employee, Employer, Company, Department, PersonalityType
from .models.quizzes_models import WellBeingQuestion, PersonalityQuestion, UserAnswer, WeeklyQuiz, PersonalityQuiz
from django.contrib.contenttypes.models import ContentType

class QuizModelTest(TestCase):
    @classmethod
    def setUpTestData(cls):
        cls.company = Company.objects.create(name='Test Company')
        cls.department = Department.objects.create(name='Test Department', company=cls.company)
        cls.personality_type = PersonalityType.objects.create(name='Test Personality')
        cls.well_being_question = WellBeingQuestion.objects.create(text='Test WellBeing Question')
        cls.personality_question = PersonalityQuestion.objects.create(text='Test Personality Question', category=PersonalityQuestion.DiscCategory.D)
        cls.employee = Employee.objects.create(username='testuser', company=cls.company, department=cls.department, personality_type=cls.personality_type)
        cls.employer = Employer.objects.create(username='testadmin', company=cls.company, department=cls.department)
        cls.weekly_quiz = WeeklyQuiz.objects.create(user=cls.employee, assigned_by=cls.employer, week_number=1)
        cls.weekly_quiz.well_being_questions.add(cls.well_being_question)
        cls.personality_quiz = PersonalityQuiz.objects.create(user=cls.employee, assigned_by=cls.employer, week_number=1)
        cls.personality_quiz.personality_questions.add(cls.personality_question)

    def test_well_being_question_creation(self):
        self.assertEqual(self.well_being_question.text, 'Test WellBeing Question')

    def test_personality_question_creation(self):
        self.assertEqual(self.personality_question.text, 'Test Personality Question')
        self.assertEqual(self.personality_question.category, PersonalityQuestion.DiscCategory.D)

    def test_weekly_quiz_creation(self):
        self.assertEqual(self.weekly_quiz.user, self.employee)
        self.assertEqual(self.weekly_quiz.assigned_by, self.employer)
        self.assertEqual(self.weekly_quiz.week_number, 1)
        self.assertEqual(self.weekly_quiz.completed, False)
        self.assertEqual(list(self.weekly_quiz.well_being_questions.all()), [self.well_being_question])

    def test_personality_quiz_creation(self):
        self.assertEqual(self.personality_quiz.user, self.employee)
        self.assertEqual(self.personality_quiz.assigned_by, self.employer)
        self.assertEqual(self.personality_quiz.week_number, 1)
        self.assertEqual(self.personality_quiz.completed, False)
        self.assertEqual(list(self.personality_quiz.personality_questions.all()), [self.personality_question])

class UserModelTest(TestCase):
    @classmethod
    def setUpTestData(cls):
        cls.company = Company.objects.create(name='Test Company')
        cls.department = Department.objects.create(name='Test Department', company=cls.company)
        cls.personality_type = PersonalityType.objects.create(name='Test Personality')
        cls.question = PersonalityQuestion.objects.create(text='Test Question',
                                                          category=PersonalityQuestion.DiscCategory.D)
        cls.employee = Employee.objects.create(username='testuser', company=cls.company, department=cls.department, personality_type=cls.personality_type)
        cls.employer = Employer.objects.create(username='testadmin', company=cls.company, department=cls.department)

    def test_employee_creation(self):
        employee = Employee.objects.get(username='testuser')
        self.assertEqual(employee.username, 'testuser')
        self.assertEqual(employee.company, self.company)
        self.assertEqual(employee.department, self.department)
        self.assertEqual(employee.personality_type, self.personality_type)

    def test_employer_creation(self):
        employer = Employer.objects.get(username='testadmin')
        self.assertEqual(employer.username, 'testadmin')
        self.assertEqual(employer.company, self.company)
        self.assertEqual(employer.department, self.department)

    def test_company_creation(self):
        self.assertEqual(self.company.name, 'Test Company')

    def test_department_creation(self):
        self.assertEqual(self.department.name, 'Test Department')
        self.assertEqual(self.department.company, self.company)

    def test_personality_type_creation(self):
        self.assertEqual(self.personality_type.name, 'Test Personality')

    def test_calculate_personality_category(self):
        employee = Employee.objects.get(username='testuser')
        if isinstance(self.question, WellBeingQuestion):
            UserAnswer.objects.create(user=employee, wellbeing_question=self.question, well_being_answer=5)
        elif isinstance(self.question, PersonalityQuestion):
            UserAnswer.objects.create(user=employee, personality_question=self.question, personality_answer=5)
        else:
            raise ValueError("Invalid question type")
        category = employee.calculate_personality_category()
        self.assertEqual(category, PersonalityQuestion.DiscCategory.D)

    def test_employee_username_max_length(self):
        employee = Employee.objects.get(username='testuser')
        employee.username = 'a' * 256
        with self.assertRaises(ValidationError):
            employee.full_clean()

    def test_employer_username_max_length(self):
        employer = Employer.objects.get(username='testadmin')
        employer.username = 'a' * 256
        with self.assertRaises(ValidationError):
            employer.full_clean()


