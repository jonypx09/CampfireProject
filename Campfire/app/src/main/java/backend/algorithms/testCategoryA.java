package backend.algorithms;

public class testCategoryA implements Comparable{

	private int value;
	private static String ID = "A";
	
	public testCategoryA(int val){
		this.value = val;
	}
	
	public int getValue(){
		return value;
	}
	
	@Override
	//Comparing Method A - This will need to be more accurate
	public double Compare(Comparable other) {
		int checkagainst = ((testCategoryA) other).getValue();
		return Math.abs(this.getValue() - checkagainst);
	}
	
	@Override
	public String getID(){
		return ID;
	}
}