package com.example.myapplication.backend.entities;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
* This entity represents a room in the system.
*/

public class Room implements Serializable {

    // Fields
    private String roomName;
    private int roomId;
    private Date from;
    private Date to;
    private String managerUsername;
    private String renterUsername;
    private String area;
    private double rating;
    private int numberOfReviews;
    private int capacity;
    private float price;
    private double ratingToAdd;

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public double getRatingToAdd() {
        return ratingToAdd;
    }

    public void setRatingToAdd(double ratingToAdd) {
        this.ratingToAdd = ratingToAdd;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getRenterUsername() {
        return renterUsername;
    }

    public void setRenterUsername(String renterUsername) {
        this.renterUsername = renterUsername;
    }

    private String roomImage;
    private Map<Date, Date> availableDates = new HashMap<>(); //key is the start date, value is the end date

    // Constructor
    public Room(String roomName, int roomId, String managerUsername, String area, double rating, int numberOfReviews,int capacity, int price, String roomImage) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.managerUsername = managerUsername;
        this.area = area;
        this.rating = rating;
        this.numberOfReviews = numberOfReviews;
        this.capacity = capacity;
        this.price = price;
        this.roomImage = roomImage;
    }

    // Constructor
    public Room(String roomName, int roomId, String managerUsername, String area, double rating, int numberOfReviews,int capacity, int price, String roomImage, Map<Date, Date> availableDates) {
        this.roomName = roomName;
        this.roomId = roomId;
        this.managerUsername = managerUsername;
        this.area = area;
        this.rating = rating;
        this.numberOfReviews = numberOfReviews;
        this.capacity = capacity;
        this.price = price;
        this.roomImage = roomImage;
        this.availableDates = availableDates;
    }

    public Room(){

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

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
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
        return this.availableDates;
    }

    public void addAvailableDates(Date startDate, Date endDate) {
        this.availableDates.put(startDate, endDate);
    }

    public void insertAvailableDates(Map<Date, Date> dates) {
        this.availableDates.putAll(dates);
    }

    public void reserveDates(Date startDate, Date endDate) {
        this.availableDates.remove(startDate, endDate);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", roomId=" + roomId +
                ", managerUsername=" + managerUsername +
                ", area='" + area + '\'' +
                ", rating=" + rating +
                ", numberOfReviews=" + numberOfReviews +
                ", roomImage='" + roomImage + '\'' +
                ", availableDates=" + availableDates +
                '}';
    }
}
