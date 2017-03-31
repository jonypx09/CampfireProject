package backend.algorithms;

import java.util.ArrayList;
import java.util.Arrays;

public class AssignmentGroup {

	private ArrayList<Student> studentsInGroup;
	private int gID;
	
	public AssignmentGroup(ArrayList<Student> students, int ID){
		this.studentsInGroup = students;
		this.gID = ID;
	}
	
	public AssignmentGroup(Student student, int ID){
		this(new ArrayList<Student>(Arrays.asList(student)), ID);
	}

	public ArrayList<Student> getStudentsInGroup() {
		ArrayList<Student> tmp = new ArrayList<>();
		for(Student s : this.studentsInGroup){
			tmp.add(s);
		}
		return tmp;
	}
	
	public void addStudent(Student s){
		this.studentsInGroup.add(s);
	}
	
	public int getgID() {
		return gID;
	}
	
	public int mergeGroups(AssignmentGroup g){
		if (g.getStudentsInGroup().size() >= this.getStudentsInGroup().size()){
			union(g, this);
			return g.getgID();
		}
		else {
			union(this, g);
			return this.getgID();
		}
	}

	/*
	 * Combines students from g2 into g1
	 */
	private void union(AssignmentGroup g1, AssignmentGroup g2){
		for (Student s : g2.getStudentsInGroup()){
			g1.addStudent(s);
		}
	}
}
