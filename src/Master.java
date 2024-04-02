import java.io.*;
import java.net.*;
import entities.Room;
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
    Socket clientSocket = null;
    Socket reducerSocket = null;

    ObjectOutputStream clientOut = null;
    ObjectInputStream clientIn = null;

    private static int clientCount = 1;

    public static void main(String[] args){

        // Initialize Rooms
        Room room1 = new Room("Room1", 1, 1, "area1", 3.5, 5, "../static/room1img.jpg");
        Room room2 = new Room("Room2", 2, 2, "area2", 4.5, 6, "../static/room2img.jpg");
        Room room3 = new Room("Room3", 3, 3, "area3", 5.5, 7, "../static/room3img.jpg");
        Room room4 = new Room("Room4", 4, 4, "area4", 6.5, 8, "../static/room4img.jpg");
        Room room5 = new Room("Room5", 5, 5, "area5", 7.5, 9, "../static/room5img.jpg");

        new Master().startServer();
    }

    void startServer() {

        // Create a new ServerSocket object and wait for clients
        try {
            server = new ServerSocket(9090);
            System.out.println("Server started at port: " + server.getLocalPort());
            waitForClient();
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch(IOException e){
            System.out.println("Error: " + e);
            return;
        }
    }

    void waitForClient() {
        while (true) {
            try {
                System.out.println("Waiting for client request");
                clientSocket = server.accept();
                System.out.println("Client No. " + clientCount + " connected ");
                clientCount++;

                // create a new ServerThread object
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThread.start();

            }
            catch(Exception e){
                System.out.println("Error: " + e);
                e.printStackTrace();
                return;
            }

        }
    }
}