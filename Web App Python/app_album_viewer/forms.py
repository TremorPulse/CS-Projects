from django import forms
from .models import Song
from .models import Album
from django import forms
from .models import Song
from .models import Album

# Form for Album model
class AlbumForm(forms.ModelForm):
    class Meta:
        # Specify the model and fields for the AlbumForm.
        model = Album
        fields = ['title', 'artist', 'format', 'release_date', 'price', 'description', 'cover']

# Form for Song model
class SongForm(forms.ModelForm):
    class Meta:
        # Specify the model and fields for the SongForm.
        model = Song
        fields = ['title', 'running_time', 'albums']
        # Customize widget attributes for better user interaction.
        widgets = {
            'title': forms.TextInput(attrs={'class': 'form-control'}),
            'running_time': forms.TimeInput(attrs={'class': 'form-control', 'type': 'time'}),
            'albums': forms.SelectMultiple(attrs={'class': 'form-control'})
        }