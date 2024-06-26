<<<<<<< HEAD
# Generated by Django 4.2.6 on 2024-05-14 07:25
=======
# Generated by Django 4.2.6 on 2024-05-14 08:52
>>>>>>> 99a36f899a430cba4556241f788f2c7f4c089439

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Content',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=200)),
                ('description', models.TextField()),
                ('audio_link', models.URLField(blank=True, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='ContentVideo',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=200)),
                ('video_link', models.URLField(blank=True, null=True)),
                ('runtime', models.DecimalField(decimal_places=2, max_digits=5)),
                ('content', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='videos', to='content.content')),
            ],
        ),
    ]
