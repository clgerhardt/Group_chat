import java.net.*;
import java.io.*;
 
public class Client {
    private String hostname;
    private int port;
    private String username;
    private Thread OutputThread;
    private Thread InputThread;
 
    public Client() {
        this.hostname = "localhost";
        this.port = 6013;
    }
 
    public void start_client() {
        try {
            Socket socket = new Socket(hostname, port);
 
            System.out.println("Connected to the server");
 
           OutputThread = new OutputThread(socket, this);
           InputThread = new InputThread(socket, this);

           OutputThread.start(); InputThread.start();
 
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
 
    }
 
    void set_username(String username) {
        this.username = username;
    }
 
    String get_username() {
        return this.username;
    }
 
    public static void main(String[] args) {
 
        Client client = new Client();
        client.start_client();
    }


    public class OutputThread extends Thread {
        private BufferedReader reader;
        private Socket socket;
        private Client client;
     
        public OutputThread(Socket socket, Client client) {
            this.socket = socket;
            this.client = client;
     
            try {
                InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
     
        public void run() {
            while (true) {
                try {
                    String client_response = reader.readLine();
                    System.out.println("\n" + client_response);
     
                    if (client.get_username() != null) {
                        System.out.print(client.get_username() + ": ");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    break;
                }
            }
        }
    }

    public class InputThread extends Thread {
        private PrintWriter writer;
        private Socket socket;
        private Client client;
     
        public InputThread(Socket socket, Client client) {
            this.socket = socket;
            this.client = client;
     
            try {
                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output, true);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
     
        public void run() {
            // Scanner doesn't work well in these case so we ended up using a console object instead
            // Console object works better in this case because it allows for part of a console text to be written and grabbed at the same time
            // The example below, states "Please enter your username: " and console will proceed to grab the rest of the line. 
            // This allows for seemless inline text printing and user input.
            Console console = System.console();
            String username = console.readLine("\nPlease enter your username: ");
            client.set_username(username);
            writer.println(username);
     
            String text = "";
    
            while(!text.equals("quit")){
                text = console.readLine(username + ": ");
                writer.println(text);
            }
     
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }
}