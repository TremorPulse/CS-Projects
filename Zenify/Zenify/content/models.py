from django.db import models


class Content(models.Model):
    title = models.CharField(max_length=200)
    description = models.TextField()
    # audio link from soundcloud
    audio_link = models.URLField(null=True, blank=True)


class ContentVideo(models.Model):
    title = models.CharField(max_length=200)
    video_link = models.URLField(
        null=True, blank=True)  # video link from pixaby
    runtime = models.DecimalField(decimal_places=2, max_digits=5)
    content = models.ForeignKey(
        Content, related_name='videos', on_delete=models.CASCADE)
