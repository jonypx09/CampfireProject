package backend.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backend.algorithms.CampfireGroup;
import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.PinCourse;
import backend.algorithms.PinGroup;
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
                if (comparables == null){
                    return new ArrayList<Comparable>();
                }
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

    /**
     * Add a match of students in the database.
     * @param email of the student who accepted the other student
     * @param course_name what course these two students are in
     * @param email_matched the student that has been selected by the other
     */
    public static void addMatch(String email, String course_name, String email_matched){
        // Make sure that students exists and that course exists
//        if (getStudent(email) == null || getCourse(course_name) == null ||
//                getStudent(email_matched) == null){
//            return;
//        }
        List<String> args = new ArrayList<>();
        args.add(email);
        args.add(course_name);
        args.add(email_matched);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "INSERT INTO matched VALUES (?,?,?)",
                args
        );
        thread.execute();
    }

    /**
     * Add a students matched pairs into the database
     * @param email of the student
     * @param matches map of all the students matches
     */
    private static void addMatchedPairs(String email, Map<String, List<Student>> matches){
        // Delete previous matches
        List<String> args = new ArrayList<>();
        args.add(email);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "DELETE FROM matched WHERE email = ?",
                args
        );
        thread.execute();
        // Add all students matches in the database
        for(String course_name : matches.keySet()){
            for (Student s : matches.get(course_name)){
                addMatch(email, course_name, s.getEmail());
            }
        }
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

        // Add all of the students matches
        addMatchedPairs(student.getEmail(), student.getMatchedStudents());
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
                student.setDescription(rs.getString("description"));

                // Add all matches to this students matches
                Map<String, ArrayList<Student>> matches = DbAdapter.getMatchedMap(email);
                for (String course_name : matches.keySet()){
                    student.getAvailablematches().put(course_name, matches.get(course_name));
                }
            }
            return student;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a student without his matched students.
     * @param email of the student
     * @return student object without his matched students
     */
    public static Student getStudentLite(String email){
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
                student.setDescription(rs.getString("description"));
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

                // Add all matches to this students matches
                Map<String, ArrayList<Student>> matches = DbAdapter.getMatchedMap(student.getEmail());
                for (String course_name : matches.keySet()){
                    student.getAvailablematches().put(course_name, matches.get(course_name));
                }

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

        // Update this students matched pairs
        addMatchedPairs(student.getEmail(), student.getMatchedStudents());
    }

    /**
     * Get a students matches.
     * @param email of the student
     * @return a Map of all the students matches
     */
    public static Map<String, ArrayList<Student>> getMatchedMap(String email){
        Map<String, ArrayList<Student>> matches = new HashMap<>();
        List<String> args = new ArrayList<>();
        args.add(email);
        ResultDatabaseThread thread = new ResultDatabaseThread(
                "SELECT * FROM matched WHERE email = ?",
                args
        );
        thread.execute();
        try {
            ResultSet rs = thread.get();
            while (rs.next()){
                String course_name = rs.getString("code");
                String matched_with = rs.getString("matched_with");
                if (matches.containsKey(course_name)){
                    matches.get(course_name).add(getStudentLite(matched_with));
                } else {
                    ArrayList<Student> stu_list = new ArrayList<>();
                    stu_list.add(getStudentLite(matched_with));
                    matches.put(course_name, stu_list);
                }
            }
            return matches;
        } catch (Exception e){
            e.printStackTrace();
        }
        return matches;
    }

    /* ---------- COURSE QUERIES ---------- */

    /**
     * Add a course in to the database.
     * @param course instance to be saved in the database
     */
    public static void addCourse(Course course){
        // Check if the course already exists
        if (getCourse(course.getName()) != null){
            throw new IllegalArgumentException();
        }

        List<String> args = new ArrayList<String>();
        args.add(course.getName());
        args.add(course.getDescription());
        args.add(course.getInstructor());
        UpdateDatabaseThread thread  = new UpdateDatabaseThread(
                "INSERT INTO course VALUES (?,?,?)", args
        );
        thread.execute();

        // Add all this courses students to be enrolled in this course
        for (Student stu : course.getStudents()){
            enrolStudentInCourse(stu.getEmail(), course.getName());
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
                ArrayList<Student> stu_list = getAllStudentsInCourse(course.getName());
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
                ArrayList<Student> stu_list = getAllStudentsInCourse(course.getName());
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

    /* ---------- GROUP QUERIES ----------- */

    /**
     * Get a unique key to create a new group with.
     * @return an int that is a unique key
     */
    public static int getUniqueGroupKey(){
        ResultDatabaseThread thread = new ResultDatabaseThread(
                "SELECT MAX(group_id) AS max from campfire_group",
                null
        );
        thread.execute();
        int unique_key = 1;
        try {
            ResultSet rs = thread.get();
            if (rs.next()){
                unique_key = rs.getInt("max") + 1;
            }
            return unique_key;
        } catch (Exception e){
            e.printStackTrace();
        }
        return unique_key;
    }

    /**
     * Get all the students in a given group.
     * @param group_id group_id you want students for
     * @return a List of students in that group
     */
    public static ArrayList<Student> getAllStudentsInGroup(int group_id){
        ArrayList<Student> stu_list = new ArrayList<Student>();
        try {
            ResultDatabaseThread thread = new ResultDatabaseThread(
                    "SELECT * FROM group_membership WHERE group_id = " + Integer.toString(group_id),
                    null
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
     * Get a CampfireGroup.
     * @param group_id id of the group wanted
     * @return instance of that CampfireGroup
     */
    public static CampfireGroup getGroup(int group_id){
        ResultDatabaseThread thread = new ResultDatabaseThread(
                "SELECT * FROM campfire_group WHERE group_id = " + Integer.toString(group_id),
                null
        );
        thread.execute();
        try {
            ResultSet rs = thread.get();
            if (rs.next()){
                ArrayList<Student> students = getAllStudentsInGroup(group_id);
                CampfireGroup group = new CampfireGroup(
                        rs.getString("name"),
                        students,
                        rs.getInt("size"),
                        group_id
                );
                return group;
            } else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Add student to a group.
     * @param email identifier of the student
     * @param group_id id of the group
     */
    public static void addStudentToGroup(String email, int group_id){
        // Check that the student exists
        if (getStudent(email) == null){
            throw new IllegalArgumentException();
        }
        // Check that the group exists
        CampfireGroup group = getGroup(group_id);
        if (group == null){
            throw new IllegalArgumentException();
        }
        // Check if the group is full
        if (getAllStudentsInGroup(group_id).size() >= group.getSize()){
            throw new IllegalArgumentException();
        }
        List<String> args = new ArrayList<>();
        args.add(email);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "INSERT INTO group_membership VALUES (?," + Integer.toString(group_id) + ")",
                args
        );
        thread.execute();
    }

    /**
     * Add a new group to the database.
     * @param group instance wanting to be stored in the database
     */
    public static void addGroup (CampfireGroup group){
        // Group already exists
        if (getGroup(group.getGroupID()) != null){
            throw new IllegalArgumentException();
        }

        List<String> args = new ArrayList<>();
        args.add(group.getName());
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "INSERT INTO campfire_group VALUES (" + Integer.toString(group.getGroupID()) + ",?," + Integer.toString(group.getSize()) + ")",
                args
        );
        thread.execute();
        // Add all the students to this group
        for (Student s : group.getMembers()){
            addStudentToGroup(s.getEmail(), group.getGroupID());
        }
    }

    /**
     * Get all the groups that a student is in.
     * @param email email of the student
     * @return a list of Campfire groups that this student is in
     */
    public static List<CampfireGroup> getAllStudentsGroups(String email){
        List<CampfireGroup> groups = new ArrayList<>();

        List<String> args = new ArrayList<>();
        args.add(email);

        ResultDatabaseThread thread = new ResultDatabaseThread(
                "SELECT DISTINCT group_id FROM group_membership WHERE email = ?",
                args
        );
        thread.execute();
        try {
            ResultSet rs = thread.get();
            while (rs.next()) {
                groups.add(getGroup(rs.getInt("group_id")));
            }
            return groups;
        } catch (Exception e){
            e.printStackTrace();
        }
        return groups;
    }

    /**
     * Delete a specified group
     * @param group_id id of the group wanted to be deleted
     */
    public static void deleteGroup (int group_id){
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "DELETE FROM campfire_group WHERE group_id = " + Integer.toString(group_id),
                null
        );
        thread.execute();
    }

    /**
     * Remove a student from a group/
     * @param email of the student to be removed
     * @param group_id of the group to remove student from
     */
    public static void removeStudentFromGroup(String email, int group_id){
        if (getStudent(email) == null ||getGroup(group_id) == null){
            return;
        }
        List<String> args = new ArrayList<>();
        args.add(email);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "DELETE FROM group_membership WHERE email = ? AND group_id = " + Integer.toString(group_id),
                args
        );
        thread.execute();
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

    /**
     * Determine whether a user is in a chat or not.
     * @param email identifier of the user
     * @param chat_id id of the chat
     * @return true if student is in this chat, false otherwise
     */
    public static boolean userInChat(String email, int chat_id){
        List<String> args = new ArrayList<>();
        args.add(email);
        ResultDatabaseThread thread = new ResultDatabaseThread(
                "SELECT * FROM chats WHERE chat_id = " + Integer.toString(chat_id) + " AND email = ?",
                args
        );
        thread.execute();
        try {
            ResultSet rs = thread.get();
            return rs.next();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Create a new chat between two students.
     * @param email_user1
     * @param email_user2
     */
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

    /**
     * Determines whether a chat exists or not
     * @param chat_id id of the chat
     * @return true if chat exists, false otherwise
     */
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

    /**
     * Get a chat object.
     * @param chat_id id of the chat wanted
     * @return chat instance specified by id
     */
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

    /**
     * Get all the chat objects that a user is in.
     * @param email identifier of the user
     * @return list of all the chat objects this student is in
     */
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

    /**
     * Add a message to a chat.
     * @param chat_id id of the chat
     * @param sender_email email of the sender
     * @param msg_text content of the message
     */
    public static void addMessage(int chat_id, String sender_email, String msg_text){
        // Check if the chat exists
        if (!chatExists(chat_id)){
            throw new IllegalArgumentException();
        }
        // Check if student exists in the chat
        if (!userInChat(sender_email, chat_id)){
            throw new IllegalArgumentException();
        }
        List<String> args = new ArrayList<>();
        args.add(sender_email);
        args.add(msg_text);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "INSERT INTO chat_line VALUES(" + Integer.toString(chat_id) + ",?,?)",
                args
        );
        thread.execute();
    }

    /**
     * Add a student to an existing chat
     * @param chat_id id of the chat
     * @param email of the user that is being added
     */
    public static void addStudentToChat(int chat_id, String email){
        // Check if the student exists
        if (getStudent(email) == null){
            throw new IllegalArgumentException();
        }
        // Check if chat exists
        if (!chatExists(chat_id)){
            throw new IllegalArgumentException();
        }
        List<String> args = new ArrayList<>();
        args.add(email);
        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "INSERT INTO chats VALUES (" + Integer.toString(chat_id) + ",?)",
                args
        );
        thread.execute();
    }

    /**
     * Return a list of emails of the students that are in the given chat.
     * @param chat_id of the chat we want to find the students for
     * @return a List of student emails in the chat
     */
    public static List<String> getAllStudentsInChat(int chat_id){
        List<String> stu_emails = new ArrayList<>();
        ResultDatabaseThread thread = new ResultDatabaseThread(
                "SELECT * FROM chats WHERE chat_id = " + Integer.toString(chat_id),
                null
        );
        thread.execute();
        try {
            ResultSet rs = thread.get();
            while(rs.next()){
                stu_emails.add(rs.getString("email"));
            }
            return stu_emails;
        } catch (Exception e){
            e.printStackTrace();
        }
        return stu_emails;
    }

    /* ---------- PIN QUERIES ---------- */
=======
                        "SELECT * FROM chats WHERE chat_id = " + Integer.toString(chat_id),
                        null
                        );
        thread.execute();
        try {
                ResultSet rs = thread.get();
                while(rs.next()){
                        stu_emails.add(rs.getString("email"));
                    }
                return stu_emails;
            } catch (Exception e){
                e.printStackTrace();
            }
        return stu_emails;
    }

        /* ---------- PIN QUERIES ---------- */

    private static void insertPinCourse(String code, String pin){
        // Make sure that the course exists
        if (getCourse(code) != null) {
            List<String> args = new ArrayList<>();
            args.add(code);
            args.add(pin);
            UpdateDatabaseThread thread = new UpdateDatabaseThread(
                    "INSERT INTO course_pins VALUES (?,?)",
                    args
            );
            thread.execute();
        }
    }

    private static void insertPinGroup(int group_id, String pin){
        // Make sure that the group exists
        if (getGroup(group_id) != null) {
            List<String> args = new ArrayList<>();
            args.add(pin);
            UpdateDatabaseThread thread = new UpdateDatabaseThread(
                    "INSERT INTO group_pins VALUES (" + Integer.toString(group_id) + ",?)",
                    args
            );
            thread.execute();
        }
    }

    /**
     * Store PinCourse object in the database. Deletes previous one.
     * @param pc PinCourse instance
     */
    public static void setPinCourse(PinCourse pc){

        Map<String, Course> pins = pc.getCoursePins();

        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "DELETE FROM course_pins",
                null
        );
        thread.execute();

        for (String pin : pins.keySet()){
            insertPinCourse(pins.get(pin).getName(), pin);
        }
    }

    /**
     * Get PinCourse object stored in the database.
     * @return PinCourse instance
     */
    public static PinCourse getPinCourse(){
        PinCourse pc = new PinCourse();
        Map<String, Course> pins = pc.getCoursePins();
        // Get all the new pins to from db and store in PinCourse object
        ResultDatabaseThread thread = new ResultDatabaseThread(
                "SELECT * FROM course_pins",
                null
        );
        thread.execute();
        try {
            ResultSet rs = thread.get();
            while(rs.next()){
                pins.put(rs.getString("pin"), getCourse(rs.getString("code")));
            }
            return pc;
        } catch (Exception e){
            e.printStackTrace();
        }
        return pc;
    }

    /**
     * Store PinGroup object in the database. Deletes previous one.
     * @param pg PinGroup instance
     */
    public static void setPinGroup(PinGroup pg){
        Map<String, CampfireGroup> pins = pg.getGroupPins();

        UpdateDatabaseThread thread = new UpdateDatabaseThread(
                "DELETE FROM group_pins",
                null
        );
        thread.execute();

        for (String pin : pins.keySet()){
            insertPinGroup(pins.get(pin).getGroupID(), pin);
        }
    }

    /**
     * Get PinGroup object stored in the database.
     * @return PinGroup instance
     */
    public static PinGroup getPinGroup(){
        PinGroup pg = new PinGroup();
        Map<String, CampfireGroup> pins = pg.getGroupPins();
        // Get all the new pins to from db and store in PinCourse object
        ResultDatabaseThread thread = new ResultDatabaseThread(
                "SELECT * FROM group_pins",
                null
        );
        thread.execute();
        try {
            ResultSet rs = thread.get();
            while(rs.next()){
                pins.put(rs.getString("pin"), getGroup(rs.getInt("group_id")));
            }
            return pg;
        } catch (Exception e){
            e.printStackTrace();
        }
        return pg;
    }
}


