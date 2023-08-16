package urn0000;

public class Segment {
	private int id; // the id of the segment 
	private int size; // the size of the segment	
	private int base = -1; // base of the segment
	private boolean isValid; // new field
	/**
	 * default constructor of a Segment
	 */
	public Segment() {
		//TODO: to be completed

	}

	/**
	 * the constructor of Segment
	 * @param segmentID the id of the segment
	 * @param size the size of the segment
	 */
	public Segment(int segmentID, int size) {
		super();
		id = segmentID;
		this.size = size;
		this.isValid = false; 

	}
	
	public int getID() {
		return id;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getSize() {
		return size;
	}
	
	public int setSize(int size) {
		return this.size = size;
	}
	
	public int isValid() {
		if(isValid == true) {
			return 1;
		}
		else  {
			return 0;
		}
	}
	
	public void setValid(boolean isValid) { // new setter
		this.isValid = isValid;
	}

	public String toString() {
		if (base == -1) {
			String output = " " + (id - 1) + "  |      | " + size + "  |  " + isValid();
			return output;
		} else {
			String output = " " + (id - 1) + "  | " + base + "  | " + size + "  |  " + isValid() ;
			return output;
		}
	}

}
