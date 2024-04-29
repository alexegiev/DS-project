package entities;

import java.io.Serializable;

public class Request implements Serializable{

    private static final long serialVersionUID = -5226349352779630051L;

    String username;
    int requestId;
    String action;
    String filterType;
    String filterValue;
    Room room;

    public Request() {
    }

    public Request(int requestId, String action, Room room) {
        this.requestId = requestId;
        this.action = action;
        this.room = room;
    }

    public Request(int requestId, String action) {
        this.requestId = requestId;
        this.action = action;
    }

    public Request(String action) {
        this.action = action;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", room=" + room +
                '}';
    }
}
