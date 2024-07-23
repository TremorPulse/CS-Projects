package urn0000;

import java.util.ArrayList;

public class Process {

	private int pid = 0; // the id of the process
	private SegmentTable segmentTable;

	//Constructor
	public Process (String processString) {
		pid ++;
		ArrayList<Segment> segmentList = new ArrayList<Segment>();

		//Parser Examples
		Parser P = new Parser();
		ArrayList<String>[] list = P.parseInputString(processString);


		for(int id = 1; id < list.length; id++) {
			int segmentSize = Integer.valueOf(list[id].get(0));
			Segment segment = new Segment(id, segmentSize);
			segmentList.add(segment);
		}

		segmentTable = new SegmentTable(segmentList);

	}

	public void resize(String segments) {
		// Parse the input string to get the new segment sizes
		ArrayList<Integer> newSizes = new ArrayList<Integer>();
		String[] sizesStr = segments.split(",");
		for (String sizeStr : sizesStr) {
			int size = Integer.parseInt(sizeStr.trim());
			newSizes.add(size);
		}

		// Check that the number of new segment sizes matches the current number of segments.
			if (newSizes.size() != segmentTable.getSegments().size()) {
				System.out.println("Error: The number of new segment sizes does not match the number of segments.");
			}

		// Update the segment sizes in the segment table.
		for (int i = 0; i < newSizes.size(); i++) {
			Segment segment = segmentTable.getSegment(i);
			int newSize = newSizes.get(i);
			if (newSize < segmentTable.getSegments().size()) {
				System.out.println("Error: New segment size is smaller than the current size.");
			}
			segment.setSize(newSize);

		}
	}

	public Segment getSegment(int id) {
		return segmentTable.getSegment(id - 1);

	}

	public SegmentTable getSegmentTable() {
		return segmentTable;
	}

	public int getPID() {
		return pid;
	}

	public void segmentTable() {
		System.out.println("P" + pid + " Segment Table\nSID | base | limit | valid-invalid\n" + segmentTable.toString());
	}

	public String toString() {
		return "Process 1 and its segments details.";
	}

	public void setSegmentTable(SegmentTable newSegmentTable) {
		this.segmentTable = newSegmentTable;

	}
}
