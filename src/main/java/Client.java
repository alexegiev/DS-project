import java.io.*;
import java.lang.ref.Cleaner;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import entities.Manager;
import entities.Request;
import entities.Room;

public class Client extends Thread{

    //private fields

    Request request;

    // List of Managers and Renters of the systems
    static List<String> managers = Arrays.asList("manager1", "manager2", "manager3");
    static List<String> renters = Arrays.asList("renter1", "renter2", "renter3");

    // Constructors

    public Client() {
    }

    Client(Request request) {
        this.request = request;
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

        Manager manager = new Manager();
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
                    // Add Room
                    manager.addRoomsFromJson(username);
                    break;
                case 2:
                    // Add Room Availability Date
                    break;
                case 3:
                    // Show Owned Rooms
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

    public void run() {

        //Initialize socket and input/output streams
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            // create a socket to connect to the server on port 9090
            requestSocket = new Socket("localhost", 9090);

            // create the streams to send and receive data from server
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            //write object Request
            out.writeObject(request);
            out.flush();

//            // write object Room
//            out.writeObject(request);
//            out.flush();

            // Wait and receive data from Master
            Request request1 = (Request) in.readObject();

//            // print the received results (from Room.toString())
//            System.out.println("Room: " + room.toString());

            //print the received results (from Request.toString())
            System.out.println("Request: " + request1.toString());

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");

        } catch (SocketException se) {
            System.err.println("Socket was closed unexpectedly!");

        } catch (IOException ioException) {
            ioException.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (requestSocket != null) {
                    requestSocket.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
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

        //TODO: Form the appropriate Request object


        //TESTING
        //new Client(new Request(1,new Room("room1",1,1,"area1",1.5,5, "asdasdas"))).start();
    }
}