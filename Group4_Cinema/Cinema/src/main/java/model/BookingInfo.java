package model;

import java.sql.Date;
import java.util.List;

public class BookingInfo {

    private String movieTitle;
    private double Price;
    private String showtime;
    private String cinemaName;
    private String roomName;
    private String location;
    private int showtimeID;
    private int seatID;
    private int bookingID;
    private String status;
    private String seatName;
    private String discountCode;
    private int eventID;
    private int roomID;
    private int ticketID;
    private Date bookingDate;

    public int getTicketID() {
        return ticketID;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public BookingInfo(double Price, int bookingID) {
        this.Price = Price;
        this.bookingID = bookingID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    private List<String> seatList;

    public String getLocation() {
        return location;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getShowtimeID() {
        return showtimeID;
    }

    public int getSeatID() {
        return seatID;
    }

    public String getStatus() {
        return status;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public int getEventID() {
        return eventID;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setShowtimeID(int showtimeID) {
        this.showtimeID = showtimeID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setSeatList(List<String> seatList) {
        this.seatList = seatList;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Constructor
    public BookingInfo(String movieTitle, double Price, String showtime, String cinemaName, String roomName, List<String> seatList) {
        this.movieTitle = movieTitle;
        this.Price = Price;
        this.showtime = showtime;
        this.cinemaName = cinemaName;
        this.roomName = roomName;
        this.seatList = seatList;
    }

    public BookingInfo(String movieTitle, double Price, String showtime, String cinemaName, String roomName, String location, int showtimeID, int seatID, String status, String discountCode, int eventID, Date bookingDate, int roomID) {
        this.movieTitle = movieTitle;
        this.Price = Price;
        this.showtime = showtime;
        this.cinemaName = cinemaName;
        this.roomName = roomName;
        this.location = location;
        this.showtimeID = showtimeID;
        this.seatID = seatID;
        this.status = status;
        this.discountCode = discountCode;
        this.eventID = eventID;
        this.bookingDate = bookingDate;
        this.roomID = roomID;
    }
    public BookingInfo(String movieTitle, double Price, String showtime, String cinemaName, String roomName, String location, int showtimeID, int seatID, String status, String discountCode, int eventID, Date bookingDate, int roomID, int ticketID, String seatName ) {
        this.movieTitle = movieTitle;
        this.Price = Price;
        this.showtime = showtime;
        this.cinemaName = cinemaName;
        this.roomName = roomName;
        this.location = location;
        this.showtimeID = showtimeID;
        this.seatID = seatID;
        this.status = status;
        this.discountCode = discountCode;
        this.eventID = eventID;
        this.bookingDate = bookingDate;
        this.roomID = roomID;
        this.ticketID = ticketID;
        this.seatName = seatName;
    }


    public BookingInfo(String movieTitle, double Price, String showtime, String cinemaName, String roomName, String location) {
        this.movieTitle = movieTitle;
        this.Price = Price;
        this.showtime = showtime;
        this.cinemaName = cinemaName;
        this.roomName = roomName;
        this.location = location;
    }

    // Getters
    public String getMovieTitle() {
        return movieTitle;
    }

    public double getPrice() {
        return Price;
    }

    public String getShowtime() {
        return showtime;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<String> getSeatList() {
        return seatList;
    }
}
