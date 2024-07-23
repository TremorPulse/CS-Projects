.data // Using the data directive to tell the compiler that this is data
.include "gpiolib.s" // Including the GPIO library file, allowing us to interact with the pins on the breadboard

// Mapping our pins to words so they are easier to work with
.set middle, pin19
.set topLeft, pin13
.set topBar, pin6
.set topRight, pin5
.set dot, pin12
.set bottomRight, pin16
.set bottom, pin20
.set bottomLeft, pin21
.set button, pin26 // In order for the button to work, the pull must be set to UP using the command 'raspi-gpio set 26 pu'

.align 2 // Using the align directive to fix memory misalignment, making sure each section data is stored in an address which is a multiple of 4
message: .asciz "Welcome.\n" // Welcome message as a string (an array of characters)
.align 2
len = .-message // Getting the length of our welcome message

.align 2
printA: .asciz "A\n" // Creating strings to ouput to console for each letter in the alphabet
.align 2
printB: .asciz "B\n"
.align 2
printC: .asciz "C\n"
.align 2
printD: .asciz "D\n"
.align 2
printE: .asciz "E\n"
.align 2
printF: .asciz "F\n"
.align 2
printG: .asciz "G\n"
.align 2
printH: .asciz "H\n"
.align 2
printI: .asciz "I\n"
.align 2
printJ: .asciz "J\n"
.align 2
printK: .asciz "K\n"
.align 2
printL: .asciz "L\n"
.align 2
printM: .asciz "M\n"
.align 2
printN: .asciz "N\n"
.align 2
printO: .asciz "O\n"
.align 2
printP: .asciz "P\n"
.align 2
printQ: .asciz "Q\n"
.align 2
printR: .asciz "R\n"
.align 2
printS: .asciz "S\n"
.align 2
printT: .asciz "T\n"
.align 2
printU: .asciz "U\n"
.align 2
printV: .asciz "V\n"
.align 2
printW: .asciz "W\n"
.align 2
printX: .asciz "X\n"
.align 2
printY: .asciz "Y\n"
.align 2
printZ: .asciz "Z\n"
.align 2
len_letter = 2 		 // The length of each letter string is 2 to include the new line escape character.

.align 2
error: .asciz "\nError.\n" // Error message
.align 2
len_error = .-error // Length of Error message

.text // Using the text directive to delineate where the programming starts

.global main // Using the global directive to let the assembler know which function will become our main method, in this case "main"
.align 4

.ltorg //Using the ltorg literal so the assembler can collect and assemble literals into a literal pool. 
main: 
	// Enabling the pins necessary for the circuit
	ldr r0, =middle		// Load the pin
	bl loadpin		// Enable the pin
 
	ldr r0, =topLeft
	bl loadpin
 
	ldr r0, =topBar
	bl loadpin
 
	ldr r0, =topRight
	bl loadpin
 
	ldr r0, =dot
	bl loadpin
 
	ldr r0, =bottomRight
	bl loadpin
 
	ldr r0, =bottom
	bl loadpin
 
	ldr r0, =bottomLeft
	bl loadpin

	ldr r0, =button
	bl loadpin
	
	// Wait for a second for the pin to enabled
	bl nanoSleep
 
	// Set the direction of each pin
	mov r1, #1 			// Setting the direction to out
 
	ldr r0, =middle		// Load the pin
	bl set_direction	// Set the direction
 
	ldr r0, =topLeft
	bl set_direction
 
	ldr r0, =topBar
	bl set_direction
 
	ldr r0, =topRight
	bl set_direction
 
	ldr r0, =dot
	bl set_direction
 
	ldr r0, =bottomRight
	bl set_direction
 
	ldr r0, =bottom
	bl set_direction
 
	ldr r0, =bottomLeft
	bl set_direction

    mov r1, #0			// Setting the direction to in (just for the button, as its an input)
    
	ldr r0, =button
	bl set_direction
 
	// Wait for a second for the pin direction to be set
	bl nanoSleep
    
	// Turn all segmenets of the display off
	bl none

	// register 4 to 11 are set to 0
	mov r4, #0 
	mov r5, #0
	mov r6, #0
	mov r8, #0
	mov r9, #0
	mov r10, #0
	mov r11, #0

	bl nanoSleep 	// Wait for a second
	bl welcome 		// Display the welcome message
    bl loop 		// Start the loop
 
