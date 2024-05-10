package com.example.myapplication.backend.entities;

import com.example.myapplication.backend.tools.Parser;

import javax.json.JsonArray;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Manager extends Thread {
    private Room room;
    private String managerUsername;
    ArrayList<Integer> requestIds;
    private static final String MASTER_HOST = "localhost";
    private static final int MASTER_PORT = 9090;

    public Manager(String managerUsername) {
        this.managerUsername = managerUsername;
        this.requestIds = new ArrayList<>(); // Initialize the ArrayList
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public ArrayList<Integer> getRequestIds() {
        return requestIds;
    }

    public void setRequestIds(ArrayList<Integer> requestIds) {
        this.requestIds = requestIds;
    }

    public void addRequestId(int requestId) {
        this.requestIds.add(requestId);
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