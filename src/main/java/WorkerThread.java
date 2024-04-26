import entities.Request;
import entities.Response;
import entities.Room;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

                // Set the action of the response
                response.setAction("Add Room");
                message = addManagerRoom(request);

                // check the message value and send the according response
                if (message.equals("Room added")){
                    response.setResponse("Room(s) added");
                    System.out.println("Room(s) added");
                } else {
                    response.setResponse("Room(s) already exists");
                    System.out.println("Room(s) already exists");
                }
            }
            else if (request.getAction().equals("Show Owned Rooms")){

                // Set the synchronized boolean to true to avoid concurrency issues
                if (!(Worker.synchronizeWorkers)){
                    Worker.synchronizeWorkers = true;
                    Reducer.activeWorkers = Master.workers.size() + 3;
                    System.out.println("Active Workers: " + Reducer.activeWorkers);
                }

                // Set the action of the response
                response.setAction("Show Owned Rooms");

                // Get all rooms from Worker's instance
                List<Room> rooms = getRooms();

                // Check if rooms is empty
                if(rooms.isEmpty()){
                    response.setResponse("No rooms found");
                    System.out.println("No rooms found on this Worker Instance");
                }
                else {
                    System.out.println("Room(s) found on this Worker Instance");
                    // Create a list of Rooms to send to Reducer
                    List<Room> responseRooms = new ArrayList<>();
                    for(Room room : rooms){

                        // check if in this Room the manager is the same as the Request's username
                        if (room.getManagerUsername().equals(request.getUsername())) {

                            // add the room to the list of Rooms
                            responseRooms.add(room);
                            System.out.println("Room added to response");
                        }
                    }
                    // Check if responseRooms is empty and set up the appropriate response
                    if (responseRooms.isEmpty()){
                        response.setResponse("No rooms found for this Manager");
                        System.out.println("No rooms found on this Worker Instance");
                    }
                    else {
                        response.setResponse("Rooms found");
                        response.setRooms(responseRooms);
                        System.out.println("Rooms found on this Worker Instance");
                    }
                }
                Thread.sleep(1000);
            }
            else if (request.getAction().equals("Add Room Availability Date")){

                // Set the synchronized boolean to true to avoid concurrency issues
                if (!(Worker.synchronizeWorkers)){
                    Worker.synchronizeWorkers = true;
                }

                // Set the action of the response
                response.setAction("Add Room Availability Date");

                // Get all rooms from Worker's instance
                List<Room> rooms = getRooms();

                // Check if rooms is empty
                if (rooms.isEmpty()){
                    response.setResponse("No rooms found");
                    System.out.println("No rooms found on this Worker Instance Here?");
                }
                else {
                    System.out.println("Room(s) found on this Worker Instance");
                    // Create a list of Rooms to send to Reducer
                    List<Room> responseRooms = new ArrayList<>();
                    for (Room room : rooms) {

                        // check if in this Room the roomName is the same as the Request's roomName
                        if (room.getRoomName().equals(request.getRoom().getRoomName())) {

                            // add the available dates to the room
                            room.insertAvailableDates(request.getRoom().getAvailableDates());
                            responseRooms.add(room);
                            System.out.println("Room added to response");
                        }
                    }
                    // Check if responseRooms is empty and set up the appropriate response
                    if (responseRooms.isEmpty()) {
                        response.setResponse("No rooms found for this Manager");
                        System.out.println("No rooms found on this Worker Instance");
                    } else {
                        response.setResponse("Room availability date added");
                        response.setRooms(responseRooms);
                        System.out.println("Rooms found on this Worker Instance");
                    }
                }
            }

            // Connect to Reducer and send data
            Socket reducerSocket = new Socket("localhost", 9092);
            ObjectOutputStream reducerOut = new ObjectOutputStream(reducerSocket.getOutputStream());
            ObjectInputStream reducerIn = new ObjectInputStream(reducerSocket.getInputStream());
            reducerOut.writeObject(response);
            reducerOut.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String addManagerRoom(Request request) {

        // Get all rooms of Worker's instance
        // and check if the room is already in the list
        for(Room room : workerInstance.getRooms()){
            if(room.getRoomId() == request.getRoom().getRoomId()){
                return "Room already exists";
            }
        }

        // Add room from request to Worker's room list
        workerInstance.addRoom(request.getRoom());
        return "Room added";
    }

    private synchronized List<Room> getRooms(){

        // Get all rooms of Worker's instance
        List<Room> roomList = workerInstance.getRooms();
        return roomList;
    }
}
