package model;

import java.sql.Date;

public class Event {
    private int eventID;
    private String name;
    private String description;
    private Date eventDate;
    private int cinemaID;
    private double discountPercentage;
    private String imagePath;

    public Event() {
    }

    public Event(int eventID, String name, String description, Date eventDate, int cinemaID, double discountPercentage, String imagePath) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.cinemaID = cinemaID;
        this.discountPercentage = discountPercentage;
        this.imagePath = imagePath;
    }

    public Event(int eventID, String name, String description, Date eventDate, String imagePath) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.imagePath = imagePath;
    }

    public int getEventID() { return eventID; }
    public void setEventID(int eventID) { this.eventID = eventID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public int getCinemaID() { return cinemaID; }
    public void setCinemaID(int cinemaID) { this.cinemaID = cinemaID; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
