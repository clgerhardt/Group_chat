import java.net.*;
import java.io.*;
import java.util.*;

public class Server{

	public static void main(String[] args){

        ArrayList<User> users = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>();

        try{
            ServerSocket sock = new ServerSocket(6013);
			System.out.println("Server started at "+ new Date() + '\n');

            while(true){

            }


        }
        catch(IOException ioe){
            System.err.println(ioe);

        }
    }

}