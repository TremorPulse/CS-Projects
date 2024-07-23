from django.shortcuts import render
from app_album_viewer.models import *
from django.core.management import call_command
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.contrib.auth.forms import AuthenticationForm
from django.shortcuts import render, get_object_or_404
from app_album_viewer.models import User
from .forms import RecommendAlbumForm
from django.core.mail import send_mail
from django.template.loader import render_to_string
from django.contrib import messages

# View for the home page.
def show_home(request):
    return HttpResponse(render(request, 'home.html'))

# View for the about page.
def show_about(request):
    return HttpResponse(render(request, 'about.html'))

# View for the albums page.
def show_album(request):
    return HttpResponse(render(request, 'albums.html'))

# View for the contact page.
def show_contact(request):
    return HttpResponse(render(request, 'contact.html'))

# View for the account page.
def show_account(request):
    custom_user = request.user
    return HttpResponse(render(request, 'account.html',
                               context={'custom_user': custom_user}))

# View for the recommend album page.
def recommend_album(request, id):
    if not request.user.is_authenticated:
        messages.add_message(request, messages.ERROR, 'You must be logged in to recommend an album.')
        return HttpResponseRedirect(reverse('home'))

    # Get the album object based on the provided ID
    album = get_object_or_404(Album, pk=id)

    # Handle GET request to pre-fill the form with default values.
    if request.method == 'GET':
        subject = 'Check out this album!'
        form = RecommendAlbumForm({'subject': subject})

    # Handle POST request to send the email.
    else:
        form = RecommendAlbumForm(request.POST)

        if form.is_valid():
            # Send the email with the provided information.
            send_mail(
                subject=form.cleaned_data['subject'],
                message=form.cleaned_data['message'],
                from_email=None,
                recipient_list=[form.cleaned_data['to']],
            )
            # Display success message and redirect to the album's detail page
            messages.add_message(request, messages.SUCCESS, 'Email Sent')
            return HttpResponseRedirect(album.get_absolute_url())

    # Render the recommend_album.html template with the form and album information.
    return HttpResponse(render(request, 'recommend_album.html',
                               context={'form': form, 'album': album}))
