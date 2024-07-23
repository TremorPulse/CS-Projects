from django.core.management.base import BaseCommand
from django.contrib.auth import get_user_model
import json
from pathlib import Path
from activity.models import Goal
from quizzes.models import PersonalityQuestion
from users.models import Employee, Department, Company
from content.models import Content, ContentVideo
from users.models import Employee, Department, Company, User

ROOT_DIR = Path('app') / 'management' / 'commands'
JSON_DIR = Path('sample_data')


class Command(BaseCommand):
    def handle(self, *args, **options):
        PersonalityQuestion.objects.all().delete()
        Employee.objects.all().delete()
        Department.objects.all().delete()
        Company.objects.all().delete()
        Goal.objects.all().delete()
        ContentVideo.objects.all().delete()
        Content.objects.all().delete()
        User.objects.all().delete()

        with open(ROOT_DIR / 'sample_data.json') as json_file:
            sample_data = json.load(json_file)

        for question in sample_data['personality_questions']:
            q = PersonalityQuestion.objects.create(
                question=question['question'],
                category=question['category']
            )
            q.save()

        for company in sample_data['companies']:
            c = Company.objects.create(
                name=company['name']
                )

            for department in sample_data['departments']:
                d, created = Department.objects.get_or_create(
                    name=department['name'],
                    )
                c.departments.add(d)
            
        for employee in sample_data['employees']:
            company = Company.objects.get(
                name=employee['company']
                )
            department = Department.objects.get(
                name=employee['department']
                ) 
            e = User.objects.create_user(
                username=employee['username'],
                password=employee['password'],
                first_name=employee['first_name'],
                last_name=employee['last_name'],
                email=employee['email'],
                company=company,
                department=department,
                is_employee=True,
                is_employer=False
            )
            employee = Employee(
                user=e,
                dob=employee['dob'],
                personality_type=employee['personality_type']
            )
            employee.save()

        for goal in sample_data['Goals']:
            g = Goal.objects.create(
                description=goal['description'],
                points=goal['points'],
                steps=goal['steps']
            )
            g.save()

        contents = sample_data['contents']
        for content in contents:
            # Create or update the Content instance
            content_instance, _ = Content.objects.update_or_create(
                title=content['title'],
                defaults={
                    'description': content['description'],
                    'audio_link': content['audio_link']
                }
            )

            # Assuming 'videos' is part of each content entry in your sample_data
            for video in content['videos']:
                # Create or update the ContentVideo instance
                video_instance, _ = ContentVideo.objects.update_or_create(
                    title=video['title'],
                    content=content_instance,  # Link directly during creation
                    defaults={
                        'video_link': video['video_link'],
                        'runtime': video['runtime'],
                    }
                )

            # No need to manually add video_instance to content_instance if linked at creation
            content_instance.save()  # Save changes if any additional manipulations were done
