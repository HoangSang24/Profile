/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author HOANG THAI
 */
public class Ticket {

    private int ticketID;
    private double price;
    private String status;
    private int bookingID;
    private int userID;
    private int showtimeID;
    private int seatID;
    private String bookingStatus;
    private String seatNumber;
    private LocalDateTime startTime;
    private String movieTitle;

    public Ticket() {
    }

    public Ticket(int ticketID, double price, String status, int bookingID, int userID, int showtimeID, int seatID, String bookingStatus) {
        this.ticketID = ticketID;
        this.price = price;
        this.status = status;
        this.bookingID = bookingID;
        this.userID = userID;
        this.showtimeID = showtimeID;
        this.seatID = seatID;
        this.bookingStatus = bookingStatus;
    }

    public Ticket(int ticketID, int bookingID, double price, String status, int seatID, int showtimeID) {
        this.ticketID = ticketID;
        this.price = price;
        this.status = status;
        this.bookingID = bookingID;
        this.showtimeID = showtimeID;
        this.seatID = seatID;
    }

    public Ticket(int ticketID, String status, double price, String seatNumber, LocalDateTime startTime, String movieTitle) {
        this.ticketID = ticketID;
        this.status = status;
        this.price = price;
        this.seatNumber = seatNumber;
        this.startTime = startTime;
        this.movieTitle = movieTitle;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getShowtimeID() {
        return showtimeID;
    }

    public void setShowtimeID(int showtimeID) {
        this.showtimeID = showtimeID;
    }

    public int getSeatID() {
        return seatID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

}
