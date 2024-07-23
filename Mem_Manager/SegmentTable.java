package urn0000;

import java.util.ArrayList;
 
/**
 * This class defines the Segment Table of a Process.
 * TODO: this class is incomplete
 */
 
public class SegmentTable {
  private ArrayList<Segment> segments;
 
  /*
   * Constructor of SegmentTable
   */
  public SegmentTable(ArrayList<Segment> seg) {
    this.segments = seg;
 
  }
  
  public void setValidity(int segmentID, boolean isValid) { // new method
		for (Segment segment : segments) {
			if (segment.getID() == segmentID) {
				segment.setValid(isValid);
				return;
			}
		}
		System.out.println("Segment " + segmentID + " not found in segment table.");
	}
 
  public Segment getSegment(int id) {
    return segments.get(id);
  }
 
 
  public ArrayList<Segment> getSegments() {
    return segments;
  }
 
  /*
   * display the details of all of the segments in the table
   */
  public String toString() {
    String output = "";
    for (Segment segment : segments) {
      output += segment.toString() + "\n";
    }
    return output;
  }
 
}