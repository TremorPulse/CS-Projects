from django.contrib import admin
from django.urls import include, path
from rest_framework_simplejwt import views as jwt_views

urlpatterns = [
    path('', include("app.urls")),
    path('', include('activity.urls')),
    path('', include('content.urls')),
    path('admin/', admin.site.urls),
    path('users/token/', 
        jwt_views.TokenObtainPairView.as_view(),  
        name ='token_obtain_pair'),
    path('users/token/refresh/', 
        jwt_views.TokenRefreshView.as_view(), 
        name ='token_refresh'),
    path('users-auth/', include('rest_framework.urls'))
]
