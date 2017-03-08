package backend.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to assist in creating and deleting the tables in the database.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Campfire.db";

    /* ---------- Queries to use for to create/delete tables in this database ---------- */
    private static final String SQL_CREATE_STUDENT_TABLE =
            "CREATE TABLE " + DatabaseContract.StudentContract.TABLE_NAME + " (" +
                    DatabaseContract.StudentContract.COLUMN_NAME_EMAIL + " TEXT PRIMARY KEY," +
                    DatabaseContract.StudentContract.COLUMN_NAME_FNAME + " TEXT," +
                    DatabaseContract.StudentContract.COLUMN_NAME_LNAME + " TEXT," +
                    DatabaseContract.StudentContract.COLUMN_NAME_PASS + " TEXT," +
                    DatabaseContract.StudentContract.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    DatabaseContract.StudentContract.COLUMN_NAME_COMPARABLE + " TEXT)";

    private static final String SQL_DELETE_STUDENT_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.StudentContract.TABLE_NAME;

    private static final String SQL_CREATE_COURSE_TABLE =
            "CREATE TABLE " + DatabaseContract.CourseContract.TABLE_NAME + " (" +
                    DatabaseContract.CourseContract.COLUMN_NAME_CODE + " TEXT PRIMARY KEY," +
                    DatabaseContract.CourseContract.COLUMN_NAME_NAME + " TEXT," +
                    DatabaseContract.CourseContract.COLUMN_NAME_INSTRUCTOR + " TEXT)";

    private static final String SQL_DELETE_COURSE_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.CourseContract.TABLE_NAME;

    private static final String SQL_CREATE_TAKING_TABLE =
            "CREATE TABLE " + DatabaseContract.TakingContract.TABLE_NAME + " (" +
                    DatabaseContract.TakingContract.COLUMN_NAME_CODE + " TEXT," +
                    DatabaseContract.TakingContract.COLUMN_NAME_EMAIL + " TEXT)";

    private static final String SQL_DELETE_TAKING_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.TakingContract.TABLE_NAME;

    /**
     * Creates an instance of the DatabaseHelper.
     * @param context of the activity that is creating this object
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create all the tables required in this database.
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_STUDENT_TABLE);
        db.execSQL(SQL_CREATE_COURSE_TABLE);
        db.execSQL(SQL_CREATE_TAKING_TABLE);
    }


    /**
     * Reset the database by removing the tables in the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_STUDENT_TABLE);
        db.execSQL(SQL_DELETE_COURSE_TABLE);
        db.execSQL(SQL_DELETE_TAKING_TABLE);
        onCreate(db);
    }

}
