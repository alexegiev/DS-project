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
            } else if (responseFromWorker.getAction().equals("Add Room")) {
                handleAddRoom();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void handleShowOwnedRooms(Response responseFromWorker) throws Exception {
        Reducer.activeWorkers--;
        Thread.sleep(1000);
        Reducer.syncResponse.addRoomList(responseFromWorker.getRooms());
        System.out.println("Rooms owned by Manager: " + Reducer.syncResponse.getRoomsString());
        if (Reducer.activeWorkers <= 0) {
            sendResponseToServerThread("Show Owned Rooms");
        }
    }

    private void handleAddRoom() throws Exception {
        sendResponseToServerThread("Room(s) Added");
    }

    private synchronized void sendResponseToServerThread(String message){
        try {
            Socket serverSocket = new Socket("localhost", 9093);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream serverIn = new ObjectInputStream(serverSocket.getInputStream());

            Response response = new Response();

            // Check the message value and send the according response
            if (message.equals("Room(s) Added")) {
                response.setResponse("Room(s) added");
            } else {
                //Reducer.syncResponse.setResponse("Rooms owned by Manager FROM REDUCER: " + "\n" +Reducer.syncResponse.getRoomsString());
                response.setResponse("Rooms owned by Manager: " + "\n" + Reducer.syncResponse.getRoomsString());
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