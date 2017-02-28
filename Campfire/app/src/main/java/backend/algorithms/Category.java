package backend.algorithms;

/*
 * Each students categories/hobbies and index value; specifically how much skill or 
 * interest they have in it.
 * 
 */
public class Category {

	//Might need more in the future; For more accurate results!
	private String category;
	private int index; 
	
	//Categories that exist
	public Category(String category, int index) {
		this.category = category;
		this.index = index;
	}

	public String getCategory() {
		return category;
	}

	public int getIndex() {
		return index;
	}
	
}
