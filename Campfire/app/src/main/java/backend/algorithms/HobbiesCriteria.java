package algorithms;

import java.util.ArrayList;

public class HobbiesCriteria extends ArrayCriteria {
	
	/**
	 * ID was matched
	 */
	private static final long serialVersionUID = 1952358793540268673L;
	public final String ID = "Hobbies";

	public HobbiesCriteria(ArrayList<String> checks) {
		super(checks);
		
	}

	@Override
	public String getID() {
		return this.ID;
	}
	
	@Override
	public double Compare(Comparable other){
		if (other.getID() != this.getID()){
			throw new IllegalArgumentException("Cannot compare two Comparables that are not the same type.");
		}
		return super.Compare(other);
	}

}
