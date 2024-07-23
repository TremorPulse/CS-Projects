# Assuming this Dockerfile is also inside the Zenify directory
# Use an official Python runtime as a parent image
FROM python:3.9-slim

# Set environment variables to ensure Python runs in unbuffered mode
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Set the working directory in the Docker container
WORKDIR /app

# Copy the Python dependencies file to the container
COPY requirements.txt /app/
# Install Python dependencies
RUN pip install --upgrade pip && pip install -r requirements.txt

# Copy the entire Django project into the container
COPY . /app/

# Command to run the Django application using Gunicorn
CMD ["gunicorn", "--bind", "0.0.0.0:8000", "Zenify.wsgi:application"]
