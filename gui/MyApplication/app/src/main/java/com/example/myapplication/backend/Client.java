package com.example.myapplication.backend;

import com.example.myapplication.backend.entities.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Client{

    //private fields

    // List of Managers and Renters of the systems
    static List<String> managers = Arrays.asList("manager1", "manager2", "manager3");
    static List<String> renters = Arrays.asList("renter1", "renter2", "renter3");
    static int requestId = 0;

    private Request request;

    static boolean roomsInitialized = false;

    public Client() {
//        if (!roomsInitialized) {
//            roomsInitialized = true;
//
//            // Create a new ClientThread object
//            ClientThread clientThread = null;
//
//            Manager dummyManager = new Manager("dummyManager");
//            // Get Manager's Rooms from JSON
//            List<Room> rooms = dummyManager.addRoomsFromJson("preLoadedRooms");
//
//            // For each room, create a ClientThread object which will handle the request
//            for (Room room : rooms) {
//                requestId++;
//                dummyManager.addRequestId(requestId);
//                clientThread = new ClientThread(new Request(requestId, "Add Room", room));
//                clientThread.start();
//            }
//        }
    }

    private String login(){

        // ask Client if is Manager or Renter
        System.out.println("Are you a Manager or a Renter? (1: Manager, 2: Renter)");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        // validate input
        while (choice != 1 && choice != 2) {
            System.out.println("Invalid choice. Please enter 1 or 2");
            choice = scanner.nextInt();
        }

        if (choice == 1) {
            return "Manager";
        } else {
            return "Renter";
        }
    }

    public boolean validate(String role, String username){
        Scanner scanner = new Scanner(System.in);
        if (role.equals("Manager")) {
            if (managers.contains(username)) {
                System.out.println("Welcome " + username + " !");
                return true;
            }
            System.out.println("Invalid username. Please enter a valid username.");
            return false;
        }
        else {
            if (renters.contains(username)) {
                System.out.println("Welcome " + username + " !");
                return true;
            }
            System.out.println("Invalid username. Please enter a valid username.");
            return false;
        }
    }
    public void addRequestId(int requestId){
        this.requestId = requestId;
    }

    public void addRequestAction(String action){
        this.request.setAction(action);
    }

    public void addRequestFilterType(String filterType){
        this.request.setFilterType(filterType);
    }

    public void addRequestFilterValue(String filterValue){
        this.request.setFilterValue(filterValue);
    }

    public void setRequest(Request request) {
        this.request = request;
    }


    public Response searchRoom(String filter, String userInputText){
        Request request = new Request("Search Room");
        // Set the Request object in the Client
        this.setRequest(request);
        // Now you can add the filter type and value to the Request
        this.addRequestFilterType(filter);
        this.addRequestFilterValue(userInputText);
        this.addRequestAction("Search Room");
        requestId++;
        request.setRequestId(requestId);


        // Print the request before it is sent
        System.out.println("Request: " + request);

        // Run the ClientThread to send the request and receive the response
        ClientThread clientThread = new ClientThread(request);
        clientThread.start();

        // Wait for the ClientThread to finish
        try {
            clientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        // Print the response after it is received
        System.out.println("Response: " + clientThread.getRooms());

        // Return the received response
        return clientThread.getRooms();

    }



//DONT CARE ABOUT THESE METHODS
//    private static void renterActions(String username) {
//
//        Scanner action = new Scanner(System.in);
//        int choice;
//
//        do {
//            // show available actions for renter
//            System.out.println("Choose available actions: " +
//                    "\n1. Search Room" +
//                    "\n2. Book Room" +
//                    "\n3. Add Rating" +
//                    "\n4. Logout");
//
//            choice = action.nextInt();
//            switch (choice) {
//                case 1:
//                    // Search Room
//
//                    // Create new Scanner object
//                    Scanner select = new Scanner(System.in);
//
//                    System.out.println("Use filter: " +
//                            "\n1. Room Name" +
//                            "\n2. Room Area" +
//                            "\n3. Availability Dates" +
//                            "\n4. People capacity" +
//                            "\n5. Price" +
//                            "\n6. Rating");
//
//                    int filter;
//                    System.out.print("Enter the value for the filter: ");
//
//                    // Get the filter from the Renter
//                    filter = select.nextInt();
//
//                    // Check if filter is valid input or not a number
//                    while (filter < 1 || filter > 6) {
//                        System.out.println("Invalid choice. Please enter a valid choice.");
//                        filter = select.nextInt();
//                    }
//
//                    // Check filterType and ask for the desired filterValue
//                    String filterType = "";
//                    String filterValue = "";
//
//                    switch (filter) {
//                        case 1:
//                            filterType = "Room Name";
//                            System.out.print("Enter Room Name: ");
//                            filterValue = select.next();
//                            break;
//                        case 2:
//                            filterType = "Room Area";
//                            System.out.print("Enter Room Area: ");
//                            filterValue = select.next();
//                            break;
//                        case 3:
//                            filterType = "Availability Dates";
//                            System.out.print("Enter From Date (yyyy-MM-dd): ");
//                            String fromDate = select.next();
//                            System.out.print("Enter To Date (yyyy-MM-dd): ");
//                            String toDate = select.next();
//                            filterValue = fromDate + " - " + toDate;
//                            break;
//                        case 4:
//                            filterType = "People Capacity";
//                            System.out.print("Enter People capacity: ");
//                            filterValue = select.next();
//                            break;
//                        case 5:
//                            filterType = "Room Price";
//                            System.out.print("Enter Price: ");
//                            filterValue = select.next();
//                            break;
//                        case 6:
//                            filterType = "Rating";
//                            System.out.print("Enter Rating: ");
//                            filterValue = select.next();
//                            break;
//                    }
//
//                    // Create a new Request object with the given parameters
//                    Request request = new Request("Search Room");
//                    request.setFilterType(filterType);
//                    request.setFilterValue(filterValue);
//
//                    // Increase the requestId
//                    requestId++;
//
//                    // Add the requestId to the request object
//                    request.setRequestId(requestId);
//
//                    // Create a new ClientThread object with the given request
//                    ClientThread clientThread = new ClientThread(request);
//                    clientThread.start();
//                    break;
//                case 2:
//                    // Book Room
//
//                    // Create new Scanner object
//                    Scanner selectRoom = new Scanner(System.in);
//
//                    // Get Room Name from Renter
//                    System.out.println("Enter Room Name: ");
//                    String roomName = selectRoom.next();
//
//                    // Get from Renter the From date and parse it to a Date object
//                    System.out.println("Enter From Date (yyyy-MM-dd): ");
//                    String fromDate = selectRoom.next();
//                    Date from = Date.valueOf(fromDate);
//
//                    // Get from Renter the To date and parse it to a Date object
//                    System.out.println("Enter To Date (yyyy-MM-dd): ");
//                    String toDate = selectRoom.next();
//                    Date to = Date.valueOf(toDate);
//
//                    // Create a new Room object with the given parameters
//                    Room roomToBook = new Room();
//                    roomToBook.setRoomName(roomName);
//                    roomToBook.setFrom(from);
//                    roomToBook.setTo(to);
//                    roomToBook.setRenterUsername(username);
//
//                    // Increase the requestId
//                    requestId++;
//
//                    // Create a new Request object with the given parameters
//                    Request requestToBook = new Request(requestId, "Book Room", roomToBook);
//
//                    // Create a new ClientThread object with the given request
//                    ClientThread clientThreadToBook = new ClientThread(requestToBook);
//                    clientThreadToBook.start();
//                    break;
//                case 3:
//                    // Add Rating
//
//                    // Create new Scanner object
//                    Scanner selectRating = new Scanner(System.in);
//
//                    // Get Room Name from Renter
//                    System.out.println("Enter Room Name: ");
//                    String roomNameRating = selectRating.next();
//
//                    // Get Rating from Renter
//                    System.out.println("Enter Rating: ");
//                    double rating = selectRating.nextDouble();
//
//                    // Create a new Room object with the given parameters
//                    Room roomToAddRating = new Room();
//                    roomToAddRating.setRoomName(roomNameRating);
//                    roomToAddRating.setRatingToAdd(rating);
//
//                    // Increase the requestId
//                    requestId++;
//
//                    // Create a new Request object with the given parameters
//                    Request requestToAddRating = new Request(requestId, "Add Rating", roomToAddRating);
//
//                    // Create a new ClientThread object with the given request
//                    ClientThread clientThreadToAddRating = new ClientThread(requestToAddRating);
//                    clientThreadToAddRating.start();
//
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please enter a valid choice.");
//                    break;
//            }
//        } while (choice != 4);
//
//        System.out.println("Logged out successfully.");
//    }

    private static void managerActions(String username) {

        Manager manager = new Manager(username);
        // show available actions for manager
        System.out.println("Choose available actions: " +
                "\n1. Add Room" +
                "\n2. Add Room Availability Date" +
                "\n3. Show Owned Rooms" +
                "\n4. Logout");

        Scanner action = new Scanner(System.in);
        int choice = action.nextInt();
        while (choice != 4) {
            ClientThread clientThread = null;

            switch (choice) {
                case 1:
                    // Get Manager's Rooms from JSON
                    List<Room> rooms = manager.addRoomsFromJson(username);

                    // For each room, create a ClientThread object which will handle the request
                    for (Room room : rooms) {
                        requestId++;
                        manager.addRequestId(requestId);
                        clientThread = new ClientThread(new Request(requestId, "Add Room", room));
                        clientThread.start();
                    }
                    break;

                case 2:
                    // Add Room Availability Date

                    // Create new Scanner
                    Scanner select = new Scanner(System.in);
                    // Get Room Name from Manager
                    System.out.println("Enter Room Name: ");
                    String roomName = select.next();

                    // Get from Manager the From date and parse it to a Date object
                    System.out.println("Enter From Date (yyyy-MM-dd): ");
                    String fromDate = select.next();
                    Date from = Date.valueOf(fromDate);

                    // Get from Manager the To date and parse it to a Date object
                    System.out.println("Enter To Date (yyyy-MM-dd): ");
                    String toDate = select.next();
                    Date to = Date.valueOf(toDate);

                    // Create a new Room object with the given parameters
                    Room roomToUpdate = new Room();
                    roomToUpdate.setRoomName(roomName);
                    roomToUpdate.addAvailableDates(from, to);
                    roomToUpdate.setManagerUsername(username);

                    // Increase the requestId
                    requestId++;
                    manager.addRequestId(requestId);
                    // Create a new Request object with the given parameters
                    Request requestToUpdate = new Request(requestId, "Add Room Availability Date", roomToUpdate);

                    clientThread = new ClientThread(requestToUpdate);
                    clientThread.start();
                    break;

                case 3:
                    // Show Owned Rooms
                    Request request = manager.showOwnedRooms(username);
                    requestId++;
                    manager.addRequestId(requestId);
                    // Add the requestId to the request object
                    request.setRequestId(requestId);

                    // Set the request Action
                    request.setAction("Show Owned Rooms");

                    clientThread = new ClientThread(request);
                    clientThread.start();
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid choice.");
                    break;
            }
            choice = action.nextInt();
        }
        // Send a request to the Master to close the connection
        Request request = new Request("Logout");
        request.setRequestIds(manager.getRequestIds());
        ClientThread clientThread = new ClientThread(request);
        clientThread.start();
        System.out.println("Logged out successfully.");
    }

//    public static void main(String args[]){
//
//        //Login
//        Client client = new Client();
//        String role = client.login();
//        String username = client.validate(role);
//
//        // check client's role and call the appropriate method
//        if (role.equals("Manager")) {
//            managerActions(username);
//        } else {
//            renterActions(username);
//        }
//
//    }
}