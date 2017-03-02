package backend.algorithms;

import java.util.ArrayList;

public abstract class ArrayCriteria implements Comparable{
	

	private ArrayList<String> values;
	
	public ArrayCriteria(ArrayList<String> checks){
		this.values = checks;
	}
	
	@Override
	public double Compare(Comparable other){
		double score = 0;
		for (String str: this.getValues()){
			if (((ArrayCriteria) other).getValues().contains(str)){
				score++;
			}
		}
		return score;
		
	}

	public ArrayList<String> getValues() {
		return values;
	}
	
}
