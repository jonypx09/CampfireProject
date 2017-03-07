package algorithms;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * The interface which all criteria need to implement.
 * Allows comparisons between the criteria of two
 * different students.
 * 
 */


/*
 * @Rod this inteface extends serializable, which I am pretty sure is the same
 * as passing the Serializable interface to all other critirea that implements
 * Comparable.
 * 
 * Fix: All the files that need criteria now need a static final ID for serializable
 * I decided not to add them in quite yet. 
 */

public interface Comparable extends Serializable{
	public double Compare(Comparable other);
	public String getID();
	public Object getItems();
}
