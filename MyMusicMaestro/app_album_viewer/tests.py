from django.test import TestCase
from django.core.exceptions import ValidationError
from django.utils import timezone
from django.core.files.uploadedfile import SimpleUploadedFile
from app_album_viewer.models import release_date_validation, Album, Comment, Song, User
from django.test import Client
from django.urls import reverse
from django.contrib.auth.models import AnonymousUser
from django.contrib.messages import get_messages

# Test case for views.
class ViewTestCase(TestCase):
    def setUp(self):
        self.client = Client()
        self.user = User.objects.create_user(username='testuser', password='testpassword')
        self.cover = SimpleUploadedFile("cover.jpg", b"file_content", content_type="image/jpg")
        self.album = Album.objects.create(
            cover=self.cover,
            title='Test Album',
            artist='Test Artist',
            description='Test Description',
            price=9.99,
            format='CD',
            release_date=timezone.now().date(),
        )
        self.song = Song.objects.create(
            title='Test Song',
            running_time=timezone.timedelta(seconds=120),
        )
        self.album.songs.add(self.song)

        # Test the show_album view. Check correct template and response is made with proper format.
    def test_show_album_view(self):
        response = self.client.get(reverse('all_albums'))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'albums.html')
        self.assertContains(response, 'Test Album')

        # Test the album_detail view. Check correct template and response is made with proper format.
    def test_album_detail_view(self):
        response = self.client.get(reverse('album_detail', kwargs={'album_id': self.album.id}))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'album_detail.html')
        self.assertContains(response, 'Test Album')
        self.assertContains(response, 'CD')
        self.assertContains(response, str(self.album.comments.count()))

        # Test the song_detail view. Check correct template and response is made with proper format.
    def test_song_list_view(self):
        response = self.client.get(reverse('song_list', kwargs={'album_id': self.album.id}))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'songs.html')
        self.assertContains(response, 'Test Song')
