package entities;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {

    String response;
    String action;
    List<Room> rooms;

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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    // Add toString method that returns all Rooms from the list
    public String getRoomsString() {
        StringBuilder roomsString = new StringBuilder();
        for (Room room : rooms) {
            roomsString.append(room.toString()).append("\n");
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
