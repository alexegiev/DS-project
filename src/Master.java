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

                //TODO: REDUCER
//                // Connect to the Reducer and receive data
//                reducerSocket = server.accept();
//                System.out.println("Connected to Reducer");
//
//                // Process data from Reducer
//                ObjectOutputStream reducerOut = new ObjectOutputStream(reducerSocket.getOutputStream());
//                ObjectInputStream reducerIn = new ObjectInputStream(reducerSocket.getInputStream());
//
//                if (reducerIn.available() > 0) {
//                    Room testingRoom = (Room) reducerIn.readObject();
//                    System.out.println("Room: " + testingRoom.toString());
//
//                    // Send data to the Client
//                    ObjectOutputStream clientOut = new ObjectOutputStream(clientSocket.getOutputStream());
//                    ObjectInputStream clientIn = new ObjectInputStream(clientSocket.getInputStream());
//                    clientOut.writeObject(testingRoom);
//                    clientOut.flush();
//                }

//                // Connect to the Reducer and receive data
//                reducerSocket = server.accept();
//                System.out.println("Connected to Reducer "
//                        + reducerSocket.getInetAddress().getHostAddress());
//
//                // Process data from Reducer
//                ObjectOutputStream reducerOut = new ObjectOutputStream(reducerSocket.getOutputStream());
//                ObjectInputStream reducerIn = new ObjectInputStream(reducerSocket.getInputStream());
//                Room testingRoom = (Room) reducerIn.readObject();
//
//                // Send data to the Client
//                ObjectOutputStream clientOut = new ObjectOutputStream(clientSocket.getOutputStream());
//                ObjectInputStream clientIn = new ObjectInputStream(clientSocket.getInputStream());
//
//                // Send the Room object
//                clientOut.writeObject(testingRoom);
//                clientOut.flush();
            }
            catch(Exception e){
                System.out.println("Error: " + e);
                e.printStackTrace();
                return;
            }

        }
    }
}