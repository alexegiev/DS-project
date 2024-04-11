package entities;

import tools.Parser;

import javax.json.JsonArray;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Manager extends Thread {
    private Room room;
    private static final String MASTER_HOST = "localhost";
    private static final int MASTER_PORT = 9090;

    public Manager() {

    }

//    @Override
//    public void run() {
//        try {
//            // Connect to the Master
//            Socket socket = new Socket(MASTER_HOST, MASTER_PORT);
//            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
//
//            // Send accommodation details to the Master
//            out.writeObject(room);
//            out.flush();
//
//            // Receive response from the Master (if any)
//            Object response = in.readObject();
//            if (response != null) {
//                // Handle the response accordingly
//                System.out.println("Received response from Master: " + response);
//            }
//
//            // Close the streams and socket
//            in.close();
//            out.close();
//            socket.close();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public void addRoomsFromJson(String username) {
        // TODO
        System.out.println("Add Rooms from manager.json");
        Parser parser = new Parser();
        JsonArray jsonArray = parser.readJsonFile( "src/main/java/static/" + username + ".json");
        parser.storeData(parser.parseJsonArray(jsonArray));
    }

    public void addRoomAvailabilityDate() {
        // TODO
    }

    public void showOwnedRooms() {
        // TODO
    }
}