from rest_framework import serializers
from .models import Company, Department, User, Employer, Employee
from django.db import transaction

class CompanySerializer(serializers.ModelSerializer):
    class Meta:
        model = Company
        fields = '__all__'

class DepartmentSerializer(serializers.ModelSerializer):
    company = CompanySerializer(read_only=True)

    class Meta:
        model = Department
        fields = ['name', 'company']

# class UserSerializer(serializers.ModelSerializer):
#     company = serializers.CharField()
#     department = serializers.CharField()

#     class Meta:
#         model = User
#         fields = ('id', 'username', 'email', 'password', 'is_employee', 'is_employer', 'company', 'department')
#         extra_kwargs = {'password': {'write_only': True}}

#     def create(self, validated_data):
#         company_name = validated_data.pop('company')
#         department_name = validated_data.pop('department')

#         with transaction.atomic():
#             company, _ = Company.objects.get_or_create(name=company_name)
#             department, _ = Department.objects.get_or_create(name=department_name)

#             user = User.objects.create_user(**validated_data, company=company, department=department)
#         return user

class UserSerializer(serializers.ModelSerializer):
    company = serializers.SlugRelatedField(slug_field='name', queryset=Company.objects.all())
    department = serializers.SlugRelatedField(slug_field='name', queryset=Department.objects.all())

    class Meta:
        model = User
        fields = ('id', 'username', 'email', 'password', 'is_employee', 'is_employer', 'company', 'department')
        extra_kwargs = {'password': {'write_only': True}}

    def create(self, validated_data):
        company_name = validated_data.pop('company')
        department_name = validated_data.pop('department')

        with transaction.atomic():
            company, _ = Company.objects.get_or_create(name=company_name)
            department, _ = Department.objects.get_or_create(name=department_name)

            user = User.objects.create_user(**validated_data, company=company, department=department)
        return user

class EmployerSerializer(serializers.ModelSerializer):
    user = UserSerializer(read_only=True)

    class Meta:
        model = Employer
        fields = ['user', 'position']

class EmployeeSerializer(serializers.ModelSerializer):
    user = UserSerializer(read_only=True)
    # personality_category = serializers.SerializerMethodField()
    class Meta:
        model = Employee
        fields = ['user', 'dob', 'personality_type']

    def get_personality_category(self, obj):
        return obj.calculate_personality_category()
