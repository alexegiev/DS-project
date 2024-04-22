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

    public static void main(String[] args) {
        new Reducer();
    }

    public Reducer() {

        ObjectOutputStream outMaster = null;
        ObjectInputStream inMaster = null;

        try {
            reducer = new ServerSocket(9092);
            System.out.println("Reducer started at port: " + reducer.getLocalPort());

            // Connect to Master on port 9094
            // CHANGE "localhost" to Master's IP address
            Socket masterSocket = new Socket("localhost", 9094);
            System.out.println("Connected to Master");

            // Get from Master the number of Workers
            outMaster = new ObjectOutputStream(masterSocket.getOutputStream());
            inMaster = new ObjectInputStream(masterSocket.getInputStream());

            // Send a request to Master
            outMaster.writeObject("Send number of Workers");

            // Receive the number of Workers and set the number of activeWorkers
            activeWorkers = (int) inMaster.readObject();
            System.out.println("Number of Workers: " + activeWorkers);

            // Start the Reducer
            waitForWorker();
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

    public synchronized static void addRoomToSyncRooms(Room room) {
        syncRooms.add(room);
    }

    public synchronized static void clearSyncRooms() {
        syncRooms.clear();
    }
}