package backend.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import backend.algorithms.Student;
import backend.algorithms.Category;
import backend.algorithms.Comparable;
import backend.algorithms.Course;

public class TestMain {

	public static void main(String[] args) throws ClassNotFoundException {
		
		SQLiteController sqlcon = new SQLiteController();
		
		try {
			setup(sqlcon);
			
			System.out.println(SQLiteQueryAdapter.courseExists(sqlcon, "CSC209"));
			SQLiteQueryAdapter.enrolStudentInCourse(sqlcon, "joe@mail.com", "CSC209");
			Course course = SQLiteQueryAdapter.getCourse(sqlcon, "CSC209");
			ArrayList<Student> s = course.getStudents();
			
			for (int i = 0; i < s.size(); i++){
				System.out.println(s.get(i).getEmail());
			}
			
			sqlcon.closeConnectionToDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void setup(SQLiteController sqlcon){
		try {
			SQLiteQueryAdapter.deleteAllCourses(sqlcon);
			SQLiteQueryAdapter.deleteAllStudents(sqlcon);
			
			Student stu1 = new Student("Joe", "Lulu", "joe@mail.com", "pass1", null, null);
			Student stu2 = new Student("Mark", "Savage", "mark@mail.com", "pass2", null, null);
			Student stu3 = new Student("Lucy", "Vander", "lucy@mail.com", "pass3", null, null);
			
			SQLiteQueryAdapter.addStudent(sqlcon, stu1);
			SQLiteQueryAdapter.addStudent(sqlcon, stu2);
			SQLiteQueryAdapter.addStudent(sqlcon, stu3);
			
			Course course1 = new Course("CSC108", "Intro to Programming", "Paul Gries");
			Course course2 = new Course("CSC209", "Object Oriented Programming", "Diane Horton");
			SQLiteQueryAdapter.addCourse(sqlcon, course1);
			SQLiteQueryAdapter.addCourse(sqlcon, course2);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
