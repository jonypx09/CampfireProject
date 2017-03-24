package backend.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import backend.algorithms.HobbiesCriteria;
import backend.algorithms.CSCCoursesCriteria;
import android.util.Base64;

/**
 * Class to provide two methods to serialize objects to a string and also deserialize strings to objects.
 * This class will primarily be used in the database to store abstract instances of criteria for users.
 *
 * Inspiration for this class was received from the following source: 
 * http://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
 */
public final class Serializer {

	// Private constructor because we don't want anyone creating instances of this class
	private Serializer(){}
	
	/**
	 * Serialize the object into a string.
	 * @param s the Serializable object to serialize into a String
	 * @return the String of the serialized object
	 * @throws IOException
	 */
	public static String serialize(Serializable s){
		if(s == null){
			return null;
		}
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject(s);
			objectStream.close();

			return Base64.encodeToString(byteStream.toByteArray(), Base64.DEFAULT);
		} catch (Exception e){
			return null;
		}
	}
	
	/**
	 * Deserialize a string into an object.
	 * @param s the String to be deserialized into an Object
	 * @return type object returned from the deserialized String
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(String s) throws IOException, ClassNotFoundException{
		if (s == null){
			return null;
		}
		try {
			byte data[] = Base64.decode(s, Base64.DEFAULT);
			ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data));
			Object obj = objectStream.readObject();
			objectStream.close();

			return obj;
		} catch (Exception e){
			return null;
		}
	}
}
