import entities.Room;

import java.io.*;
import java.net.*;

public class Worker {

    //fields
    ServerSocket worker = null;
    ObjectInputStream in;
    ObjectOutputStream out;

    private int sleepTime;
    private static final Object lock = new Object();

    static private int masterCount = 1;

    public static void main(String args[]) throws UnknownHostException {
        InetAddress workerIp = InetAddress.getLocalHost();
        System.out.println("Worker IP: " + workerIp.getHostAddress());

        //TODO: Send the ip address to Master, so that he can know the Worker's IP for the future

        new Worker().startWorker();
    }

    public void startWorker() {

        try {
            worker = new ServerSocket(9091);
            System.out.println("Worker started at port: " + worker.getLocalPort());
            waitForMaster();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void waitForMaster() {
        while (true) {
            try {
                System.out.println("Waiting for Master request");
                Socket masterSocket = worker.accept();
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