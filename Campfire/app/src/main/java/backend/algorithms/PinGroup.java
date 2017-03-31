package backend.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class PinGroup {
	
	private Map<String, CampfireGroup> GroupPins;
			
	// Creates a map where a PIN is a special ID for each course
	public PinGroup() {
		this.GroupPins = new HashMap<String, CampfireGroup>();
	}
			
	// Generates a 15 Character PIN code for each course that is created
	public String generatePin(){
		String allCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String newPin = "";
		Random random = new Random();
		
		while (newPin.length() < 15){
			newPin += allCharacters.charAt(random.nextInt(36));
		}
		
		//Create a Unique PIN
		for(String PINS : this.getGroupPins().keySet()){
			if(PINS == newPin){
				newPin = "";
				while (newPin.length() < 15){
					newPin += allCharacters.charAt(random.nextInt(36));
				}
			}
		}
		
	    return newPin; 
	}
		
	// Get the entire map of all the groups and pins
	public Map<String, CampfireGroup> getGroupPins(){
		return this.GroupPins;
	}
	
	// Extract the PIN for the given group
	public String findPin(CampfireGroup group){
		for(Entry<String, CampfireGroup> value : this.GroupPins.entrySet()){
			if(group == value.getValue()){
				return value.getKey();
			}
		}
		return null;
	}
	
	//Add a Group with the designated PIN
	public void addPinGroup(CampfireGroup group){
		this.GroupPins.put(generatePin(), group);
	}
	
	//Remove a Group by using the designated PIN
	public void removePinGroup(String pin){
		this.GroupPins.remove(pin);
	}
	
	//For database team to add load the hashmap in right away
	public void setPinGroup(HashMap<String, CampfireGroup> setter){
		this.GroupPins = setter;
	}

}
