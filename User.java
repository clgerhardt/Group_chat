import java.util.ArrayList;
import java.util.HashMap;
import java.net.*;

public class User {
    String username;
    HashMap<User, ArrayList<String>> messages  = new HashMap<>(); 

    public User(String username) {
        this.username = username;
    }

    
    public String getUsername() {
        return username;
    }

    public void addPMessage(String message, User sendingUser) {

        if(messages.containsKey(User)) {
            ArrayList m = messages.get(User);
            m.add(message);
        } else {
            messages.put(user, new ArrayList<String>());
        }

    }

    public static void main(String[] args) {
        
    }
    
}