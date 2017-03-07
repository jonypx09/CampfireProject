package backend.algorithms;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class HashMapCriteria<U, T> implements Comparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<T, ArrayList<U>> values = new HashMap<T, ArrayList<U>>();
	
	public HashMapCriteria(HashMap<T, ArrayList<U>> values) {
		this.values = values;
	}
	
	public double Compare(Comparable other){
		double score = 0;
		for (T key: this.getMap().keySet()){
			if (((HashMapCriteria) other).getMap().containsKey(key)){
				for(U time: this.getMap().get(key)){
					if (((ArrayList<U>) ((HashMapCriteria) other).getMap().get(key)).contains(time)){
						score++;
					}
				}
			}
		}
		return score;
	}
	
	public HashMap<T, ArrayList<U>> getMap(){
		return values;
	}
	
	@Override
	public HashMap<T, ArrayList<U>> getItems() {
		return values;
	}	
}