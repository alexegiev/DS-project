import entities.Request;
import entities.Response;
import entities.Room;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ReducerThread extends Thread {

    //fields

    Socket workerSocket;

    public ReducerThread(Socket workerSocket) {
        this.workerSocket = workerSocket;
    }

    public void run(){

        // Process data from Worker
        ObjectOutputStream out;
        ObjectInputStream in;

        try{
            out = new ObjectOutputStream(workerSocket.getOutputStream());
            in = new ObjectInputStream(workerSocket.getInputStream());

            Response responseFromWorker = (Response) in.readObject();

            // Connect to ServerThread
            Socket serverSocket = new Socket("localhost", 9093);

            // Create new Response object to send to ServerThread
            Response response = new Response();

            // Create the appropriate Response to send back to the ServerThread
            if (responseFromWorker.getAction().equals("Add Room")){
                response.setResponse("Room added");
            }
            else if (responseFromWorker.getAction().equals("Show Owned Rooms")){
                // Check if the list in responceFromWorker is empty
                if (responseFromWorker.getRooms() == null || responseFromWorker.getRooms().isEmpty()){
                    response.setResponse("No rooms owned by Manager");
                }
                else{
                    // Create a list of Rooms to send to ServerThread
                    response.setResponse("Rooms owned by Manager: " + "\n" + responseFromWorker.getRoomsString());
                }

            }
            ObjectOutputStream serverOut = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream serverIn = new ObjectInputStream(serverSocket.getInputStream());
            serverOut.writeObject(response);
            serverOut.flush();

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }
}
