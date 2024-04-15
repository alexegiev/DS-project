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

        // show available actions for renter
        System.out.println("Choose available actions: " +
                "\n1. Search Room" +
                "\n2. Book Room" +
                "\n3. Show Booked Rooms" +
                "\n4. Logout");

        //TODO: Implement the actions
        Scanner action = new Scanner(System.in);
        int choice = action.nextInt();
        while (choice != 4) {
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
                default:
                    System.out.println("Invalid choice. Please enter a valid choice.");
                    break;
            }
            choice = action.nextInt();
        }
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