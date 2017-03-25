package backend.database;

import android.location.Criteria;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import backend.algorithms.Assignment;
import backend.algorithms.AssignmentGroup;
import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.Student;

/**
 * Class to provide useful functions for creating threads to query the database.
 */
public class DbAdapter {
    // We dont anyone creating an instance of this class
    private DbAdapter (){}

    /* ---------- PRIVATE HELPER FUNCTIONS ---------- */

    /**
     * Deserialized string to a Comparable ArrayList.
     */
    private static ArrayList<Comparable> comparableDeserializer(String serialized_comparable){
        try {
            ArrayList<Comparable> comparables;
            if (serialized_comparable == null) {
                return new ArrayList<Comparable>();
            } else {
                comparables = (ArrayList<Comparable>) Serializer.deserialize(serialized_comparable);
            }
            return comparables;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<Comparable>();
    }

    /**
     * Get all the students in a specified course.
     * @param code course code of the course
     * @return an ArrayList of Student in that course
     */
    private static ArrayList<Student> getAllStudentsInCourse(String code){
        ArrayList<Student> stu_list = new ArrayList<Student>();
        try {
            List<String> args = new ArrayList<String>();
            args.add(code);
            ResultDatabaseThread thread = new ResultDatabaseThread(
                    "SELECT * FROM taking WHERE code = ?", args
            );
            thread.execute();
            ResultSet rs = thread.get();
            while(rs.next()){
                Student stu = getStudent(rs.getString("email"));
                stu_list.add(stu);
            }
            return stu_list;
        } catch (Exception e){
            e.printStackTrace();
        }
        return stu_list;
    }

    /**
     * Return the max chat ID.
     * @return an int, the max chat ID
     */
    private static int getMaxChatID(){
        try {
            ResultDatabaseThread thread = new ResultDatabaseThread(
                    "SELECT MAX(chat_id) AS max FROM chats", null
            );
            thread.execute();
            ResultSet rs = thread.get();
            if (rs.next()){
                return rs.getInt("max");
            } else {
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /* ---------- STUDENT QUERIES ---------- */

    /**
     * Add a student into the database.
     * @param student object to be added to the database
     */
    public static void addStudent(Student student){
        // Check if the student already exists
        if (getStudent(student.getEmail()) != null){
            throw new IllegalArgumentException();
        }

        ArrayList<String> args = new ArrayList<String>();
        args.add(student.getEmail());
        args.add(student.getFname());
        args.add(student.getLname());
        args.add(student.getPass());
        args.add(student.getDescription());
        args.add(Serializer.serialize(student.getCriteria()));
        UpdateDatabaseThread udb = new UpdateDatabaseThread(
                "INSERT INTO student VALUES (?, ?, ?, ?, ?, ?)", args
        );
        udb.execute();
    }

    /**
     * Get a student instance from the database.
     * @param email of the student you want to get from the database
     * @return Student object of that student
     */
    public static Student getStudent(String email){
        try {
            ArrayList<String> args = new ArrayList<String>();
            args.add(email);
            ResultDatabaseThread thread = new ResultDatabaseThread("SELECT * FROM student WHERE email = ?", args);
            thread.execute();

            ResultSet rs = thread.get();
            Student student = null;
            if (rs.next()){

                student = new Student(
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("email"),
                        rs.getString("pass"),
                        comparableDeserializer(rs.getString("comparable"))
                );
            }
            return student;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all the student object in the database.
     * @return a list of all the students in the database
     */
    public static List<Student> getAllStudents(){
        ArrayList<Student> student_list = new ArrayList<Student>();
        try {
            ResultDatabaseThread thread = new ResultDatabaseThread("SELECT * FROM student", null);
            thread.execute();

            ResultSet rs = thread.get();
            while (rs.next()){

                Student student = new Student(
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("email"),
                        rs.getString("pass"),
                        comparableDeserializer(rs.getString("comparable"))
                );
                student.setDescription(rs.getString("description"));

                student_list.add(student);
            }
            return student_list;
        } catch (Exception e){
            e.printStackTrace();
        }
        return student_list;
    }

    /**
     * Delete a student in the database.
     * @param email identifier of the student that is going to be deleted
     */
    public static void deleteStudent(String email){
        // Check if no student exists
        if (getStudent(email) == null){
            throw new IllegalArgumentException();
        }

        List<String> args = new ArrayList<>();
        args.add(email);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "DELETE FROM student WHERE email = ?", args
        );
        thread.execute();
    }

    /**
     * Update an existing students information in the database.
     * @param student object that will update the database
     */
    public static void updateStudent(Student student){
        // Make sure that the student exists
        if (getStudent(student.getEmail()) == null){
            throw new IllegalArgumentException();
        }

        List<String> args = new ArrayList<>();
        args.add(student.getFname());
        args.add(student.getLname());
        args.add(student.getPass());
        args.add(student.getDescription());
        args.add(Serializer.serialize(student.getCriteria()));
        args.add(student.getEmail());
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "UPDATE student SET fname = ?, lname = ?, pass = ?, description = ?, comparable = ? where email= ?;",
                args
        );
        thread.execute();
    }

    /* ---------- COURSE QUERIES ---------- */

    /**
     * Add a course in to the database.
     * @param course instance to be saved in the database
     */
    public static void addCourse(Course course){
        // Check if the course already exists
        if (getCourse(course.getCourseCode()) != null){
            throw new IllegalArgumentException();
        }

        List<String> args = new ArrayList<String>();
        args.add(course.getCourseCode());
        args.add(course.getName());
        args.add(course.getInstructor());
        UpdateDatabaseThread thread  = new UpdateDatabaseThread(
                "INSERT INTO course VALUES (?,?,?)", args
        );
        thread.execute();

        // Add all this courses students to be enrolled in this course
        for (Student stu : course.getStudents()){
            enrolStudentInCourse(stu.getEmail(), course.getCourseCode());
        }
    }

    /**
     * Get a course object from the database.
     * @param code identifier of the course wanted
     * @return Course object of the course that is wanted
     */
    public static Course getCourse(String code){
        try {
            List<String> args = new ArrayList<String>();
            args.add(code);

            ResultDatabaseThread thread = new ResultDatabaseThread(
                    "SELECT * FROM course WHERE code = ?", args
            );
            thread.execute();
            ResultSet rs = thread.get();
            if (rs.next()){
                Course course = new Course(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("instructor")
                );

                // Get all the students enrolled in this course and add them to course
                ArrayList<Student> stu_list = getAllStudentsInCourse(course.getCourseCode());
                for (Student stu : stu_list){
                    course.addStudent(stu);
                }

                return course;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all of the courses in the database.
     * @return a List of all the courses in the database
     */
    public static List<Course> getAllCourses(){
        ArrayList<Course> course_list = new ArrayList<Course>();
        try {
            ResultDatabaseThread thread = new ResultDatabaseThread("SELECT * FROM course", null);
            thread.execute();
            ResultSet rs = thread.get();
            while (rs.next()){

                Course course = new Course(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("instructor")
                );

                // Get all the students enrolled in this course and add them to course
                ArrayList<Student> stu_list = getAllStudentsInCourse(course.getCourseCode());
                for (Student stu : stu_list){
                    course.addStudent(stu);
                }

                course_list.add(course);
            }
            return course_list;
        } catch (Exception e){
            e.printStackTrace();
        }
        return course_list;
    }

    /**
     * Enrol a student in a particular course.
     * @param email identifier of the student
     * @param code identifier of the course
     */
    public static void enrolStudentInCourse(String email, String code){
        // If course / student does not exist exit
        if (getStudent(email) == null || getCourse(code) == null){
            throw new IllegalArgumentException();
        }

        List<String> args = new ArrayList<String>();
        args.add(email);
        args.add(code);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "INSERT INTO TAKING VALUES (?,?)", args
        );
        thread.execute();
    }

    /**
     * Get a list of all the course codes of the courses this student is enrolled in.
     * @param email identifier of the student
     * @return a List of string codes of the courses being taken by this student
     */
    public static List<String> allStudentsCourses(String email){
        List<String> course_codes = new ArrayList<String>();
        try {
            List<String> args = new ArrayList<String>();
            args.add(email);
            ResultDatabaseThread thread = new ResultDatabaseThread(
                    "SELECT * FROM taking WHERE email = ?", args
            );
            thread.execute();
            ResultSet rs = thread.get();
            while(rs.next()){
                course_codes.add(rs.getString("code"));
            }
            return course_codes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return course_codes;
    }


    /* ---------- CHAT QUERIES ---------- */

    /**
     * Get the presentable name of the user when given an email.
     * @param email indentifier of the user
     * @return proper name of this user
     */
    public static String emailToName(String email){
        Student stu = getStudent(email);
        if(stu == null){
            throw new IllegalArgumentException();
        }
        return stu.getFname() + " " + stu.getLname().charAt(0) + ".";
    }

    public static void newChat(String email_user1, String email_user2){
        // Check that the two students exist
        if (getStudent(email_user1) == null || getStudent(email_user2) == null){
            throw new IllegalArgumentException();
        }
        int id = getMaxChatID() + 1;
        List<String> args = new ArrayList<>();
        args.add(email_user1);
        args.add(email_user2);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "INSERT INTO chats VALUES (" + Integer.toString(id) + ", ?);"
                + "INSERT INTO chats VALUES (" + Integer.toString(id) + ", ?)",
                args
        );
        thread.execute();
    }

    public static boolean chatExists(int chat_id){
        try {
            // Check if this chat exists
            ResultDatabaseThread thread = new ResultDatabaseThread(
                    "SELECT * FROM chats WHERE chat_id = " + Integer.toString(chat_id),
                    null
            );
            thread.execute();
            ResultSet rs1 = thread.get();
            if (rs1.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static Chat getChat(int chat_id){
        // Check if the chat exists or not
        if (!chatExists(chat_id)){
            throw new IllegalArgumentException();
        }
        try {
            ResultDatabaseThread thread = new ResultDatabaseThread(
                    "SELECT * FROM chat_line WHERE chat_id = " + Integer.toString(chat_id)
                    + " ORDER BY sent_at",
                    null
            );
            thread.execute();
            ResultSet rs = thread.get();
            Chat chat = new Chat(chat_id);
            while(rs.next()){
                Message msg = new Message(
                        rs.getString("email"),
                        rs.getString("content"),
                        rs.getString("sent_at")
                );
                chat.addMessage(msg);
            }
            return chat;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Chat> getAllChatsForUser(String email){
        // Check if student exists
        if(getStudent(email) == null){
            throw new IllegalArgumentException();
        }
        List<Chat> chats = new ArrayList<>();
        try {
            List<String> args = new ArrayList<>();
            args.add(email);
            ResultDatabaseThread thread = new ResultDatabaseThread(
                    "SELECT * FROM chats WHERE email = ?",
                    args
            );
            thread.execute();
            // Get all the chats this user belongs to
            List<Integer> chat_ids = new ArrayList<>();
            ResultSet rs = thread.get();
            while(rs.next()){
                chat_ids.add(rs.getInt("chat_id"));
            }

            // Get all the chat instances for this user
            for (Integer id : chat_ids){
                Chat chat = getChat(id);
                chats.add(chat);
            }
            return chats;
        } catch (Exception e){
            e.printStackTrace();
        }
        return chats;
    }
}
