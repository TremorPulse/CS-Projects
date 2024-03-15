from django import forms

# Form for recommending an album via email
class RecommendAlbumForm(forms.Form):
    to = forms.EmailField(label='To', required = True)
    subject = forms.CharField(label='Subject', max_length=100, required = True)
    message = forms.CharField(label='Message', widget=forms.Textarea, required = True)