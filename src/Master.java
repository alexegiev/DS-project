import java.io.*;
import java.net.*;

public class Master {

    public static void main(String[] args){

        ServerSocket server;

        // counter used for debugging purposes
        int counter = 0;

        try {
            server = new ServerSocket(9090);
            while (true) {
                System.out.println("Waiting for client request");
                Socket client = server.accept();
                System.out.println("Connected to client "
                                    + client.getInetAddress().getHostAddress());
                counter++;

                // create a new thread object
                ServerThread thread = new ServerThread(client);
            }
        }
        catch(IOException e){
            System.out.println("Error: " + e);
            return;
        }
    }
}