package backend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.Student;

/**
 * Class to manage what queries and data are being passed to and from the database.
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
        // Serialize the students comparables and store it as a string in the database
        try {
            String comparable = Serializer.serialize(student.getCriteria());
            values.put(DatabaseContract.StudentContract.COLUMN_NAME_COMPARABLE, comparable);
        } catch (Exception e){
            e.printStackTrace();
        }
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
            // Deserialize the criteria text into a critieria arraylist and add to student object
            String comparable = cursor.getString(cursor.getColumnIndex(DatabaseContract.StudentContract.COLUMN_NAME_COMPARABLE));
            try {
                ArrayList<Comparable> comparableList = (ArrayList) Serializer.deserialize(comparable);
                stu = new Student(fname, lname, email, pass, comparableList);
                stu.setDescription(description);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        cursor.close();

        return stu;
    }

    /**
     * Get all the students that are contained in the database.
     * @return an ArrayList of all the students in the database
     */
    public ArrayList<Student> getAllStudents(){
        // Get read only database
        SQLiteDatabase db = this.dbh.getReadableDatabase();
        // Run query to get all students with the email that was given
        Cursor cursor = db.query(
                DatabaseContract.StudentContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<Student> stuList = new ArrayList<Student>();
        // Create all the student objects from the database and them to the array list
        while(cursor.moveToNext()){
            Student stu = getStudent(cursor.getString(cursor.getColumnIndex(DatabaseContract.TakingContract.COLUMN_NAME_EMAIL)));
            stuList.add(stu);
        }
        return stuList;
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

        // Insert all the students in this course into the taking table
        for (Student stu : course.getStudents()){
            addToTaking(course.getCourseCode(), stu.getEmail());
        }
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

        // If this course exists get the course instance
        Course course = null;
        if(cursor.moveToNext()){
            // Get all the students information from the database
            String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.CourseContract.COLUMN_NAME_NAME));
            String instructor = cursor.getString(cursor.getColumnIndex(DatabaseContract.CourseContract.COLUMN_NAME_INSTRUCTOR));
            course = new Course(code, name, instructor);

            // Add all student objects in this course
            ArrayList<Student> stuList = getStudentsInCourse(code);
            for (Student stu : stuList){
                course.addStudent(stu);
            }
        }

        return course;
    }

    /**
     * Get all the courses stored in the database.
     * @return an ArrayList of all the courses
     */
    public ArrayList<Course> getAllCourses(){
        // Get read only database
        SQLiteDatabase db = this.dbh.getReadableDatabase();
        // Run query to get all students with the email that was given
        Cursor cursor = db.query(
                DatabaseContract.CourseContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Pull all the course object in to an arraylist
        ArrayList<Course> courseList = new ArrayList<Course>();
        while(cursor.moveToNext()){
            Course course = getCourse(cursor.getString(cursor.getColumnIndex(DatabaseContract.CourseContract.COLUMN_NAME_CODE)));
            courseList.add(course);
        }
        return courseList;
    }

    /**
     * Add student email as taking course with identifier as code.
     * @param code of the course student is in
     * @param email of the student that is taking the course
     */
    public void addToTaking(String code, String email){
        // Get the database to write in
        SQLiteDatabase db = this.dbh.getWritableDatabase();
        // Fill in the attributes of the taking table
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TakingContract.COLUMN_NAME_CODE, code);
        values.put(DatabaseContract.TakingContract.COLUMN_NAME_EMAIL, email);
        // Enter this entry to the table
        db.insert(DatabaseContract.TakingContract.TABLE_NAME, null, values);
    }

    /**
     * Return all the
     * @param code
     * @return
     */
    public ArrayList<Student> getStudentsInCourse(String code){
        // Get read only database
        SQLiteDatabase db = this.dbh.getReadableDatabase();
        // Create the where clause of the query
        String selection = DatabaseContract.TakingContract.COLUMN_NAME_CODE + " = ?";
        String[] selection_args = {code};
        // Run query to get entries with the code given
        Cursor cursor = db.query(
                DatabaseContract.TakingContract.TABLE_NAME,
                null,
                selection,
                selection_args,
                null,
                null,
                null
        );
        ArrayList<Student> stuList = new ArrayList<Student>();
        // Create all the student objects from the database and them to the array list
        while(cursor.moveToNext()){
            Student stu = getStudent(cursor.getString(cursor.getColumnIndex(DatabaseContract.TakingContract.COLUMN_NAME_EMAIL)));
            stuList.add(stu);
        }
        return stuList;
    }

    /**
     * Return an arraylist of course codes that a given student is enrolled in.
     * @param email the email of the student that we want to find what courses they are in
     * @return an Arraylist of all the codes of the courses
     */
    public ArrayList<String> enrolledIn(String email){
        // Get read only database
        SQLiteDatabase db = this.dbh.getReadableDatabase();
        // Create the where clause of the query
        String selection = DatabaseContract.TakingContract.COLUMN_NAME_EMAIL + " = ?";
        String[] selection_args = {email};
        // Run query to get entries with the code given
        Cursor cursor = db.query(
                DatabaseContract.TakingContract.TABLE_NAME,
                null,
                selection,
                selection_args,
                null,
                null,
                null
        );
        ArrayList<String> codeList = new ArrayList<String>();
        // Create all the student objects from the database and them to the array list
        while(cursor.moveToNext()){
            String code = cursor.getString(cursor.getColumnIndex(DatabaseContract.TakingContract.COLUMN_NAME_CODE));
            codeList.add(code);
        }
        return codeList;
    }

    /**
     * Clear the entire database of all its data.
     */
    public void wipe(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        dbh.onUpgrade(db, dbh.DATABASE_VERSION, dbh.DATABASE_VERSION + 1);
    }
}
