package backend.algorithms;

import java.util.ArrayList;

public class CampfireGroup {

	private ArrayList<Student> group;
	private int size;
	private int current_size = 0;
	private String name;
	private int groupID;
	
	public CampfireGroup(String name, ArrayList<Student> group, int size, int ID) {
		/* 
		 * Add null checkers
		 * Check for group name to be original
		 * Check for group size is not greater than max size
		 * Check for null, for all values
		 * If group is null set group to empty list.
		 * 
		 **/
		this.group = group;
		this.size = size;
		this.name = name;
		this.groupID = ID;
		this.current_size = group.size();
	}
	
	public ArrayList<Student> getMembers() {
		ArrayList<Student> tmp = new ArrayList<>();
			for(Student s : this.group){
				tmp.add(s);
			}
		return tmp;
	}
		
	public void addMember(Student stu){
		if(this.current_size+1 > size){
			throw new IllegalArgumentException("Group is full, cannot add more members");
		}
		
		if(!this.group.contains(stu)){
			this.group.add(stu);
			this.current_size++;
		}
		
	}
		
	public void removeMember(Student stu){
		if(this.current_size == 0){
			throw new IllegalArgumentException("Group is empty, cannot remove members");
		}
		if(!this.group.contains(stu)){
			throw new IllegalArgumentException("Student does not exist in this group");
		}
		this.group.remove(stu);
		this.current_size--;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getCurrentSize(){
		return this.current_size;
	}
	
	public int getGroupID(){
		return groupID;
	}
	
}








