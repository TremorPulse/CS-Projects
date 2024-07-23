from django.db import models
from django.core.validators import MinValueValidator
from django.conf import settings
from django.template.defaultfilters import slugify
from django.contrib.auth.models import User
from django.contrib.auth.models import AbstractUser
from django.utils import timezone
from django.core.exceptions import ValidationError
from django.contrib.auth.models import Group, Permission
from datetime import datetime
from django.urls import reverse

def release_date_validation(value):
    if value > timezone.now().date() and value.year - timezone.now().year > 3:
        raise ValidationError("Invalid release date.")
    elif value > timezone.now().date() and value.year - timezone.now().year > 1:
        value = value.replace(month=1)
    return value

class Album(models.Model):
    cover = models.ImageField(default='covers/default.png', blank=True)
    title = models.CharField(max_length=255)
    description = models.TextField(blank=True, null=True)
    artist = models.CharField(max_length=255)
    price = models.DecimalField(max_digits=10, decimal_places=2, null=True, validators=[MinValueValidator(0)])
    FORMAT_CHOICES = (
        ('Digital download', 'Digital download'),
        ('CD', 'CD'),
        ('Vinyl', 'Vinyl'),
    )
    format = models.CharField(max_length=20, choices=FORMAT_CHOICES)
    release_date = models.DateField(validators=[release_date_validation], blank=False)
    tracklist = models.ManyToManyField('Song', blank=True, related_name='albums_in_tracklist')
    comments = models.ManyToManyField('User', through='Comment')

    def get_absolute_url(self):
        return reverse('album_detail', args=[str(self.id)])

class Comment(models.Model):
    user = models.ForeignKey('User', on_delete=models.CASCADE, blank=False)
    album = models.ForeignKey(Album, on_delete=models.CASCADE, blank=False)
    text = models.CharField(max_length=255)

class Song(models.Model):
    title = models.CharField(max_length=100)
    running_time = models.DurationField(validators=[MinValueValidator(timezone.timedelta(seconds=1))])
    albums = models.ManyToManyField('Album', blank=True, related_name='songs')

class User(AbstractUser):
    display_name = models.CharField(max_length=100)
    groups = models.ManyToManyField(Group, related_name='user_groups', blank=True)
    user_permissions = models.ManyToManyField(Permission, related_name='user_user_permissions', blank=True)

