from django.urls import path
from app_album_viewer.views import Show_Album, Album_Detail, Song_List, AlbumCreateView, AlbumUpdateView
from app_album_viewer.views import add_song_to_album, remove_song_from_album, delete_album

urlpatterns = [
    path('', Show_Album, name='all_albums'),
    path('<int:album_id>/', Album_Detail, name='album_detail'),
    path('<int:album_id>/songs/', Song_List, name='song_list'),
    path('new/', AlbumCreateView.as_view(), name='album_new'),
    path('<int:pk>/edit/', AlbumUpdateView.as_view(), name='album_edit'),
    path('<int:album_id>/delete/', delete_album, name='album_delete'),
    path('<int:album_id>/add_song/<int:song_id>/', add_song_to_album, name='add_song_to_album'),
    path('<int:album_id>/remove_song/<int:song_id>/', remove_song_from_album, name='remove_song_from_album'),
]
