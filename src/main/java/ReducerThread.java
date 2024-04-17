import entities.Request;
import entities.Response;
import entities.Room;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ReducerThread extends Thread {

    private final Socket workerSocket;

    public ReducerThread(Socket workerSocket) {
        this.workerSocket = workerSocket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(workerSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(workerSocket.getInputStream());

            synchronized(this) {
                Response responseFromWorker = (Response) in.readObject();
                System.out.println("Response from worker: " + responseFromWorker);

                if (responseFromWorker.getAction().equals("Show Owned Rooms")) {
                    handleShowOwnedRooms(responseFromWorker);
                }
                else if (responseFromWorker.getAction().equals("Add Room")) {
                    handleAddRoom(responseFromWorker);
                }
                else if (responseFromWorker.getAction().equals("Add Room Availability Date")) {
                    handleUpdateAvailabilityDate(responseFromWorker);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdateAvailabilityDate(Response responseFromWorker) throws InterruptedException {
        // Check if the response is successful
        if (responseFromWorker.getResponse().equals("Room availability date added")) {
            // Set the response to the Reducer syncResponse
            Reducer.syncResponse.setResponse("Room availability date added");
        } else {
            System.out.println("Room availability date already exists");
        }

        // Reduce the activeWorkers count
        Reducer.activeWorkers--;

        // Check if all workers have responded
        if (Reducer.activeWorkers <= 0) {
            // Print the message
            System.out.println(Reducer.syncResponse.getResponse());
            sendResponseToServerThread(responseFromWorker);
        }
    }

    private synchronized void handleShowOwnedRooms(Response responseFromWorker) throws Exception {
        // Reduce the activeWorkers count
        Reducer.activeWorkers--;
        System.out.println("Active Workers: " + Reducer.activeWorkers);
        // Add the rooms to the syncResponse
        Reducer.syncResponse.addRoomList(responseFromWorker.getRooms());
        System.out.println("Rooms owned by Manager: " + Reducer.syncResponse.getRoomsString());

        // Check if all workers have responded
        if (Reducer.activeWorkers <= 0) {
            sendResponseToServerThread(responseFromWorker);
        }
    }

    private void handleAddRoom(Response responseFromWorker) throws Exception {
        // Reduce the activeWorkers count
        Reducer.activeWorkers--;

        // Check if all workers have responded
        if (Reducer.activeWorkers <= 0) {
            sendResponseToServerThread(responseFromWorker);
        }
    }

    private synchronized void sendResponseToServerThread(Response responseFromWorker){
        try {
            Socket serverSocket = new Socket("localhost", 9093);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream serverIn = new ObjectInputStream(serverSocket.getInputStream());

            Response response = new Response();

            // Check the message value and send the according response
            if (responseFromWorker.getAction().equals("Add Room")) {
                response.setResponse(responseFromWorker.getResponse());
                response.setAction("Add Room");
            }
            else if (responseFromWorker.getAction().equals("Show Owned Rooms")) {
                response.setAction("Show Owned Rooms");
                response.setResponse("Rooms owned by Manager: " + "\n" + Reducer.syncResponse.getRoomsString());
            } else if (responseFromWorker.getAction().equals("Add Room Availability Date")) {
                // Set the response based on the response from the worker
                response.setResponse(responseFromWorker.getResponse());
                response.setAction("Add Room Availability Date");
            }

            // Send the response to the ServerThread
            serverOut.writeObject(response);
            serverOut.flush();
            Thread.sleep(1000);

            // Clear Reducer's response and list
            Reducer.syncResponse.setResponse("");
            Reducer.syncResponse.getRooms().clear();
            Reducer.activeWorkers = Master.workers.size();
            System.out.println("AFTER Active Workers: " + Reducer.activeWorkers);

            // Clear all active WorkerThreads
            clearWorkerThreads();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearWorkerThreads() {
        for (WorkerThread workerThread : Reducer.workerThreads) {
            workerThread.interrupt();
        }
        Reducer.workerThreads.clear();
    }
}