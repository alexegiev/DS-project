import entities.Request;
import entities.Response;
import entities.Room;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLOutput;

public class WorkerThread extends Thread{

    Socket masterSocket;
    Worker workerInstance;

    // MAYBE WE WILL NOT NEED THIS CONSTRUCTOR
    public WorkerThread(Socket masterSocket){
        this.masterSocket = masterSocket;
    }

    // this constructor is used mainly when WorkerThread needs to access
    // some data from the Worker instance that created it
    public WorkerThread(Socket masterSocket, Worker workerInstance){
        this.masterSocket = masterSocket;
        this.workerInstance = workerInstance;
    }

    public void run() {
        // Process data from Master
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        System.out.println("WorkerThread started");

        try {

            out = new ObjectOutputStream(masterSocket.getOutputStream());
            in = new ObjectInputStream(masterSocket.getInputStream());

            // Receive data from Master
            Request request = (Request) in.readObject();

            // Check Request.Action and perform the appropriate action

            String message;
            Response response = new Response();
            if(request.getAction().equals("Add Room")){
                message = addManagerRoom(request);
                if (message.equals("Room added")){
                    response.setResponse("Room added");
                    System.out.println("Room added");
                } else {
                    response.setResponse("Room already exists");
                    System.out.println("Room already exists");
                }
            }

//            out.writeObject(response);
//            out.flush();

            // Connect to Reducer and send data
            Socket reducerSocket = new Socket("localhost", 9092);
            ObjectOutputStream reducerOut = new ObjectOutputStream(reducerSocket.getOutputStream());
            ObjectInputStream reducerIn = new ObjectInputStream(reducerSocket.getInputStream());
            Response sendResponse = new Response();
            sendResponse.setResponse(response.getResponse());
            reducerOut.writeObject(sendResponse);
            reducerOut.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String addManagerRoom(Request request) {

        // Get all rooms of Worker's instance
        // and check if the room is already in the list
        for(Room room : workerInstance.getRooms()){
            if(room.equals(request.getRoom())){
                return "Room already exists";
            }
        }

        // Add room from request to Worker's room list
        workerInstance.addRoom(request.getRoom());
        return "Room added";
    }
}
