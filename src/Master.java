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
    ServerSocket serverToClient = null;
    ServerSocket server = null;

    // Define socket that is used to handle the connection
    Socket clientSocket = null;
    Socket reducerSocket = null;

    ObjectOutputStream clientOut = null;
    ObjectInputStream clientIn = null;

    public static void main(String[] args){

        new Master().startServer();
    }

    void startServer() {

        try {
            server = new ServerSocket(9090);
            while (true) {
                System.out.println("Waiting for client request");
                clientSocket = server.accept();
                System.out.println("Connected to client "
                        + clientSocket.getInetAddress().getHostAddress());

                // create a new ServerThread object
                Worker thread = new Worker(clientSocket);
                thread.start();

                // Connect to the Reducer and receive data
                System.out.println("Waiting for Reducer data");
                reducerSocket = server.accept();
                System.out.println("Connected to Reducer "
                        + reducerSocket.getInetAddress().getHostAddress());

                // Process data from Reducer
                ObjectOutputStream reducerOut = new ObjectOutputStream(reducerSocket.getOutputStream());
                ObjectInputStream reducerIn = new ObjectInputStream(reducerSocket.getInputStream());
                Room testingRoom = (Room) reducerIn.readObject();

                // Send data to the Client
                ObjectOutputStream clientOut = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream clientIn = new ObjectInputStream(clientSocket.getInputStream());

                // Send the Room object
                clientOut.writeObject(testingRoom);
                clientOut.flush();

            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch(IOException e){
            System.out.println("Error: " + e);
            return;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (clientIn != null) {
                    clientIn.close();
                }
                if (clientOut != null) {
                    clientOut.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (reducerSocket != null) {
                    reducerSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}