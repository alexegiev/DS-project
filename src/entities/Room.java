package entities;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/*
* This entity represents a room in the system.
*/

public class Room implements Serializable {

    // Fields
    private String roomName;
    private int roomId;
    private int managerId;
    private String area;
    private double rating;
    private int numberOfReviews;
    private String roomImage;
    private Map<Date, Date> availableDates; //key is the start date, value is the end date

    // Constructor
    public Room(String roomName, int roomId, int managerId, String area, double rating, int numberOfReviews, String roomImage) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.managerId = managerId;
        this.area = area;
        this.rating = rating;
        this.numberOfReviews = numberOfReviews;
        this.roomImage = roomImage;
    }


    // Getters and Setters
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomNumber) {
        this.roomId = roomNumber;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerIdId(int managerId) {
        this.managerId = managerId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public Map<Date, Date> getAvailableDates() {
        return availableDates;
    }

    public void addAvailableDates(Date startDate, Date endDate) {
        this.availableDates.put(startDate, endDate);
    }

    public void reserveDates(Date startDate, Date endDate) {
        this.availableDates.remove(startDate, endDate);
    }
}
