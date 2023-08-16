package urn0000;
/**
* This class serves the purpose of storing the size of the hole in memory.
*/
public class Hole {

	// base and limit values
	private int start;
	private int end;
	private Segment segment = null;
	

	/**
	 * Default constructor.
	 * Sets the first hole the size of the maximum space, and is free.
	 */
	Hole(int start, int end){
		segment = null;
	    this.start = start;
	    this.end = end;
	}

	public int getStart(){
	    return this.start;
	}
	 
	public int getEnd(){
	    return this.end;
	}
	
	/**
	 * This method returns the size of the hole.
	 * @return hole size
	 */
	int getSize(){
	    return (end - start) + 1;
	}

	/**
	 * This method sets the range of the hole for the block
	 * @param start start byte of the hole
	 * @param end end byte of the hole
	 */
	public void setRange(int start, int end){
	    this.start = start;
	    this.end = end;
	}
	
	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

}