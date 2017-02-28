package backend.algorithms;

/*
 * The interface which all criteria need to implement.
 * Allows comparisons between the criteria of two
 * different students.
 * 
 */
public interface Comparable {
	public double Compare(Comparable other);
	public String getID();
}
