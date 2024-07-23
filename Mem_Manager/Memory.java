package urn0000;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class Memory {

	private int OSsize = 124;
	private int total_size = 1024;
	private Map<Integer, Boolean> allocatedSegments = new HashMap<>(); // Uses a map to keep track of allocated segments.
	private ArrayList<Hole> listOfHoles = new ArrayList<>(); // Uses an array list to keep track of holes in memory.
	private Hole hole;

	/**
	 * Main memory constructor.
	 * @param size = total memory size. 
	 * @param os_size = size of OS.
	 */
	public Memory(int size, int os_size) {
		if(os_size > size) {
			throw new IllegalArgumentException("OS size cannot be greater than the total size.");
		}
		this.total_size = size;
		this.OSsize = os_size;
		hole = new Hole(OSsize, total_size - 1);
		listOfHoles.add(hole);
	}

	/**
	 * Allocate a process to memory.
	 * @param process = a process to be allocated to memory..
	 * @return Return 1 if successful.
	 */
	public int allocate(Process process) {
		// Iterate through each segment in the process.
		for (Segment segment : process.getSegmentTable().getSegments()) {
			if(segment.getBase() == -1) {
				allocate(process, segment);
			}
		}
		return 1;
	}

	/**
	 * Add a segment of the process to the memory.
	 * @param process = the process that contains the segment.
	 * @param segment = the segment to be allocated.
	 */
	public void allocate(Process process, Segment segment) {
		SegmentTable segTable = process.getSegmentTable();
		// Iterate through each hole in memory to find a hole that is large enough to hold the current segment.
		for (Hole hole : listOfHoles) {
			if (hole.getSize() >= segment.getSize() && hole.getSegment() == null) {
				// If a hole is found, allocate the segment to the hole and update the segment's starting address.
				hole.setSegment(segment);
				Hole newHole = new Hole(hole.getStart() + segment.getSize(), hole.getEnd());
				listOfHoles.add(newHole);
				hole.setRange(hole.getStart(), hole.getStart() + segment.getSize() - 1); // Update the hole to reflect the allocated block.
				segment.setBase(hole.getStart());
				segTable.setValidity(segment.getID(), true);
				allocatedSegments.put(segment.getID(), true); // Update the map of allocated segments.
				return;
			}
		}
		// If no hole was found, return an error.
		System.out.println("Unable to allocate segment " + segment.getID() + " for process " + process.getPID() + ".");
	}
	/**
	 * Remove a segment of the process from memory.
	 * @param process = the process that contains the segment.
	 * @param segment = the segment to be removed from main memory.
	 */
	public int deallocate(Process process, Segment segment) {
		SegmentTable segTable = process.getSegmentTable();
		if (!allocatedSegments.containsKey(segment.getID())) {
			System.out.println("Segment " + segment.getID() + " not found in allocated segments.");
			return -1;
		}

		// Find the hole containing the segment to be deallocated.
		for (Hole hole : listOfHoles) {
			if (hole.getSegment() == segment) {
				// Remove the segment from the hole and update the segment's base address to -1.
				hole.setSegment(null);
				segment.setBase(-1);
				allocatedSegments.remove(segment.getID(), false);
				segTable.setValidity(segment.getID(), false);
				return 1;
			}
		}

		// If the segment was not found in any hole, return an error.
		SegmentTable segmentTable = process.getSegmentTable();
		if (segmentTable.getSegments().contains(segment)) {
			System.out.println("Segments deallocated.");
		}
		else {
			System.out.println("Unable to deallocate segment " + segment.getID() + " for process " + process.getPID() + ".");

		}
		return -1;

	}


	/**
	 * Deallocate memory allocated to the process. Combines adjacent holes next to each other.
	 * @param process = the process to be deallocated.
	 * @return Return 1 if successful. Return -1 otherwise with an error message.
	 */
	public int deallocate(Process process) {
		boolean deallocated = false;

		// Deallocate all segments of the process
		for (Segment segment : process.getSegmentTable().getSegments()) {
			int result = deallocate(process, segment);
			if (result == 1) {
				deallocated = true;
			}
		}

		if (!deallocated) {
			System.out.println("No segments of process " + process.getPID() + " were deallocated.");
			return -1;
		}

		// Combine adjacent holes.
		Collections.sort(listOfHoles, Comparator.comparing(Hole::getStart));
		ListIterator<Hole> iterator = listOfHoles.listIterator();
		Hole prevHole = null;
		while (iterator.hasNext()) {
			Hole currentHole = iterator.next();
			if (prevHole != null && prevHole.getEnd() + 1 == currentHole.getStart()) {
				prevHole.setRange(prevHole.getStart(), currentHole.getEnd());
				iterator.remove();
			} else {
				prevHole = currentHole;
			}
		}

		return 1;
	}

	/**
	 * The process to be be updated.
	 * @param process = the input process to be updated/resized.
	 * @return Return 1 if successful.
	 */
	public int resizeProcess (Process process) {
		// Deallocate all segments of the process.
		int deallocateResult = deallocate(process);
		if (deallocateResult == -1) {
			System.out.println("Unable to resize process " + process.getPID() + ". Deallocation failed.");
			return -1;
		}

		// Allocate new segments for the process.
		SegmentTable segTable = process.getSegmentTable();
		for (Segment segment : segTable.getSegments()) {
			// Check if the size of the segment is zero or negative
			if (segment.getSize() <= 0) {
				// If the size of a segment is zero, then the segment is to be removed from the memory and update the segment table accordingly.
				segTable.getSegments().remove(segment);
				continue;
			}
			// Allocate the segment if its size is greater than zero.
			allocate(process, segment);
			segTable.setValidity(segment.getID(), true);
		}

		System.out.println("Process " + process.getPID() + " has been resized.");
		return 1;
	}

	/**
	 * Check if a segment is valid in the Main Memory.
	 * @param process = the process that contains the segment.
	 * @param segment = the segment to be checked.
	 * @return Return true if the segment is valid, false otherwise.
	 */
	public int isSegmentValid(Process process, Segment segment) {
		// Check if the segment is allocated.
		if (!allocatedSegments.containsKey(segment.getID())) {
			System.out.println("Segment " + segment.getID() + " not found in allocated segments.");
			return 0;
		}

		// Check if the segment is loaded into memory.
		if (segment.getBase() == -1) {
			System.out.println("Segment " + segment.getID() + " is not loaded into main memory.");
			return 0;
		}

		// Check if the segment is within the process's address space.
		if (segment.getBase() + segment.getSize() > total_size) {
			System.out.println("Segment " + segment.getID() + " is not within the process's address space.");
			return 0;
		}

		return 1;
	}

	/**
	 * Set the valid-invalid bit of a segment in the Main Memory.
	 * @param process = the process that contains the segment.
	 * @param segment = the segment to be updated.
	 * @param valid = true to set the bit to valid, false to set it to invalid.
	 */
	public void setValid(Process process, Segment segment, boolean valid) {
		// Check if the segment is valid.
		if (isSegmentValid(process, segment) == 0) {
			return;
		}

		// Set the valid-invalid bit.
		segment.setValid(valid);
	}

	/**
	 * Get the value of the valid-invalid bit of a segment in the Main Memory.
	 * @param process = the process that contains the segment.
	 * @param segment = the segment to be checked.
	 * @return Return true if the segment is valid, false otherwise.
	 */
	public int isValid(Process process, Segment segment) {
		// Check if the segment is valid.
		if (isSegmentValid(process, segment) == 0) {
			return 0;
		}
		else {
			// Get the value of the valid-invalid bit.
			return segment.isValid();
		}
	}

	/**
	 * function to display the state of memory to the console
	 */
	public void memoryState() {
		int total = 0;
		for (Hole hole : listOfHoles) {
			if (hole.getSegment() == null){
				total += hole.getSize();
			}
		}
		System.out.println("Memory State: [OS " + OSsize + "] | [Hole " + total + "]");
	}

}