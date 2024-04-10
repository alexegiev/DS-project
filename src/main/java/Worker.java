import entities.Room;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Worker {

    //fields
    ServerSocket workerSocket = null;
    InetAddress workerIp = null;
    int workerPort;
    ObjectInputStream in;
    ObjectOutputStream out;

    private int sleepTime;
    private static final Object lock = new Object();

    static private int masterCount = 1;

    public static void main(String args[]) {

        // boolean used to check where this worker is tested
        // true: In lab
        // false: In local
        boolean isLab = false;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Is Worker initiated in Lab? (1:Yes / 2:No )");
        int choice = scanner.nextInt();
        if (choice == 1) {
            isLab = true;
        } else if (choice == 2) {
            isLab = false;
        } else {
            System.out.println("Invalid choice");
            return;
        }

        if(!isLab){
            System.out.print("Please provide a port number: ");

            while (!scanner.hasNextInt()) {
                System.out.println("That's not a number! Please enter a number: ");
                scanner.next(); // discard the non-integer input
            }

            int port = scanner.nextInt();


            new Worker().startWorkerInLocal(port);
        }
        else {
            new Worker().startWorkerInLab();
        }


    }

    private void startWorkerInLab() {
        try {
            //Get worker's IP
            this.workerIp = InetAddress.getLocalHost();
            this.workerIp = InetAddress.getByName(workerIp.getHostAddress());

            // Create a new ServerSocket object
            this.workerSocket = new ServerSocket(9091);
            this.workerPort = workerSocket.getLocalPort();
            System.out.println("Worker started at: " + workerIp + ":" + workerPort);

            // Send the IP and Port of Worker to Master
            sendToMaster(workerIp, workerPort);

            // Wait for Master
            waitForMaster();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startWorkerInLocal(int port) {

        try {
            //Get worker's IP
            this.workerIp = InetAddress.getLocalHost();
            this.workerIp = InetAddress.getByName(workerIp.getHostAddress());

            // Create a new ServerSocket object
            this.workerSocket = new ServerSocket(port);
            this.workerPort = workerSocket.getLocalPort();
            System.out.println("Worker started at: " + workerIp + ":" + workerPort);

            // Send the IP and Port of Worker to Master
            sendToMaster(workerIp, workerPort);

            // Wait for Master
            waitForMaster();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToMaster(InetAddress workerIp, int workerPort) {

        Socket masterSocket = null;
        try {
            // Connect to Master CHANGE "localhost" to Master's IP address
            masterSocket = new Socket("localhost", 9095);
            out = new ObjectOutputStream(masterSocket.getOutputStream());
            in = new ObjectInputStream(masterSocket.getInputStream());

            // Send IP and Port to Master
            out.writeObject(workerIp);
            out.flush();
            out.writeObject(workerPort);
            out.flush();

            System.out.println("Worker's IP and Port sent to Master");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (masterSocket != null) {
                    masterSocket.close();
                    System.out.println("Connection to Master closed");
                }
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    private void waitForMaster() {
        while (true) {
            try {
                System.out.println("Waiting for Master request");
                Socket masterSocket = workerSocket.accept();
                System.out.println("Connected to Master: " + masterCount + " Timers");
                masterCount++;

                // create a new WorkerThread object
                WorkerThread workerThread = new WorkerThread(masterSocket);
                workerThread.start();

            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    public void run() {
        try {
            // change values ONLY FOR TESTING
            Room testingRoom = (Room) in.readObject();
            Thread.sleep(sleepTime);

            // Connect to Reducer and send data
            Socket reducerSocket = new Socket("localhost", 9092);
            ObjectOutputStream reducerOut = new ObjectOutputStream(reducerSocket.getOutputStream());
            reducerOut.writeObject(testingRoom);
            reducerOut.flush();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        finally {
//            try {
//                in.close();
//                out.close();
//            } catch (IOException e) {
//                System.out.println("Error: " + e);
//            }
//        }
    }

    private void storeInMemory(Room room) {
        // Create an ArrayList to store Room objects
        List<Room> roomList = new ArrayList<>();
        // Store the room object in the list
        roomList.add(room);
        System.out.println("Room information stored in memory: " + room);
    }
}