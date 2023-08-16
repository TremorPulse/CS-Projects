package urn0000;

import java.util.ArrayList;

//Parse the process input string into components

public class Parser {

	/**
	 * Parser Constructor
	 * @param none
	 */
	public Parser() {
		
	}

	/**
	 * Method to extract the Process id and a list of Segment size with some parameters 
	 * @param process_string of the form pid, sid1_size, sid2_size ...
	 *        example: 1, 100, 200
	 * @param process_string of the form pid, [sid1_size; size1_arg1; size1_arg2, ...], ...
	 *        example: 1, [100; 0], [200; 1], [300; 0; 2; 3]
	 * @return comp and ArrayList of components from process_string
	 *        example comp[0] = pid and comp[1] = {100, 0}       
	 */
	public ArrayList<String>[] parseInputString (String process_string){
		
		int size, index;
		ArrayList<String>[] comp;
		
		//number of components to look at, first being the pid
		size = process_string.split(",").length;
		
		//process id, followed by segments and their arguments
		comp = new ArrayList[size];
		
		//init all
		for (int i =0; i<size; i++){
			comp[i] = new ArrayList<String>();
		}
		
	
		//split into each component of process_string
		index = 0;
		for (String part:process_string.split(",")) {
			//remove space
			part = part.replace(" ", "");	
			
			//see if options are declared by looking at [ and ]
			if (part.contains("[") && part.contains("]")){
				part = part.replace(" ", "").replace("[", "").replace("]", "");
				
				//split into options: size, read-write bit, shared list
				for (String split:part.split(";")) {
					comp[index].add(split);
				}
			} else {
				//no options presented
				comp[index].add(part);
			}
			index++;
		}
		
		//TODO: remove the print
	/*	for (int i =0; i<size; i++){
			System.out.println(comp[i].toString());
		}*/
		
		//return all the parts separated by , and ; from process_string
		return comp;
	}
	
}

