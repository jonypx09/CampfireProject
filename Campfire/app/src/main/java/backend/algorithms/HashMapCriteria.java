package backend.algorithms;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class HashMapCriteria implements Comparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, ArrayList<String>> schedule = new HashMap<String, ArrayList<String>>();
	
	public HashMapCriteria(HashMap<String, ArrayList<String>> schedule) {
		this.schedule = schedule;
	}
	
	public double Compare(Comparable other){
		double score = 0;
		for (String key: this.getMap().keySet()){
			if (((HashMapCriteria) other).getMap().containsKey(key)){
				for(String time: this.getMap().get(key)){
					if (((HashMapCriteria) other).getMap().get(key).contains(time)){
						score++;
					}
				}
			}
		}
		return score;
	}
	
	public HashMap<String, ArrayList<String>> getMap(){
		return schedule;
	}
	
}