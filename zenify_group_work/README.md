## Running instructions for new code
=====================================================
After implementing google fit API, we have some new technologies that you must install and run before 
being able to use the website with all its functionality. 

- Pip install all the python packages in requirements.txt (via the project directory)
- Npm install all the javascript packages in Packages in package.json (via the frontend directory)
- Open up 5 terminals 

- In terminal 1, run the following instructions(use UBUNTU terminal if you are not on linux, see below for more information):
    sudo service redis-server start  

- In terminal 2, run the following instructions:
    python manage.py makemigrations 
    python manage.py migrate
    python manage.py migrate django_celery_beat
    python manage.py runserver 8000

- In terminal 3, run the following instructions:
    celery -A Zenify worker -l info 

- In terminal 4, run the following instructions:
    celery -A Zenify beat -l info

-In terminal 5, run the following instructions:
    npm install react-router-dom
    npm install react-scripts --save
    npm start

==============================================================================================================================
## Extra Information for installation 

When running redis, I was unable to run it on a normal windows command prompt, An easy work around on windows was using WSL
(windows sub system for Linux.) this will allow you to install and run the redis server in the background. Which would allow 
you to run the code to see the live updates of steps. 

To install redis in the Ubuntu terminal run: 
    sudo apt-get update
    sudo apt install redis-server

If you do not run the redis sever or the celery instructions, the website
will work but the steps will not update frequently.

Please ask me if you have any troubles with any of these steps  - Anis

