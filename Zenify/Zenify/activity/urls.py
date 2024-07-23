from django.contrib import admin
from django.urls import path
from . import views
from .views import (
    homeview, google_fit_login, google_fit_callback, check_signin_status,
    get_steps, StepDataView, GoalListView, CreateAchievementView
)

urlpatterns = [
    path('', views.homeview, name='home'),
    path('google/login/', views.google_fit_login, name='google_fit_login'),
    path('google/callback/', views.google_fit_callback,
         name='google_fit_callback'),
    path('check-signin/', views.check_signin_status, name='check_signin_status'),
    path('get-steps/', views.get_steps, name='get_steps'),
    path('api/steps/', views.StepDataView.as_view(), name='step_data'),
    path('goals/', GoalListView.as_view(), name='goal-list'),
    path('achievements/', CreateAchievementView.as_view(),
         name='create-achievement'),
    path('admin/', admin.site.urls),
]
