from django.urls import path
from .views import ContentView
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path('contents/', ContentView.as_view(), name='content-list'),
] + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