.ltorg

/*	
	The loop is responsible for polling the button to see if it has been pressed or not, increasing the required counter registers
	and resetting other registers, and brancing to other methods for the solution to work. There is no way to end loop.

	r4 - keeps track of time when the button is pressed
	r5 - keeps track of time when the button is not pressed
*/
loop:
	mov r0, #5			// Register 0 is used to pass the sleep time to 'partSleep' which is a method of the GPIO library
	bl partSleep		// The program will sleep for 5/100 of a second as Register 0 holds 5
	ldr r0, =button		// Loads the pin26
	bl read_value		// Reads the value of said pin
	cmp r0, #0			// Compares the returned value where 0 means the button has been pressed and 1 means the button hasn't been pressed
	addeq r4, r4, #1	// Increments the register 4 counter if the button has been pressed
	moveq r5, #0		// Resets the register 5 counter if the button has been pressed
	addne r5, r5, #1	// Increments the register 5 counter if the button hasn't been pressed
	blne check_button	// Branches to check_button if the button isn't down
	cmp r5, #12			// Compares the register 5 counter to 12
	movgt r5, #0		// Resets register 5 counter if greater than 12
	blge convert		// Branches to convert if greater or equal
	cmp r5, #80			// Compares the register 5 counter to 80
	movge r5, #0		// Resets register 5 counter if greater than 80

	bl loop				// Recursion

/*
	The check_button method is responsible for checking how long a button has been down for and differentiate a dot and dash of morse code
*/
check_button:
	cmp r4, #0				// Compares the counter register 4 to 0
	bxeq lr					// Links back, (exits), to next instruction in loop if register 4 contains 0
	cmp r4, #140			// Compares the counter register 4 to 140
	blge print_error		// If register 4 contains 140 or greater then it will first print an error to console, essentially, if the button has been pressed too long
	blge reset_counters		// Then it will reset the counters
	blge loop				// and finally restart the loop
	cmp r4, #5				// Compares the counter register 4 to 5
	blt add_dot				// If register 4 contains a number less than 5, then the button press was to represent a dot, and thus, runs the method responsible for adding a dot
	bge add_dash			// If register 4 contains a number that is 5 or greater, then the button press was to represent a dash, and thus, runs the method responsilbe for adding a dash
	bl loop					// Once the the check_button method is done, it will restart the loop

/*
	The add_dash and add_dot method is responsible for adding a dash or dot to the respective register that will store the value,
	for later decoding where:

	r6 - keeps track of how many times the button has been pressed

	r8 - first register in the morse code combination
	r9 - second register in the morse code combination
	r10 - third register in the morse code combination
	r11 - fourth register in the morse code combination

	In such a fashion where if the button has only been pressed once, it will store the dot or dash in r8, if the button has been pressed twice, it will store the
	dot or dash in r9, and so forth.

	A dot is represented by the value 0 and a dash is represented by the value 1
*/
add_dash:
	add r6, r6, #1		// Increments the r6 counter by 1 as add_dash is called when the button is pressed and was to signal a dash
	cmp r6, #1			// Compares r6 to 1
	moveq r8, #1		// Stores a dash in r8 if r6 contains 1 (The button has been pressed once)
	cmp r6, #2			// Compares r6 to 2
	moveq r9, #1		// Stores a dash in r9 if r6 contains 2 (The button has been pressed twice)
	cmp r6, #3			// Compares r6 to 3
	moveq r10, #1		// Stores a dash in r10 if r6 contains 3 (The button has been pressed thrice)
	cmp r6, #4			// Compares r6 to 4
	moveq r11, #1		// Stores a dash in r11 if r6 contains 4 (The button has been pressed quarce)
	bleq convert		// Since the max length of a combination for a letter is 4, we have the opportunity to convert a letter
	mov r4, #0			// We reset counter register 4
	bx lr				// We branch back to the next instruction in check_button
	
// add_dot is more or less identical to add_dash, but instead of storing 1, we store 0, to indicate it was a dot to be stored
add_dot:				
	add r6, r6, #1		
	cmp r6, #1
	moveq r8, #0
	cmp r6, #2
	moveq r9, #0
	cmp r6, #3
	moveq r10, #0
	cmp r6, #4
	moveq r11, #0
	bleq convert
	mov r4, #0
	bx lr


