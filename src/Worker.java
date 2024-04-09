import entities.Room;

import java.io.*;
import java.net.*;

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
        if (args.length < 1) {
            System.out.println("Please provide a port number as a command-line argument.");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number. Please provide a valid port number as a command-line argument.");
            return;
        }

        new Worker().startWorker(port);

    }

    public void startWorker(int port ) {

        try {
            //Get worker's IP
            this.workerIp = InetAddress.getLocalHost();
            this.workerIp = InetAddress.getByName(workerIp.getHostAddress());
            System.out.println("Worker's IP: " + workerIp);

            // Create a new ServerSocket object
            this.workerSocket = new ServerSocket(port);
            this.workerPort = workerSocket.getLocalPort();
            System.out.println("Worker started at port: " + workerPort);

            // Send the IP and Port of Worker to Master
            sendToMaster(workerIp, workerPort);

            // Wait for Master
            waitForMaster();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToMaster(InetAddress workerIp, int workerPort) {

        try {
            // Connect to Master
            Socket masterSocket = new Socket("localhost", 9095);
            out = new ObjectOutputStream(masterSocket.getOutputStream());
            in = new ObjectInputStream(masterSocket.getInputStream());

            // Send IP and Port to Master
            out.writeObject(workerIp);
            out.writeObject(workerPort);
            out.flush();

            System.out.println("Worker's IP and Port sent to Master");

        } catch (IOException e) {
            throw new RuntimeException(e);
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
}