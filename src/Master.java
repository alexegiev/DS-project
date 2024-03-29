import java.io.*;
import java.net.*;
/*
*
* This is the Master class of the Distributed System
* It's main purpose is to
*   Listen for incoming Client connections
*   Send the data of the Client to the Worker class
*   Accept data from Reducer class
*   Send data from the Reducer class, to the Client
*/
public class Master {

    // Define socket that receives incoming requests
    ServerSocket server = null;

    // Define socket that is used to handle the connection
    Socket serverSocket = null;

    public static void main(String[] args){

        new Master().startServer();
    }

    void startServer() {

        try {
            server = new ServerSocket(9090);
            while (true) {
                System.out.println("Waiting for client request");
                Socket client = server.accept();
                System.out.println("Connected to client "
                        + client.getInetAddress().getHostAddress());

                // create a new ServerThread object
                Worker thread = new Worker(client);

                thread.start();
            }
        }
        catch(IOException e){
            System.out.println("Error: " + e);
            return;
        }
    }
}