package urn0000;

public class Main {

	public static void main(String[] args) {

		// Indicate the start and the end for each task.
		System.out.println("Start Component B.1");

		// Create memory with 1024 bytes total memory and 124 bytes of OS memory.
		Memory M = new Memory(1024, 124);
		// Show how the memory looks before we do anything.
		System.out.println("Initial Main Memory State:");
		M.memoryState();

		// Create process examples for tasks B.1.
		Process p1 = new Process("1, 100, 200, 150");
		Process p2 = new Process("2, 70, 87, 20, 55, 10");
		Process p3 = new Process("3, 10, 260, 40, 10, 70");

		// Now attempt to allocate the first segment from P1 into memory. (Segment numbers start at 0).
		System.out.println("Add Segment [P1, S1] to Main Memory from Process." + p1.toString());
		System.out.println("+ State Before:");
		M.memoryState(); // Show how the memory looks after the operations.
		p1.segmentTable(); // Show how the segment table looks after the operations.

		M.allocate(p1, p1.getSegment(1)); // Add Segment S1 of P1 in main memory.

		System.out.println("- State After:");
		M.memoryState(); // Show how the memory looks after the operations.
		p1.segmentTable(); // Show how the segment table looks after the operations.


		// Next, allocate all segments from P1 into memory.
		// (This would skip over the already allocated [P1, S1].
		// Note that P1.S1 is already in the memory. Each segment can only be added once.
		System.out.println("Add all segments of P1 to Main Memory.");
		M.allocate(p1); // Or some variant of this form to all Segments of P1 in main memory.
		p1.segmentTable(); // Show how the segment table looks after the operations.

		M.memoryState(); // Show how the memory looks after the operations.
		// you can add P2 and P3 in here 

		System.out.println("Deallocate segment [P1 S1] from Main Memory.");
		M.deallocate(p1, p1.getSegment(1));
		p1.segmentTable();
		M.memoryState();

		System.out.println("Deallocate P1 from Main Memory.");
		M.deallocate(p1);
		p1.segmentTable();
		M.memoryState();


		System.out.println("Resize " + p3.toString());
		M.allocate(p1);
		p1.resize("30, 40, 10");
		M.resizeProcess(p1);
		p1.segmentTable();
		M.memoryState();


		System.out.println("End Component B.1");


		System.out.println("Start Component B.2.2");
		// create a new process for tasks B.2.2
		Process p5 = new Process("5, [20; rwx], [70; r--], [50; -w-]");

		//TODO: more code to be added
		// ..... 
		System.out.println("End Component B.2.2");

		// TODO: continue to complete the rest of the code
	}
}
