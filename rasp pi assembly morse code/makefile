# This tells that run is not a file to be created but a command that can be run  
.PHONY: run debug

# Create a option to run the code
run: coursework pin clear
	./coursework

debug: coursework
	gdb ./coursework

# By default we are compiling the code
.DEFAULT_GOAL := coursework

# Create the code executable
coursework: coursework.o
	# This links the coursework.o object file with the system libraries
	gcc -g -o coursework coursework.o
	# This makes it so we can run the program by giving it the executable permission
	chmod +x coursework

# Create the object file
coursework.o: coursework.s
	as -g -o coursework.o coursework.s

# Set pin
pin:
	raspi-gpio set 26 pu

clear:
	clear