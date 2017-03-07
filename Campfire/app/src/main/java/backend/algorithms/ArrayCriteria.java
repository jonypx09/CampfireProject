package backend.algorithms;

import java.util.ArrayList;

public abstract class ArrayCriteria<T> implements Comparable{

	/**
	 * ID was matched
	 */
	private static final long serialVersionUID = 1952358793540268673L;
	private ArrayList<T> values;
	
	public ArrayCriteria(ArrayList<T> checks){
		this.values = checks;
	}

	@Override
	public double Compare(Comparable other){
		double score = 0;
		for (T val: this.getItems()){
			if (((ArrayCriteria<T>) other).getItems().contains(val)){
				score++;
			}
		}
		return score;
		
	}

	@Override
	public ArrayList<T> getItems() {
		return values;
	}
	
}
