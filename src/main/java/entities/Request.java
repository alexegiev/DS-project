package entities;

import java.io.Serializable;

public class Request implements Serializable{

    private static final long serialVersionUID = -5226349352779630051L;

    int requestId;
    String action;
    Room room;

    public Request(int requestId, String action, Room room) {
        this.requestId = requestId;
        this.action = action;
        this.room = room;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", room=" + room +
                '}';
    }
}
