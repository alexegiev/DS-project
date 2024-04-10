package entities;

import java.io.Serializable;

public class Request implements Serializable{

    int requestId;
    Room room;

    public Request(int requestId, Room room) {
        this.requestId = requestId;
        this.room = room;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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
