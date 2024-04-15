package entities;

import tools.Parser;

import javax.json.JsonArray;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Manager extends Thread {
    private Room room;
    private String managerUsername;
    private static final String MASTER_HOST = "localhost";
    private static final int MASTER_PORT = 9090;

    public Manager(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public List<Room> addRoomsFromJson(String username) {

        System.out.println("Add Rooms from manager.json");

        // Create a new Parser object
        Parser parser = new Parser();

        // Parse the json file and create a JsonArray
        JsonArray jsonArray = parser.readJsonFile( "src/main/java/static/" + username + ".json");

        // Parse the JsonArray and create a list of Room objects
        List<Room> managerRooms = parser.parseJsonArray(jsonArray);

        // Return to Room List
        return managerRooms;

    }

    public void addRoomAvailabilityDate() {
        // TODO
    }

    public Request showOwnedRooms(String username) {
        // create appropriate Request object
        Request request = new Request();
        request.setAction("Show Owned Rooms");
        request.setUsername(username);
        return request;
    }
}