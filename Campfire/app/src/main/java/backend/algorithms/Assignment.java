package backend.algorithms;

import java.util.ArrayList;
import java.util.Random;

public class Assignment {
	
	private int assignment_id;
	private String name;
	private Course course;
	private int maxGroupSize;
	private ArrayList<AssignmentGroup> groups = new ArrayList<>();
	
	public Assignment(int assignment_id, String name, Course c, int size){
		if (size < 1){
			throw new IllegalArgumentException();
		}
		this.assignment_id = assignment_id;
		this.name = name;
		maxGroupSize = size;
		course = c;
	}

	public int getAssignment_id(){
		return this.assignment_id;
	}

	public String getName(){
		return this.name;
	}

	public Course getCourse() {
		return course;
	}

	public int getMaxGroupSize() {
		return maxGroupSize;
	}

	public ArrayList<AssignmentGroup> getGroups() {
		return groups;
	}
	
	public AssignmentGroup combineGroups(AssignmentGroup g1, AssignmentGroup g2){
		if (this.getGroups().contains(g1) == false || this.getGroups().contains(g2) == false){
			throw new IllegalArgumentException();
		}
		if (g1.getStudentsInGroup().size() + g2.getStudentsInGroup().size() > this.getMaxGroupSize()){
			return null;
		}
		int id = g1.mergeGroups(g2);
		if (id == g1.getgID()){
			this.getGroups().remove(g2);
			return g1;
		}
		else {
			this.getGroups().remove(g1);
			return g2;
		}
	}
	
	/*
	 * Creates a one person group for every
	 * student in the course on this assignment
	 * 
	 */
	public void populate(){
		Random r = new Random();
		int tmpID;
		ArrayList<Integer> gIDs = new ArrayList<>();
		for (AssignmentGroup g : this.getGroups()){
			gIDs.add(g.getgID());
		}
		
		for (Student s : course.getStudents()){
			tmpID = r.nextInt(1000);
			
			//Find a unique groupID
			while (gIDs.contains(tmpID)){
				tmpID = r.nextInt(1000);
			}
			
			AssignmentGroup group = new AssignmentGroup(s, tmpID);
			gIDs.add(tmpID);
			this.groups.add(group);
		}
		
	}

}
