public class Message{
    private String message;
    private String user;
    private String timestamp;

    public Message(String message, String user, String timestamp){
        this.message = message;
        this.user = user;
        this.timestamp = timestamp;
    }

    public String getMessage(){
        return this.message;
    }

    public String getUser(){
        return this.user;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

}