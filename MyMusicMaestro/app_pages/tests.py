from django.test import TestCase
from django.core.exceptions import ValidationError
from django.utils import timezone
from django.core.files.uploadedfile import SimpleUploadedFile
from app_album_viewer.models import release_date_validation, Album, Comment, Song, User
from django.test import Client
from django.urls import reverse
from django.contrib.auth.models import AnonymousUser
from django.contrib.messages import get_messages

# Test case for release date validation function
class ReleaseDateValidationTest(TestCase):
    # Test a valid release date for less than one year ago.
    def test_valid_release_date(self):
        valid_date = timezone.now().date() - timezone.timedelta(days=365)
        self.assertEqual(release_date_validation(valid_date), valid_date)

    # Test an invalid release date for more than three years ago.
    def test_invalid_release_date_more_than_three_years(self):
        invalid_date = timezone.now().date() - timezone.timedelta(days=1200)
        self.assertEqual(release_date_validation(invalid_date), invalid_date)

    # Test an invalid release date for between one and three years ago.
    def test_invalid_release_date_between_one_and_three_years(self):
        invalid_date = timezone.now().date() - timezone.timedelta(days=700)
        expected_date = invalid_date.replace(month=1)  # Adjust to January.
        self.assertEqual(release_date_validation(invalid_date), expected_date)

# Test case for Album model.
class AlbumModelTest(TestCase):
    # Set up a test album instance
    def setUp(self):
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

    # Test a valid album instance.
    def test_valid_album_creation(self):
        self.assertEqual(self.album.title, 'Test Album')
        self.assertEqual(self.album.artist, 'Test Artist')
        self.assertEqual(self.album.description, 'Test Description')
        self.assertEqual(self.album.price, 9.99)
        self.assertEqual(self.album.format, 'CD')
        self.assertEqual(self.album.release_date, timezone.now().date())
        self.assertEqual(self.album.tracklist.count(), 0)

    # Test an album title that is too long.
    def test_album_title(self):
        self.album.title = 'A' * 100
        with self.assertRaises(ValidationError):
            self.album.full_clean()

    # Test an album artist that is too long.
    def test_album_artist(self):
        self.album.artist = 'A' * 100
        with self.assertRaises(ValidationError):
            self.album.full_clean()

    # Test an album description that is too long.
    def test_album_description(self):
        self.album.description = 'A' * 1000
        with self.assertRaises(ValidationError):
            self.album.full_clean()

    # Test an album price that is negative.
    def test_album_price_negative(self):
        self.album.price = -9.99
        with self.assertRaises(ValidationError):
            self.album.full_clean()

    # Test an album price that is too large.
    def test_album_price_max_digits(self):
        self.album.price = 123456789012345.6789
        with self.assertRaises(ValidationError):
            self.album.full_clean()

    # Test an album price that has too many decimal places.
    def test_album_price_max_decimal_places(self):
        self.album.price = 9.123
        with self.assertRaises(ValidationError):
            self.album.full_clean()


    # Test an album format that is not in the choices.
    def test_album_format_choices(self):
        self.album.format = 'Invalid Format'
        with self.assertRaises(ValidationError):
            self.album.full_clean()

    # Test an album release date that is in the future.
    def test_album_release_date_future(self):
        future_date = timezone.now().date() + timezone.timedelta(days=30)
        self.album.release_date = future_date
        with self.assertRaises(ValidationError):
            self.album.full_clean()

    # Test an album release date that is more than three years ago.
    def test_album_cover_missing(self):
        self.album.cover = None
        with self.assertRaises(ValidationError):
            self.album.full_clean()

# Test case for Comment model.
class CommentModelTest(TestCase):
    def setUp(self):
        # Set up a test user, album, and comment instance
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
        self.comment = Comment.objects.create(
            user=self.user,
            album=self.album,
            text='Test Comment',
        )

    def test_valid_comment_creation(self):
        # Test a valid comment creation with default values
        self.assertEqual(self.comment.user, self.user)
        self.assertEqual(self.comment.album, self.album)
        self.assertEqual(self.comment.text, 'Test Comment')

    # Test a comment with no user.
    def test_comment_user_missing(self):
        self.comment.user = None
        with self.assertRaises(ValidationError):
            self.comment.full_clean()

    # Test a comment with no album.
    def test_comment_album_missing(self):
        self.comment.album = None
        with self.assertRaises(ValidationError):
            self.comment.full_clean()

    # Test a comment with no text.
    def test_comment_text_empty(self):
        self.comment.text = ''
        with self.assertRaises(ValidationError):
            self.comment.full_clean()

    # Test a comment with text that is too long.
    def test_comment_text_max_length(self):
        self.comment.text = 'A' * 1000
        with self.assertRaises(ValidationError):
            self.comment.full_clean()

