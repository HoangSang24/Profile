/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class Showtimes {

    private int showtimeID;
    private int movieID;
    private int cinemaID;
    private int roomID;
    private Date startTime;
    private String CinemaName;
    private String RoomName;
    private String Address;
    private double price; // Thêm giá vé

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String RoomName) {
        this.RoomName = RoomName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Showtimes() {
    }

    public Showtimes(int showtimeID, int cinemaID, Date startTime, String CinemaName, String Address) {
        this.showtimeID = showtimeID;
        this.cinemaID = cinemaID;
        this.startTime = startTime;
        this.CinemaName = CinemaName;
        this.Address = Address;
    }

    public String getCinemaName() {
        return CinemaName;
    }

    public void setCinemaName(String CinemaName) {
        this.CinemaName = CinemaName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public Showtimes(int movieID, int cinemaID, int roomID, Date startTime, double price) {
        this.movieID = movieID;
        this.cinemaID = cinemaID;
        this.roomID = roomID;
        this.startTime = startTime;
        this.price = price;
    }

    public Showtimes(int showtimeID, int movieID, int cinemaID, int roomID, Date startTime, double price) {
        this.showtimeID = showtimeID;
        this.movieID = movieID;
        this.cinemaID = cinemaID;
        this.roomID = roomID;
        this.startTime = startTime;
        this.price = price;
    }

    public int getShowtimeID() {
        return showtimeID;
    }

    public void setShowtimeID(int showtimeID) {
        this.showtimeID = showtimeID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getCinemaID() {
        return cinemaID;
    }

    public void setCinemaID(int cinemaID) {
        this.cinemaID = cinemaID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Showtimes{"
                + "showtimeID=" + showtimeID
                + ", movieID=" + movieID
                + ", cinemaID=" + cinemaID
                + ", roomID=" + roomID
                + ", startTime=" + startTime
                + '}';
    }
}