/*
	The convert method is responible for deciding which series of branches should be explored depending on the length of the current combination, decided
	by the number of times the button has been pressed, which is stored in register 6
*/
convert:
	cmp r6, #1
	bleq length1	// The combination length is 1, hence, branches length1, etc.
	cmp r6, #2
	bleq length2
	cmp r6, #3
	bleq length3
	cmp r6, #4
	bleq length4
	mov r6, #0		// In case r6 is not a value between 1 - 4 (inclusive), r6 will be reset and convert will restart the loop
	bl loop

/*
	The reset_counters method resets all the counters, (r4, r5, r6), back to their minimum value, (0), and branches back to where it was called from
*/
reset_counters:
	mov r4, #0
	mov r5, #0
	mov r6, #0
	bx lr

// There are only 2 possible outcomes with combination length of 1, E and T
length1:
	cmp r8, #0			// The first register, (r8), is compared to see whether its a dot, (0), or dash (1).
	bleq set_e			// If its a dot, then its E
	blne set_t			// If its a dash, then its T

	bl reset_counters	// Counters are reset
	bl loop				// The loop is restarted

/*
	Subsequent methods, from length2 to dashdashdot4, are done simarly, where one function points to another, analysing the relevant registers form (r8 to r11)
	calling their respective functions.

	Since no letter has the combination "- - - -", if we branch to dashdashdash4, an error is returned.
*/
length2:
	cmp r8, #0
	bleq dot2
	blne dash2

dot2:
	cmp r9, #0
	bleq set_i
	blne set_a
	bl reset_counters
	bl loop

dash2:
	cmp r9, #0
	bleq set_n
	blne set_m
	bl reset_counters
	bl loop

length3:
	cmp r8, #0
	bleq dot3
	blne dash3

dot3:
	cmp r9, #0
	bleq dotdot3
	blne dotdash3

dash3:
	cmp r9, #0
	bleq dashdot3
	blne dashdash3

dotdot3:
	cmp r10, #0
	bleq set_s
	blne set_u
	bl reset_counters
	bl loop

dotdash3:
	cmp r10, #0
	bleq set_r
	blne set_w
	bl reset_counters
	bl loop

dashdot3:
	cmp r10, #0
	bleq set_d
	blne set_k
	bl reset_counters
	bl loop

dashdash3:
	cmp r10, #0
	bleq set_g
	blne set_o
	bl reset_counters
	bl loop

length4:
	cmp r8, #0
	bleq dot4
	blne dash4

dot4:
	cmp r9, #0
	bleq dotdot4
	blne dotdash4


dash4:
	cmp r9, #0
	bleq dashdot4
	blne dashdash4

dotdot4:
	cmp r10, #0
	bleq dotdotdot4
	blne dotdotdash4

dotdash4:
	cmp r10, #0
	bleq dotdashdot4
	blne dotdashdash4

dashdot4:
	cmp r10, #0
	bleq dashdotdot4
	blne dashdotdash4

dashdash4:
	cmp r10, #0
	bleq dashdashdot4
	blne dashdashdash4

dotdotdot4:
	cmp r11, #0
	bleq set_h
	blne set_v
	bl reset_counters
	bl loop

dotdotdash4:
	cmp r11, #0
	bleq set_f
	blne print_error
	bl reset_counters
	bl loop

dotdashdot4:
	cmp r11, #0
	bleq set_l
	blne print_error
	bl reset_counters
	bl loop

dotdashdash4:
	cmp r11, #0
	bleq set_p
	blne set_j
	bl reset_counters
	bl loop


dashdotdot4:
	cmp r11, #0
	bleq set_b
	blne set_x
	bl reset_counters
	bl loop

dashdotdash4:
	cmp r11, #0
	bleq set_c
	blne set_y
	bl reset_counters
	bl loop

dashdashdot4:
	cmp r11, #0
	bleq set_z
	blne set_q
	bl reset_counters
	bl loop

dashdashdash4:
	bl print_error
	bl reset_counters
	bl loop

