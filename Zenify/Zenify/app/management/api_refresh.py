# Run this code from a Django shell or a custom management command
from datetime import timedelta
from celery import current_app
from django_celery_beat.models import PeriodicTask, IntervalSchedule

# Set up the scheduler for every 10 minutes
schedule, created = IntervalSchedule.objects.get_or_create(
    every=1,
    period=IntervalSchedule.MINUTES,
)

# Ensure your task is correctly named with the full task path
PeriodicTask.objects.create(
    interval=schedule,
    name='My periodic task every 1 minutes',
    task='myapp.tasks.my_periodic_task'
)
