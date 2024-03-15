import json
from pathlib import Path
from datetime import datetime
from django.core.management.base import BaseCommand
from django.core.files.images import ImageFile
from django.utils import timezone
from django.contrib.auth.models import User
from app_album_viewer.models import Album, User, Song, Comment

# We define the root directory for our file operations.
ROOT_DIR = Path('app_album_viewer') / 'management'

class Command(BaseCommand):
    help = "Seed database."

    # Main function for handling our seeding process.
    def handle(self, *args, **options):
        # Clear any existing data in the database.
        Album.objects.all().delete()
        User.objects.all().delete()
        Song.objects.all().delete()
        Comment.objects.all().delete()

        # Create a default user for testing purposes.
        User.objects.create_user(username='user', display_name='user', password='password')

        assert not Comment.objects.all()

        # Load sample data from our JSON file.
        with open(ROOT_DIR / "sample_data.json") as json_file:
            sample_data = json.load(json_file)

        # Seed albums and associated comments and define our release date format.
        for album_data in sample_data['albums']:
            release_date = datetime.strptime(album_data['release_date'], "%Y-%m-%d")

            # Create an Album instance.
            album_instance = Album.objects.create(
                title=album_data['title'],
                description=album_data['description'],
                artist=album_data['artist'],
                price=album_data['price'],
                format=album_data['format'],
                release_date=release_date
            )

            # Handle album cover if available in the sample data.
            image_path = album_data.get('cover')
            # Check if the image path exists and specify the cover art path and image file.
            if image_path:
                cover_art_path = Path('app_album_viewer/media/covers') / image_path
                cover_image = ImageFile(open(cover_art_path, 'rb'), name=image_path)
                # Associate the cover image with the album and save that instance of the album.
                album_instance.cover = cover_image
                album_instance.save()

            # Add comments to the album and associate them with the user.
            for comment_data in album_data['comments']:
                user_display_name = comment_data.get('user__display_name')
                message = comment_data['message']

                if user_display_name:
                    # Generate a unique username based on display name.
                    username_parts = [part.lower() for part in user_display_name.split()]
                    username = '.'.join(username_parts)

                    # Try to find a user with the generated username.
                    try:
                        user = User.objects.get(username=username)
                    except User.DoesNotExist:
                        # Create a new user if not found.
                        user = User.objects.create_user(username=username, password='password')
                        user.refresh_from_db()
                        # Set the display name for the user as the concatenation of the display name and user id, then save.
                        user.display_name = f"{user_display_name}_{user.id}"
                        user.save()

                    # Create a comment associated with the user and album.
                    try:
                        Comment.objects.create(
                            user=user,
                            album=album_instance,
                            text=message
                        )
                    except Exception as e:
                        print(f"Error creating comment: {e}")

        # Seed songs and associate them with albums.
        for song_data in sample_data['songs']:
            title = song_data['title']
            running_time = song_data['runtime']
            album_titles = song_data['albums']

            # Create a Song instance.
            song_instance = Song.objects.create(title=title, running_time=timezone.timedelta(seconds=running_time))

            # Associate the song with albums,
            for album_title in album_titles:
                # Filter albums based on the title.
                albums = Album.objects.filter(title=album_title)

                for album in albums:
                    try:
                        # Associate the song with the album and vice versa.
                        song_instance.albums.add(album)
                        album.tracklist.add(song_instance)
                    except Album.DoesNotExist:
                        print(f"No album found for title: {album_title}")

        print("Seeding complete.")
