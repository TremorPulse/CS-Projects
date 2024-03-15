from django.shortcuts import render
from django.shortcuts import get_object_or_404
from django.http import HttpResponseRedirect
from django.urls import reverse_lazy
from django.contrib import messages
from django.views import generic
from django.contrib.messages.views import SuccessMessageMixin
from .models import Album, Song
from .forms import AlbumForm, SongForm

# View to show all albums.
def Show_Album(request):
    # Get all albums and create a list for rendering
    album = Album.objects.all()
    arr = []
    for i in album:
        arr.append({
            'id': i.id,
            'title': i.title,
            'format': i.format,
            'comments': i.comments,
            'cover': i.cover,
        })
    return render(request, 'albums.html', {'album_list': arr})

# View to display details of a specific album
def Album_Detail(request, album_id):
    album = get_object_or_404(Album, pk=album_id)
    return render(request, 'album_detail.html', {'album': album})

# View to list songs for a specific album.
def Song_List(request, album_id):
    album = get_object_or_404(Album, pk=album_id)
    songs = album.tracklist.all()
    other_songs = Song.objects.exclude(id__in=songs.values_list('id'))
    return render(request, 'songs.html', {'songs': songs, 'album': album, 'other_songs': other_songs})

# View for creating a new album.
class AlbumCreateView(SuccessMessageMixin, generic.edit.CreateView):
    form_class = AlbumForm
    template_name = 'album_form.html'
    success_message = 'Album Created'

    def get_success_url(self):
        return self.object.get_absolute_url()

# View for updating an existing album.
class AlbumUpdateView(SuccessMessageMixin, generic.edit.UpdateView):
    form_class = AlbumForm
    model = Album
    template_name = 'album_form.html'
    success_message = 'Album Updated'

    def get_success_url(self):
        return self.object.get_absolute_url()

# View for deleting an album.
def delete_album(request, album_id=None):
    album = get_object_or_404(Album, id=album_id)
    album.delete()
    messages.add_message(request, messages.INFO, 'Album Deleted')
    return HttpResponseRedirect(reverse_lazy('all_albums'))

# View for adding a song to an album.
def add_song_to_album(request, album_id=None, song_id=None):
    album = get_object_or_404(Album, id=album_id)
    song = get_object_or_404(Song, id=song_id)
    album.tracklist.add(song)
    messages.add_message(request, messages.INFO, 'Song Added')
    return HttpResponseRedirect(reverse_lazy('album_detail', kwargs={'album_id': album_id}))

# View for removing a song from an album.
def remove_song_from_album(request, album_id=None, song_id=None):
    album = get_object_or_404(Album, id=album_id)
    song = get_object_or_404(Song, id=song_id)
    album.tracklist.remove(song)
    messages.add_message(request, messages.INFO, 'Song Removed')
    return HttpResponseRedirect(reverse_lazy('album_detail', kwargs={'album_id': album_id}))