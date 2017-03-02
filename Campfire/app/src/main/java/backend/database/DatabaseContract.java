package com.example.rod.database;

import android.provider.BaseColumns;

/**
 * Created by Rod on 28/02/2017.
 */

/**
 * Contract class that stores String constants to be accessed by the database.
 */
public final class DatabaseContract {

    // We dont want anyone creating an instance of DatabaseContract
    private DatabaseContract(){}

    /**
     * Fields for the student table in the database.
     */
    public static class StudentContract implements BaseColumns {
        public static final String TABLE_NAME = "student";
        public static final String COLUMN_NAME_FNAME = "fname";
        public static final String COLUMN_NAME_LNAME = "lname";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASS = "pass";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    /**
     * Fields for the course table in the database.
     */
    public static class CourseContract implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_NAME_CODE = "code";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_INSTRUCTOR = "instructor";
    }
}
