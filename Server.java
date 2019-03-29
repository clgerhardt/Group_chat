import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
        private int port;
        private Set<String> usernames = new HashSet<>();
        private Set<UserThread> user_threads = new HashSet<>();
        private HashMap<String, UserThread> user_map = new HashMap<>();
     
        public Server() {
            this.port = 6013;
        }
     
        public void run_server() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
     
                System.out.println("Chat Server is listening on port " + port);
     
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("New user connected");
     
                    UserThread new_user = new UserThread(socket, this);
                    user_threads.add(new_user);
                    new_user.start();
     
                }
     
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        } 

        void send_to_group(String message, UserThread dont_sent_to_this_user) {
            for (UserThread u : user_threads) {
                if (u != dont_sent_to_this_user) {
                    u.sendMessage(message);
                }
            }
        }

        void send_to_person(String message, String specific_user, UserThread user){
            if(user_map.containsKey(specific_user)){
                UserThread u = user_map.get(specific_user);
                u.sendMessage(message);
            }
            else{
                user.sendMessage("This user not in chat. Make sure you spelled their name correctly.");
            }
        }
     
        void add_username(String username, UserThread user_thread) {
            usernames.add(username);
            user_map.put(username, user_thread);
        }
     
        void remove_user(String user_name, UserThread user) {
            boolean removed = usernames.remove(user_name);
            if (removed) {
                user_threads.remove(user);
                user_map.remove(user_name);
                System.out.println("The user " + user_name + " left");
            }
        }
     
        Set<String> get_usernames() {
            return this.usernames;
        }

        public static void main(String[] args) {
            Server server = new Server();
            server.run_server();
        }

        public class UserThread extends Thread {
            private Socket socket;
            private Server server;
            private PrintWriter writer;
         
            public UserThread(Socket socket, Server server) {
                this.socket = socket;
                this.server = server;
            }

            void sendMessage(String message) {
                writer.println(message);
            }
         
            public void run() {
                try {
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         
                    OutputStream output = socket.getOutputStream();
                    writer = new PrintWriter(output, true);
                  
                    String username = reader.readLine();
                    server.add_username(username, this);
         
                    String server_message = "New user connected: " + username;
                    server.send_to_group(server_message, this);
         
                    String clientMessage= "";
                    while (!clientMessage.equals("quit")){
                        clientMessage = reader.readLine();
                        System.out.println(clientMessage.startsWith("@"));
                        if(clientMessage.startsWith("@")){
                            String [] result = clientMessage.split(" ", 2);
                            server_message = username + ": " + result[1];
                            server.send_to_person(server_message, result[0].substring(1), this);
                        }
                        else{
                            server_message = username + ": " + clientMessage;
                            server.send_to_group(server_message, this);
                        }
                    }
         
                    server.remove_user(username, this);
                    socket.close();
         
                    server_message = username + " has left.";
                    server.send_to_group(server_message, this);
         
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
         
        }
}