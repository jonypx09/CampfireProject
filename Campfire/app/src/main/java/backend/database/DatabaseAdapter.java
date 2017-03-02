package com.example.rod.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import algorithms.Course;
import algorithms.Student;

/**
 * Created by Rod on 28/02/2017.
 */

public class DatabaseAdapter {

    private DatabaseHelper dbh;

    /**
     * Create an instance of Database Adapter to access to the database
     * @param context of the Activity we are currently in
     */
    public DatabaseAdapter(Context context){
        dbh = new DatabaseHelper(context);
    }

    /**
     * Adds a student instance to the database.
     * @param student object that will be stored in the student table
     */
    public void addStudent(Student student){
        // Get the database to write in
        SQLiteDatabase db = this.dbh.getWritableDatabase();
        // Fill in the attributes of a student
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.StudentContract.COLUMN_NAME_EMAIL, student.getEmail());
        values.put(DatabaseContract.StudentContract.COLUMN_NAME_FNAME, student.getFname());
        values.put(DatabaseContract.StudentContract.COLUMN_NAME_LNAME, student.getLname());
        values.put(DatabaseContract.StudentContract.COLUMN_NAME_PASS, student.getPass());
        values.put(DatabaseContract.StudentContract.COLUMN_NAME_DESCRIPTION, student.getDescription());
        // Enter this student in the table
        db.insert(DatabaseContract.StudentContract.TABLE_NAME, null, values);
    }

    /**
     * Get a student object stored in the database with the given emal specified, if it exists.
     * @param email identifier of the user that is wishing to be returned
     * @return a Student object with the email specified
     */
    public Student getStudent(String email){
        // Get read only database
        SQLiteDatabase db = this.dbh.getReadableDatabase();
        // Create the where clause of the query
        String selection = DatabaseContract.StudentContract.COLUMN_NAME_EMAIL + " = ?";
        String[] selection_args = {email};

        // Run query to get all students with the email that was given
        Cursor cursor = db.query(
                DatabaseContract.StudentContract.TABLE_NAME,
                null,
                selection,
                selection_args,
                null,
                null,
                null
                );

        // If this user exists create the student instance
        Student stu = null;
        if(cursor.moveToNext()){
            // Get all the students information from the database
            String fname = cursor.getString(cursor.getColumnIndex(DatabaseContract.StudentContract.COLUMN_NAME_FNAME));
            String lname = cursor.getString(cursor.getColumnIndex(DatabaseContract.StudentContract.COLUMN_NAME_LNAME));
            String pass = cursor.getString(cursor.getColumnIndex(DatabaseContract.StudentContract.COLUMN_NAME_PASS));
            String description = cursor.getString(cursor.getColumnIndex(DatabaseContract.StudentContract.COLUMN_NAME_DESCRIPTION));
            stu = new Student(fname, lname, email, pass, null, null);
        }

        return stu;
    }

    /**
     * Add a course object to the database.
     * @param course object that will be stored as a course in the database
     */
    public void addCourse(Course course){
        // Get the database to write in
        SQLiteDatabase db = this.dbh.getWritableDatabase();
        // Fill in the attributes of a student
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CourseContract.COLUMN_NAME_CODE, course.getCourseCode());
        values.put(DatabaseContract.CourseContract.COLUMN_NAME_NAME, course.getName());
        values.put(DatabaseContract.CourseContract.COLUMN_NAME_INSTRUCTOR, course.getInstructor());

        // Enter this student in the table
        db.insert(DatabaseContract.CourseContract.TABLE_NAME, null, values);

        // TODO insert all students as enrolled in this course
    }

    /**
     * Get a Course object from the database by specifying the code identifier if it exists.
     * @param code identifier of the Course needed to be retrieved
     * @return the Course object identified by the code given
     */
    public Course getCourse(String code){
        // Get read only database
        SQLiteDatabase db = this.dbh.getReadableDatabase();
        // Create the where clause of the query
        String selection = DatabaseContract.CourseContract.COLUMN_NAME_CODE + " = ?";
        String[] selection_args = {code};

        // Run query to get all students with the email that was given
        Cursor cursor = db.query(
                DatabaseContract.CourseContract.TABLE_NAME,
                null,
                selection,
                selection_args,
                null,
                null,
                null
        );

        // If this user exists create the student instance
        Course course = null;
        if(cursor.moveToNext()){
            // Get all the students information from the database
            String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.CourseContract.COLUMN_NAME_NAME));
            String instructor = cursor.getString(cursor.getColumnIndex(DatabaseContract.CourseContract.COLUMN_NAME_INSTRUCTOR));
            course = new Course(code, name, instructor);
        }

        // TODO get all the students enrolled in this course and attach to this object

        return course;
    }
}