# Test case for Song model.
class SongModelTest(TestCase):
    def setUp(self):
        # Set up a test album and song instance
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

    # Test a valid song creation.
    def test_valid_song_creation(self):
        self.assertEqual(self.song.title, 'Test Song')
        self.assertEqual(self.song.running_time, timezone.timedelta(seconds=120))
        self.assertEqual(self.song.albums.count(), 1)

    # Test a song title that is too long.
    def test_song_title_missing(self):
        self.song.title = ''
        with self.assertRaises(ValidationError):
            self.song.full_clean()

    # Test a song title that is too long.
    def test_song_title_max_length(self):
        self.song.title = 'A' * 101
        with self.assertRaises(ValidationError):
            self.song.full_clean()

    def test_song_running_time_min_value(self):
        self.song.running_time = timezone.timedelta(seconds=0)
        with self.assertRaises(ValidationError):
            self.song.full_clean()


# Test case for User model.
class UserModelTest(TestCase):
    def setUp(self):
        # Set up a test user instance.
        self.user = User.objects.create_user(
            username='testuser',
            password='testpassword',
            display_name='Test User',
        )

    # Test a valid user creation.
    def test_valid_user_creation(self):
        self.assertEqual(self.user.username, 'testuser')
        self.assertEqual(self.user.display_name, 'Test User')

    # Test a user with no username.
    def test_user_username_missing(self):
        self.user.username = ''
        with self.assertRaises(ValidationError):
            self.user.full_clean()

    # Test a user with no password.
    def test_user_display_name_max_length(self):
        self.user.display_name = 'A' * 101
        with self.assertRaises(ValidationError):
            self.user.full_clean()

# Test case for views.
class TestViews(TestCase):
    def setUp(self):
        # Set up a test client and urls.
        self.client = Client()
        self.home_url = reverse('home')
        self.about_url = reverse('about')
        self.contact_url = reverse('contact')
        self.account_url = reverse('page_account')
        self.recommend_album_url = reverse('recommend_album', args=['1'])
        self.user = User.objects.create(username='testuser')
        self.user.set_password('12345')
        self.user.save()
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

    # Test that the home page is rendered correctly.
    def test_show_home_GET(self):
        response = self.client.get(self.home_url)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'home.html')

    # Test that the about page is rendered correctly.
    def test_show_about_GET(self):
        response = self.client.get(self.about_url)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'about.html')

    # Test that the contact page is rendered correctly.
    def test_show_contact_GET(self):
        response = self.client.get(self.contact_url)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'contact.html')

    # Test that the account page is rendered correctly.
    def test_show_account_GET(self):
        self.client.login(username='testuser', password='12345')
        response = self.client.get(self.account_url)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'account.html')

    # Test that the account page is not rendered if the user is not logged in.
    def test_recommend_album_GET(self):
        self.client.login(username='testuser', password='12345')
        response = self.client.get(self.recommend_album_url)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'recommend_album.html')

    # Test that the account page is not rendered if the user is not logged in.
    def test_recommend_album_POST(self):
        self.client.login(username='testuser', password='12345')
        response = self.client.post(self.recommend_album_url, {
            'subject': 'Check out this album!',
            'message': 'This is a great album!',
            'to': 'test@example.com',
        })
        self.assertEquals(response.status_code, 302)
        messages = list(get_messages(response.wsgi_request))
        self.assertEqual(len(messages), 1)
        self.assertEqual(str(messages[0]), 'Email Sent')

    # Test that the account page is not rendered if the user is not logged in.
    def test_recommend_album_not_logged_in(self):
        response = self.client.get(self.recommend_album_url)
        self.assertEquals(response.status_code, 302)
        messages = list(get_messages(response.wsgi_request))
        self.assertEqual(len(messages), 1)
        self.assertEqual(str(messages[0]), 'You must be logged in to recommend an album.')