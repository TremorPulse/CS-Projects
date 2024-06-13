from django.urls import path
from . import views
from .views import RegisterView, LoginView, LogoutView, VerifyEmailView

urlpatterns = [
    path('home/', views.HomeView.as_view(), name='home'),
    path('dashboard/', views.DashboardView.as_view(), name='view_dashboard'),
    path('register/', RegisterView.as_view()),
    path('verify/', VerifyEmailView.as_view(), name='activate'),
    path('login/', LoginView.as_view()),
    path('logout/', LogoutView.as_view()),
]