import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
        private int port;
        private Set<String> usernames = new HashSet<>();
        private Set<UserThread> user_threads = new HashSet<>();
     
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

        void send_to_group(String message, UserThread exclude_user) {
            for (UserThread u : user_threads) {
                if (u != exclude_user) {
                    u.sendMessage(message);
                }
            }
        }

        void send_to_person(String message, UserThread specific_user){
            for (UserThread u : user_threads) {
                if (u == specific_user) {
                    u.sendMessage(message);
                }
            }
        }
     
        void add_username(String username) {
            usernames.add(username);
        }
     
        void remove_user(String user_name, UserThread user) {
            boolean removed = usernames.remove(user_name);
            if (removed) {
                user_threads.remove(user);
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
         
            public void run() {
                try {
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         
                    OutputStream output = socket.getOutputStream();
                    writer = new PrintWriter(output, true);
                  
                    String username = reader.readLine();
                    server.add_username(username);
         
                    String server_message = "New user connected: " + username;
                    server.send_to_group(server_message, this);
         
                    String clientMessage= "";
                    while (!clientMessage.equals("quit")){
                        clientMessage = reader.readLine();
                        server_message = username + ": " + clientMessage;
                        server.send_to_group(server_message, this);
                    }
         
                    server.remove_user(username, this);
                    socket.close();
         
                    server_message = username + " has left.";
                    server.send_to_group(server_message, this);
         
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
         
            void sendMessage(String message) {
                writer.println(message);
            }
        }

}