import entities.Response;
import entities.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Reducer {

    ServerSocket reducer = null;
    public static int activeWorkers = 0;

    public static List<Room> syncRooms = new ArrayList<>();
    public static Response syncResponse = new Response();

    public static List<WorkerThread> workerThreads = new ArrayList<>();

    private static int workers; // used for resetting

    public static void main(String[] args) {
        new Reducer();
    }

    public Reducer() {

        ObjectOutputStream outMaster = null;
        ObjectInputStream inMaster = null;

        try {
            reducer = new ServerSocket(9092);
            System.out.println("Reducer started at port: " + reducer.getLocalPort());

            // Connect to Master on port 9099
            // CHANGE "localhost" to Master's IP address
            Socket masterSocket = new Socket("localhost", 9099);
            System.out.println("Connected to Master");

            // Get from Master the number of Workers
            outMaster = new ObjectOutputStream(masterSocket.getOutputStream());
            inMaster = new ObjectInputStream(masterSocket.getInputStream());

            // Send a request to Master
            outMaster.writeObject("Send number of Workers");

            // Receive the number of Workers and set the number of activeWorkers
            activeWorkers = (int) inMaster.readObject();
            workers = activeWorkers;

            System.out.println("Number of Workers: " + activeWorkers);

            // Close the connection with Master
            masterSocket.close();

            // Start the Reducer
            new Thread(this::waitForWorker).start();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitForWorker() {

        while (true) {
            try {
                // Waiting for Worker request
                System.out.println("Waiting for Worker request");
                Socket workerSocket = reducer.accept();

                // create a new WorkerThread object
                ReducerThread reducerThread = new ReducerThread(workerSocket);
                reducerThread.start();
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    public static void sendResponse(){

        try {
            System.out.println("Sending response to Master");
            System.out.println("SyncResponse: " + syncResponse.getResponse());
            // Connect to Master CHANGE "localhost" to Master's IP address
            Socket masterSocket = new Socket("localhost", 9093);
            ObjectOutputStream out = new ObjectOutputStream(masterSocket.getOutputStream());
            out.writeObject(syncResponse);
            out.flush();
            System.out.println("Response sent to Master");

            // Reset the activeWorkers
            activeWorkers = workers;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static void addRoomToSyncRooms(Room room) {
        syncRooms.add(room);
    }

    public synchronized static void clearSyncRooms() {
        syncRooms.clear();
    }

    public synchronized static void reduceActiveWorkers() {
        activeWorkers--;
        System.out.println("Active Workers: " + activeWorkers);

        // Check if all workers have finished
        if (activeWorkers == 0) {
            // Send the response to Master
            sendResponse();
            // Clear the syncRooms
            clearSyncRooms();
            // Clear the syncResponse.rooms
            syncResponse.clearRooms();
            // Clear the syncResponse
            syncResponse.setResponse("");
            // Reset the activeWorkers
            activeWorkers = workers;
        }
    }
}