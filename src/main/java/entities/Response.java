package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Response implements Serializable {

    String response;
    String action;
    String filterType;
    String filterValue;
    List<Room> rooms = new ArrayList<>();

    private static final long serialVersionUID = -2971810955042798604L;

    public Response() {
    }

    public Response(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public synchronized List<Room> getRooms() {
        return rooms;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public synchronized void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public synchronized void addRoomList(List<Room> rooms) {
        this.rooms.addAll(rooms);
    }

    public synchronized void clearRooms() {
        rooms.clear();
    }

    // Add toString method that returns all Rooms from the list
    public synchronized String getRoomsString() {
        StringBuilder roomsString = new StringBuilder();
        int counter = 1;
        for (Room room : rooms) {
            roomsString.append(counter).append(". ").append(room.toString()).append("\n");
            counter++;
        }
        return roomsString.toString();
    }

    @Override
    public String toString() {
        return "Response{" +
                "response='" + response + '\'' +
                '}';
    }
}