.align 4
/*
	In the following "set_x" functions, where x is a letter of the alphabet, each method will first ouput its respective letter to the console
	and then display the letter on the seven segment display
*/
set_a:
	push {r0-r7, lr}		// Push registers 0 to 7 and the link register onto the temporary stack as they will be interacted with and to make sure we can link back

	mov r0, #1				// Set the output as the standard out
	ldr r1, =printA			// Load the pointer the to string
	mov r2, #len_letter		// Get the length of the string
	mov r7, #4				// Set the function of the system call to write(4)
	svc #0					// Conduct the system call
 
	ldr r1, =low			// Setting the voltage to low, (which will illuminate the segement due to the way the circuit was constructed)
 
	ldr r0, =topRight		// Load pin
	bl set_value			// Set pin value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r1, =high			// Setting the voltage to high, (which will turn off the segement due to the way the circuit was constructed)
 
	ldr r0, =bottom
	bl set_value
 
	pop {r0-r7, lr}			// Pop registers 0 to 7 and the link register off the temporary stack
 
	bx lr
/*
*/
 
set_b:
	push {r0-r7, lr}

	mov r0, #1			
	ldr r1, =printB			
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r1, =high
 
	pop {r0-r7, lr}
	
	bx lr
 
/* 
*/
 
set_c:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printC			
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topBar
	bl set_value
 
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/* 
*/
 
set_d:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printD			
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_e:
	push {r0-r7, lr}

	mov r0, #1			
	ldr r1, =printE		
	mov r2, #len_letter		
	mov r7, #4			
	svc #0	

	ldr r1, =low
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	bl nanoSleep

	pop {r0-r7, lr}

	bx lr
 
/* 
*/
 
set_f:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printF			
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/* 
*/
 
set_g:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printG			
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_h:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printH			
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_i:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printI		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_j:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printJ		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_k:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printK		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_l:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printL			
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value

	ldr r0, =topBar
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_m:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printM		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_n:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printN		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =topRight
	bl set_value

	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottom
	bl set_value

	ldr r0, =middle
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_o:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printO		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =middle
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_p:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printP			
	mov r2, #len_letter	
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_q:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printQ		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_r:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printR		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottomRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_s:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printS		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_t:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printT		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	bl nanoSleep

	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_u:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printU		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =topBar
	bl set_value

	ldr r0, =middle
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_v:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printV		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topBar
	bl set_value

	ldr r0, =middle
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_w:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printW		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
 
	pop {r0-r7, lr}
	
	bx lr
 
 
/*
*/
 
set_x:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printX	
	mov r2, #len_letter	
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =middle
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
 
	pop {r0-r7, lr}
	
	bx lr
 
 
/*
*/
 
set_y:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printY		
	mov r2, #len_letter		
	mov r7, #4			
	svc #0	

	ldr r1, =low
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
set_z:
	push {r0-r7, lr}

	mov r0, #1				
	ldr r1, =printZ		
	mov r2, #len_letter		
	mov r7, #4				
	svc #0	

	ldr r1, =low
 
	ldr r0, =middle
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r1, =high
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	pop {r0-r7, lr}
	
	bx lr
 
/*
*/
 
.LTORG
 
/*
	The none method sets all segments to off
*/
none:
	push {r0-r7, lr}
 
	ldr r1, =high
 
	ldr r0, =topRight
	bl set_value
 
	ldr r0, =topBar
	bl set_value
 
	ldr r0, =bottomRight
	bl set_value
 
	ldr r0, =bottomLeft
	bl set_value
 
	ldr r0, =topLeft
	bl set_value
 
	ldr r0, =bottom
	bl set_value
 
	ldr r0, =middle
	bl set_value

    ldr r0, =dot
    bl set_value
 
	ldr r1, = high
 
	pop {r0-r7, lr}

	bx lr


	
.align 2

/*
	The welcome method outputs the welcome message to console
*/
welcome:
    mov r0, #1
    ldr r1, =message
    ldr r2, =len
    mov r7, #4
    svc #0

	bx lr

/*
	The print_error method ouputs the error message to console
*/
print_error:
	push {r0-r7, lr}		
	mov r0, #1			
	ldr r1, =error		
	mov r2, #len_error		
	mov r7, #4			
	svc #0				
	pop {r0-r7, lr}			
	bx lr


.end
