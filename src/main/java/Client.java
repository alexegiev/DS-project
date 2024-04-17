import java.sql.Date;
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
    static int requestId = 1;

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
            switch (choice) {
                case 1:
                    // Get Manager's Rooms from JSON
                    List<Room> rooms = manager.addRoomsFromJson(username);

                    // For each room, create a ClientThread object which will handle the request
                    for (Room room : rooms) {
                        requestId++;
                        new ClientThread(new Request(requestId, "Add Room", room)).start();
                    }
                    break;

                case 2:
                    // Add Room Availability Date

                    // Get Room Name from Manager
                    System.out.println("Enter Room Name: ");
                    String roomName = action.next();

                    // Get from Manager the From date and parse it to a Date object
                    System.out.println("Enter From Date (yyyy-MM-dd): ");
                    String fromDate = action.next();
                    Date from = Date.valueOf(fromDate);

                    // Get from Manager the To date and parse it to a Date object
                    System.out.println("Enter To Date (yyyy-MM-dd): ");
                    String toDate = action.next();
                    Date to = Date.valueOf(toDate);

                    // Create a new Room object with the given parameters
                    Room roomToUpdate = new Room();
                    roomToUpdate.setRoomName(roomName);
                    roomToUpdate.addAvailableDates(from, to);
                    roomToUpdate.setManagerUsername(username);

                    // Increase the requestId
                    requestId++;

                    // Create a new Request object with the given parameters
                    Request requestToUpdate = new Request(requestId, "Add Room Availability Date", roomToUpdate);

                    // Create a new ClientThread object which will handle the request
                    new ClientThread(requestToUpdate).start();
                    break;

                case 3:
                    // Show Owned Rooms
                    Request request = manager.showOwnedRooms(username);
                    requestId++;

                    // Add the requestId to the request object
                    request.setRequestId(requestId);

                    // Set the request Action
                    request.setAction("Show Owned Rooms");

                    // Create a new ClientThread object which will handle the request
                    new ClientThread(request).start();
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid choice.");
                    break;
            }
            choice = action.nextInt();
        }
        System.out.println("Logged out successfully.");
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
                    "\n4. Rate Room" +
                    "\n5. Logout");

            //TODO: Implement the actions
            choice = action.nextInt();
            switch (choice) {
                case 1:
                    // Search Room
                    break;
                case 2:
                    // Book Room
                    break;
                case 3:
                    // Show Booked Rooms
                    break;
                case 4:
                    // Add Room Rating

                    // Get Room Name
                    System.out.println("Enter Room Name: ");
                    String roomName = action.next();
                    // Get from Renter the Rating number and parse it to a Rating object
                    System.out.println("Enter your Rating for the room ");
                    String Rating = action.next();
                    Double Rate = Double.valueOf(Rating);

                    // Create a new Room object with the given parameters
                    Room roomToUpdate = new Room();
                    roomToUpdate.setRoomName(roomName);
                    roomToUpdate.setRating(Rate);
                    // Increase the requestId
                    requestId++;

                    // Create a new Request object with the given parameters
                    Request requestToUpdate = new Request(requestId, "Add Rating", roomToUpdate);

                    // Create a new ClientThread object which will handle the request
                    new ClientThread(requestToUpdate).start();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid choice.");
                    break;
            }
        } while (choice != 5);

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

        //TESTING
        //new Client(new Request(1,new Room("room1",1,1,"area1",1.5,5, "asdasdas"))).start();
    }
}