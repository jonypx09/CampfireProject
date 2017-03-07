package backend.algorithms;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleCriteria extends HashMapCriteria{
	
	/**
	 * ID was matched
	 */
	private static final long serialVersionUID = 1952358793540268673L;
	private final String ID = "Time Schedule";
	
	public ScheduleCriteria(HashMap<String, ArrayList<String>> schedule) {
		super(schedule);
	}
		
	@Override
	public String getID(){
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
