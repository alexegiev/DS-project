import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import entities.Manager;
import entities.Request;
import entities.Room;

public class Client{

    //private fields

    // List of Managers and Renters of the systems
    static List<String> managers = Arrays.asList("manager1", "manager2", "manager3");
    static List<String> renters = Arrays.asList("renter1", "renter2", "renter3");
    static int requestId = 0;

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

    private String validate(String role){
        String username;
        Scanner scanner = new Scanner(System.in);
        if (role.equals("Manager")) {
            System.out.println("Enter Manager Username: ");
            username = scanner.next();
            while(true) {
                if (managers.contains(username)) {
                    System.out.println("Welcome " + username + " !");
                    break;
                }
                System.out.println("Invalid username. Please enter a valid username.");
                username = scanner.next();
            }
        }
        else {
            System.out.println("Enter Renter Username: ");
            username = scanner.next();
            while(true) {
                if (renters.contains(username)) {
                    System.out.println("Welcome " + username + " !");
                    break;
                }
                System.out.println("Invalid username. Please enter a valid username.");
                username = scanner.next();
            }
        }
        return username;
    }

    private static void managerActions(String username) {

        Manager manager = new Manager(username);
        // show available actions for manager
        System.out.println("Choose available actions: " +
                "\n1. Add Room" +
                "\n2. Add Room Availability Date" +
                "\n3. Show Owned Rooms" +
                "\n4. Logout");

        //TODO: Implement the actions
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
        try {
            clientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void renterActions(String username) {

        Scanner action = new Scanner(System.in);
        int choice;

        do {
            // show available actions for renter
            System.out.println("Choose available actions: " +
                    "\n1. Search Room" +
                    "\n2. Book Room" +
                    "\n3. Show Booked Rooms" +
                    "\n4. Logout");

            //TODO: Implement the actions
            choice = action.nextInt();
            switch (choice) {
                case 1:
                    // Search Room

                    // Create new Scanner object
                    Scanner select = new Scanner(System.in);

                    System.out.println("Use filter: " +
                            "\n1. Room Name" +
                            "\n2. Room Area" +
                            "\n3. Availability Dates" +
                            "\n4. People capacity" +
                            "\n5. Price" +
                            "\n6. Rating");

                    int filter;
                    System.out.print("Enter the value for the filter: ");

                    // Get the filter from the Renter
                    filter = select.nextInt();

                    // Check if filter is valid input or not a number
                    while (filter < 1 || filter > 6) {
                        System.out.println("Invalid choice. Please enter a valid choice.");
                        filter = select.nextInt();
                    }

                    // Check filterType and ask for the desired filterValue
                    String filterType = "";
                    String filterValue = "";

                    switch (filter) {
                        case 1:
                            filterType = "Room Name";
                            System.out.print("Enter Room Name: ");
                            filterValue = select.next();
                            break;
                        case 2:
                            filterType = "Room Area";
                            System.out.print("Enter Room Area: ");
                            filterValue = select.next();
                            break;
                        case 3:
                            filterType = "Availability Dates";
                            System.out.print("Enter From Date (yyyy-MM-dd): ");
                            String fromDate = select.next();
                            System.out.print("Enter To Date (yyyy-MM-dd): ");
                            String toDate = select.next();
                            filterValue = fromDate + " - " + toDate;
                            break;
                        case 4:
                            filterType = "People capacity";
                            System.out.print("Enter People capacity: ");
                            filterValue = select.next();
                            break;
                        case 5:
                            filterType = "Price";
                            System.out.print("Enter Price: ");
                            filterValue = select.next();
                            break;
                        case 6:
                            filterType = "Rating";
                            System.out.print("Enter Rating: ");
                            filterValue = select.next();
                            break;
                    }

                    // Create a new Request object with the given parameters
                    Request request = new Request("Search Room");
                    request.setFilterType(filterType);
                    request.setFilterValue(filterValue);

                    // Increase the requestId
                    requestId++;

                    // Add the requestId to the request object
                    request.setRequestId(requestId);

                    // Create a new ClientThread object with the given request
                    ClientThread clientThread = new ClientThread(request);
                    clientThread.start();
                    break;
                case 2:
                    // Book Room
                    break;
                case 3:
                    // Show Booked Rooms
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid choice.");
                    break;
            }
        } while (choice != 4);

        System.out.println("Logged out successfully.");
    }



    public static void main(String args[]){

        //Login
        Client client = new Client();
        String role = client.login();
        String username = client.validate(role);

        // check client's role and call the appropriate method
        if (role.equals("Manager")) {
            managerActions(username);
        } else {
            renterActions(username);
        }

    }
}