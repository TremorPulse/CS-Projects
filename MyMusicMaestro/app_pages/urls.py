from django.urls import path
from app_album_viewer.models import *
from app_pages import views

urlpatterns = [
    path('', views.show_home, name='home'),
    path('contact/', views.show_contact, name='contact'),
    path('about/', views.show_about, name='about'),
    path('account/', views.show_account, name='page_account'),
    path('recommend-a-friend/<int:id>/', views.recommend_album, name='recommend_album'),
]