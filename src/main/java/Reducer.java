import entities.Response;
import entities.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Reducer {

    ServerSocket reducer = null;
    public static int activeWorkers = 0;
    private static int workerCount = 1;

    public static List<Room> syncRooms = new ArrayList<>();
    public static Response syncResponse = new Response();

    public static List<WorkerThread> workerThreads = new ArrayList<>();

    public static void main(String[] args) {
        new Reducer();
    }

    public Reducer() {
        try {
            reducer = new ServerSocket(9092);
            System.out.println("Reducer started at port: " + reducer.getLocalPort());
            System.out.println("Workers: " + Master.workers.size());
            waitForWorker();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitForWorker() {

        while (true) {
            try {
                System.out.println("Waiting for Worker request");
                Socket workerSocket = reducer.accept();
                System.out.println("Connected to Worker: " + workerCount + " Times");
                workerCount++;

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