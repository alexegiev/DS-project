package entities;
import java.io.Serializable;

/*
* This entity represents a room in the system.
*/

public class Room implements Serializable {

    //TODO: Add fields
    // private fields
    boolean testing; // is used only for debugging purposes
    int roomCount = 0;
    int roomIncrement = 0;

    public Room(int roomCount) {
        this.testing = false;
        this.roomCount = roomCount;
    }

    public boolean isTesting() {
        return testing;
    }

    public void setTesting(boolean testing) {
        this.testing = testing;
    }

    @Override
    public String toString() {
        return "Room{" +
                "testing=" + testing +
                ", roomCount=" + roomCount +
                ", roomIncrement=" + roomIncrement +
                '}';
    }

    public void incrementRoomIncrement() {
        this.roomIncrement++;
    }
}
