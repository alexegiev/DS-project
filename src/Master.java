import java.io.*;
import java.net.*;

public class Master {

    public static void main(String[] args){

        ServerSocket server;

        try {
            server = new ServerSocket(9090);
            while (true) {
                System.out.println("Waiting for client request");
                Socket client = server.accept();
                System.out.println("Connected to client"
                                    + client.getInetAddress().getHostAddress());

                ServerThread clientThread = new ServerThread(client);

                new Thread(clientThread).start();
            }

        }
        catch(IOException e){
            System.out.println("Error: " + e);
            return;
        }
    }
}