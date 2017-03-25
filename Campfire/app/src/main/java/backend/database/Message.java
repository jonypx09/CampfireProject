package backend.database;

/**
 * Class to store a message in a chat and hold corresponding information.
 */
public class Message {

    private String sender_email;
    private String text;
    private String timestamp;

    public Message(String sender_email, String text, String timestamp){
        this.sender_email = sender_email;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSender_email() {
        return sender_email;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
