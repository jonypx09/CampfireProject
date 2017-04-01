package backend.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rod on 23/03/2017.
 */

public class Chat {

    private int chatID;
    private List<Message> messages;

    public Chat(int chatID){
        this.chatID = chatID;
        this.messages = new ArrayList<Message>();
    }

    public Chat(int chatID, List<Message> messages){
        this.chatID = chatID;
        this.messages = messages;
    }

    public int getChatID() {
        return chatID;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message msg){
        this.messages.add(msg);
    }
}
