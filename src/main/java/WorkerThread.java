import entities.Request;
import entities.Response;
import entities.Room;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            response.setRequestId(request.getRequestId());

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
            else if (request.getAction().equals("Search Room")){

                // Set the synchronized boolean to true to avoid concurrency issues
                if (!(Worker.synchronizeWorkers)){
                    Worker.synchronizeWorkers = true;
                }

                // Set the action of the response
                response.setAction("Search Room");

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

                    if (request.getFilterType().equals("Room Name")){
                        for (Room room : rooms) {
                            // check if in this Room the roomName is the same as the Request's roomName
                            if (room.getRoomName().equals(request.getFilterValue())) {
                                responseRooms.add(room);
                                System.out.println("Room added to response");
                            }
                        }
                    }
                    else if (request.getFilterType().equals("Room Area")){
                        for (Room room : rooms) {
                            // check if in this Room the roomLocation is the same as the Request's roomArea
                            if (room.getArea().equals(request.getFilterValue())) {
                                responseRooms.add(room);
                                System.out.println("Room added to response");
                            }
                        }
                    }
                    else if (request.getFilterType().equals("Availability Dates")){
                        for (Room room : rooms) {
                            // check if in this Room the roomAvailabilityDate is the same as the Request.filterValue

                            // Parse the filterValue to Date
                            String filterDate = request.getFilterValue();
                            String[] dates = filterDate.split(" - ");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                            Date startDate = format.parse(dates[0]);
                            Date endDate = format.parse(dates[1]);

                            // Find the room that has the filterDate in the available dates
                            if (filterDate != null) {
                                for (Map.Entry<Date, Date> entry : room.getAvailableDates().entrySet()) {
                                    Date startDateRoom = entry.getKey();
                                    Date endDateRoom = entry.getValue();
                                    if (startDateRoom.equals(startDate) && endDateRoom.equals(endDate)) {
                                        // filterDate is equal to the startDate and  endDate
                                        System.out.println("Date matches with available dates");
                                        responseRooms.add(room);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else if (request.getFilterType().equals("People Capacity")){
                        for (Room room : rooms) {
                            // check if in this Room the roomCapacity is the same as the Request's roomCapacity
                            System.out.println(room.getCapacity() + " " + Integer.parseInt(request.getFilterValue()));
                            if (room.getCapacity() == Integer.parseInt(request.getFilterValue())) {
                                responseRooms.add(room);
                                System.out.println("Room added to response");
                            }
                        }
                    }
                    else if (request.getFilterType().equals("Room Price")){
                        for (Room room : rooms) {
                            // check if in this Room the roomPrice is the same as the Request's roomPrice
                            if (room.getPrice() == Float.parseFloat(request.getFilterValue())) {
                                responseRooms.add(room);
                                System.out.println("Room added to response");
                            }
                        }
                    }
                    else if (request.getFilterType().equals("Rating")){
                        for (Room room : rooms) {
                            // check if in this Room the roomLocation is the same as the Request's roomLocation
                            if (room.getRating() == Float.parseFloat(request.getFilterValue())){
                                responseRooms.add(room);
                                System.out.println("Room added to response");
                            }
                        }
                    }
                    // Set the filter type
                    response.setFilterType(request.getFilterType());

                    // Set the filter value
                    response.setFilterValue(request.getFilterValue());

                    // Check for the Request.filterType for the Search filter
                    // Check if responseRooms is empty and set up the appropriate response
                    if (responseRooms.isEmpty()) {
                        System.out.println("No rooms found on this Worker Instance");
                    } else {
                        response.setRooms(responseRooms);
                        System.out.println("Rooms found on this Worker Instance");
                    }
                }

            }
            else if (request.getAction().equals("Book Room")){
                // Set the action of the response
                response.setAction("Book Room");

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
                            room.reserveDates(request.getRoom().getFrom(), request.getRoom().getTo());
                            responseRooms.add(room);
                            System.out.println("Room added to response");
                        }
                    }
                    // Check if responseRooms is empty and set up the appropriate response
                    if (responseRooms.isEmpty()) {
                        response.setResponse("No rooms found for this Manager");
                        System.out.println("No rooms found on this Worker Instance");
                    } else {
                        response.setResponse("Room booked");
                        response.setRooms(responseRooms);
                        System.out.println("Rooms found on this Worker Instance");
                    }
                }
            }
            else if (request.getAction().equals("Add Rating")){
                // Set the action of the response
                response.setAction("Add Rating");

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
                            int newNumberOfReviews = room.getNumberOfReviews() + 1;
                            room.setNumberOfReviews(newNumberOfReviews);
                            room.setRating((room.getRating() + request.getRoom().getRating()) / newNumberOfReviews);
                            responseRooms.add(room);
                            System.out.println("Room added to response");
                        }
                    }
                    // Check if responseRooms is empty and set up the appropriate response
                    if (responseRooms.isEmpty()) {
                        response.setResponse("No rooms found for this Manager");
                        System.out.println("No rooms found on this Worker Instance");
                    } else {
                        response.setResponse("Rating added");
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
