package model;

import java.time.LocalDateTime;
import java.util.Optional;

public class Booking {
    private int bookingID;
    private int userID;
    private int showtimeID;
    private int seatID;
    private String status;
    private String discountCode;
    private Optional<Integer> eventID; // Có thể null
    private LocalDateTime bookingDate; // Ngày đặt vé

    // Các trạng thái đặt vé
    public static final String STATUS_PENDING = "PENDING";   // Chờ xác nhận
    public static final String STATUS_CONFIRMED = "CONFIRMED"; // Đã xác nhận
    public static final String STATUS_CANCELED = "CANCELED";   // Đã hủy

    // Constructor đầy đủ (dùng khi lấy từ DB)
    public Booking(int bookingID, int userID, int showtimeID, int seatID, String status, String discountCode, Integer eventID, LocalDateTime bookingDate) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.showtimeID = showtimeID;
        this.seatID = seatID;
        this.status = status;
        this.discountCode = discountCode;
        this.eventID = Optional.ofNullable(eventID);
        this.bookingDate = bookingDate;
    }

    // Constructor không có bookingID (dùng khi thêm mới)
    public Booking(int userID, int showtimeID, int seatID, String status, String discountCode, Integer eventID) {
        this.userID = userID;
        this.showtimeID = showtimeID;
        this.seatID = seatID;
        this.status = status;
        this.discountCode = discountCode;
        this.eventID = Optional.ofNullable(eventID);
        this.bookingDate = LocalDateTime.now(); // Mặc định là ngày giờ hiện tại
    }

    // Constructor mặc định
    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.eventID = Optional.empty();
    }

    // Getters và Setters
    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getShowtimeID() { return showtimeID; }
    public void setShowtimeID(int showtimeID) { this.showtimeID = showtimeID; }

    public int getSeatID() { return seatID; }
    public void setSeatID(int seatID) { this.seatID = seatID; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }

    public Optional<Integer> getEventID() { return eventID; }
    public void setEventID(Integer eventID) { this.eventID = Optional.ofNullable(eventID); }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
}
