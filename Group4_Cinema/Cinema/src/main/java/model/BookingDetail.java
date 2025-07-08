package model;

public class BookingDetail {
    private int bookingID;
    private String username;
    private String movieTitle;
    private String eventName;
    private double discountPercentage;
    private double originalPrice;
    private double discountedPrice;

    public BookingDetail(int bookingID, String username, String movieTitle, String eventName, 
                         double discountPercentage, double originalPrice, double discountedPrice) {
        this.bookingID = bookingID;
        this.username = username;
        this.movieTitle = movieTitle;
        this.eventName = eventName;
        this.discountPercentage = discountPercentage;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
    }

    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }

    public double getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(double discountedPrice) { this.discountedPrice = discountedPrice; }
}
