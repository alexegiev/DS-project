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

            Response responseFromWorker = (Response) in.readObject();
            System.out.println("Response from worker: " + responseFromWorker);

            if (responseFromWorker.getAction().equals("Show Owned Rooms")) {
                //System.out.println("Reducer received Show Owned Rooms");
                handleShowOwnedRooms(responseFromWorker);
            }
            else if (responseFromWorker.getAction().equals("Add Room")) {
                handleAddRoom(responseFromWorker);
            }
            else if (responseFromWorker.getAction().equals("Add Room Availability Date")) {
                handleUpdateAvailabilityDate(responseFromWorker);
            }
            else if (responseFromWorker.getAction().equals("Add Rating")) {
                handleUpdateRoomRating(responseFromWorker);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void handleUpdateRoomRating(Response responseFromWorker) throws InterruptedException {
        // Reduce the activeWorkers count
        Reducer.activeWorkers--;

        // Put Thread to sleep
        Thread.sleep(1000);

        // Check if the response is successful
        if (responseFromWorker.getResponse().equals("Add Rating")) {
            System.out.println("Rating added");
            // Set the response to the Reducer syncResponse
            Reducer.syncResponse.setResponse("Rating added");
        } else {
            System.out.println("Rating didnt added ");
        }

        // Check if all workers have responded
        if (Reducer.activeWorkers <= 0) {
            sendResponseToServerThread(responseFromWorker);
        }
    }

    

    private void handleUpdateAvailabilityDate(Response responseFromWorker) throws InterruptedException {

        // Reduce the activeWorkers count
        Reducer.activeWorkers--;

        // Put Thread to sleep
        Thread.sleep(1000);

        // Check if the response is successful
        if (responseFromWorker.getResponse().equals("Room availability date added")) {
            System.out.println("Room availability date added");
            // Set the response to the Reducer syncResponse
            Reducer.syncResponse.setResponse("Room availability date added");
        } else {
            System.out.println("Room availability date already exists");
        }

        // Check if all workers have responded
        if (Reducer.activeWorkers <= 0) {
            sendResponseToServerThread(responseFromWorker);
        }
    }

    private synchronized void handleShowOwnedRooms(Response responseFromWorker) throws Exception {

        // Reduce the activeWorkers count
        Reducer.activeWorkers--;

        // Put Thread to sleep
        Thread.sleep(1000);

        // Add the rooms to the syncResponse
        Reducer.syncResponse.addRoomList(responseFromWorker.getRooms());
        System.out.println("Rooms owned by Manager: " + Reducer.syncResponse.getRoomsString());

        // Check if all workers have responded
        if (Reducer.activeWorkers <= 0) {
            sendResponseToServerThread(responseFromWorker);
        }
    }

    private void handleAddRoom(Response responseFromWorker) throws Exception {
        sendResponseToServerThread(responseFromWorker);
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
            }
            else if (responseFromWorker.getAction().equals("Show Owned Rooms")) {
                //Reducer.syncResponse.setResponse("Rooms owned by Manager FROM REDUCER: " + "\n" +Reducer.syncResponse.getRoomsString());
                response.setResponse("Rooms owned by Manager: " + "\n" + Reducer.syncResponse.getRoomsString());
            } else if (responseFromWorker.getAction().equals("Add Room Availability Date")) {
                // Check if Reducer syncResponse is empty
                if (Reducer.syncResponse.getResponse().equals("") || Reducer.syncResponse.getResponse() == null){
                    response.setResponse(responseFromWorker.getResponse());
                } else {
                    response.setResponse("Room availability date added");
                }
            }

            // Send the response to the ServerThread
            serverOut.writeObject(response);
            serverOut.flush();
            Thread.sleep(1000);

            // Clear Reducer's response and list
            Reducer.syncResponse.setResponse("");
            Reducer.syncResponse.getRooms().clear();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}