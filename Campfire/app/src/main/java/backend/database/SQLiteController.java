package backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to handle building and creating the database. Also handles the
 * communication and queries made to the database.
 * 
 * Creates at tables named 'student', 'course' and 'coursetostudent'
 */
public class SQLiteController {
	
	// Our connection instance to the database
	private static Connection connection = null;
	private static boolean initialized = false;
	
	/**
	 * Connect to the database Campfire.db database.
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void connectToDB() throws SQLException, ClassNotFoundException{
		// Set up the database connection
		Class.forName("org.sqlite.JDBC");
	    connection = DriverManager.getConnection("jdbc:sqlite:Campfire.db");
	    Statement statement = connection.createStatement();
	    
	    // Verify on first instance of this class that we are connected to the database
	    if (!initialized){
	    	initialized = true;
	    	
	    	// Check if the student table exists in the database
	    	String queryStudent = "SELECT name FROM sqlite_master WHERE type='table' AND name='student'";
	    	ResultSet resultStudent = statement.executeQuery(queryStudent);
	    	
	    	// If the student table does not exist in database, create it 
	    	if(!resultStudent.next()){
	    		String createQuery = "CREATE TABLE student(" + 
	    							 "email CHAR(20) PRIMARY KEY," +
	    							 "fname CHAR(20)," + 
	    							 "lname CHAR(20)," +
	    							 "pass CHAR(20));";
	    		statement.executeUpdate(createQuery);
	    	}
	    	
	    	// Check if the course table exists in the database
	    	String queryCourse = "SELECT name FROM sqlite_master WHERE type='table' AND name='course'";
	    	ResultSet resultCourse = statement.executeQuery(queryCourse);
	    	
	    	// If the course table does not exist in database, create it 
	    	if(!resultCourse.next()){
	    		String createQuery = "CREATE TABLE course(" + 
	    							 "code CHAR(20) PRIMARY KEY," +
	    							 "name CHAR(20)," + 
	    							 "instructor CHAR(20));";
	    		statement.executeUpdate(createQuery);
	    	}
	    	
	    	// Check if the coursetostudent table exists in the database
	    	String queryCourseToStudent = "SELECT name FROM sqlite_master WHERE type='table' AND name='coursetostudent'";
	    	ResultSet resultCourseToStudent = statement.executeQuery(queryCourseToStudent);
	    	
	    	// If the coursetostudent table does not exist in database, create it 
	    	if(!resultCourseToStudent.next()){
	    		String createQuery = "CREATE TABLE coursetostudent(" + 
	    							 "code CHAR(20)," +
	    							 "email CHAR(20));";
	    		statement.executeUpdate(createQuery);
	    	}
	    }
	    
	    statement.close();
	}
	
	/**
	 * Return the Connection instance that this SQLite Controller is attached.
	 * @return Connection instance to the database we are connected to
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		// Check if we need to connect to the database, do so if needed
		if (connection == null){
			connectToDB();
		}
		return SQLiteController.connection;
	}
	
	/**
	 * Method used to request information from the database, with giving a query.
	 * @param query a string of the query that is requesting information
	 * @return the ResultSet object of the return value from the query
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet returnQuery(PreparedStatement state) throws ClassNotFoundException, SQLException{
		// Check if we need to connect to the database, do so if needed
		if (connection == null){
			connectToDB();
		}
		return state.executeQuery();
	}
	
	/**
	 * Updates the database based on the query that is given.
	 * @param query that is being requesting modification of the database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateQuery(PreparedStatement state) throws ClassNotFoundException, SQLException{
		// Check if we need to connect to the database, do so if needed
		if (connection == null){
			connectToDB();
		}
		state.executeUpdate();
	}
	
	
	/**
	 * End the connection to the Campfire.db database.
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void closeConnectionToDB() throws SQLException, ClassNotFoundException{
		// Close connection and statement object
		if (connection != null){
			connection.close();
		}
	}
}
