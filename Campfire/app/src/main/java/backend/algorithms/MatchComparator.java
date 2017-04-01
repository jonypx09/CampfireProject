package backend.algorithms;

import java.util.Comparator;
import java.util.Map;

public class MatchComparator implements Comparator<Student>{
	Map<Student, Holder> matchValues;
	
	
	

	public MatchComparator(Map<Student, Holder> matchValues) {
		super();
		this.matchValues = matchValues;
	}




	@Override
	public int compare(Student o1, Student o2) {
		Double value1 = matchValues.get(o1).getValue();
		Double value2 = matchValues.get(o2).getValue();
		return value1.compareTo(value2);
	}

}
