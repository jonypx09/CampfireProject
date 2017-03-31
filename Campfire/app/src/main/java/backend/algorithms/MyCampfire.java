package backend.algorithms;

import java.util.ArrayList;

public class MyCampfire {
	
	/*Basic way of adding people to your campfire group*/
	
	private ArrayList<Student> group;
	private Student host;
	private String ID;
	
	public MyCampfire(Student stu, String specialID) {
		host = stu;
		ID = specialID;
		group = new ArrayList<Student>();
	}

	public ArrayList<Student> getMembers() {
		ArrayList<Student> tmp = new ArrayList<>();
			for(Student s : this.group){
				tmp.add(s);
			}
		return tmp;
	}
	
	public void addMember(Student stu){
		this.group.add(stu);
	}
	
	public void removeMember(Student stu){
		this.group.remove(stu);
	}

	public Student getHost() {
		return host;
	}

	public String getID() {
		return ID;
	}
	
}

