package model;

import java.sql.Date;

public class Review {
    private int reviewID;
    private int userID;
    private String username;
    private int movieID;
    private String movieTitle;
    private String movieImage;
    private int rating;
    private String comment;
    private Date reviewDate;

    // Getters & Setters
    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public Review() {
    }

    public Review(int reviewID, int userID, int movieID, int rating, String comment, Date reviewDate) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.movieID = movieID;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getUsername() { // Getter cho username
        return username;
    }

    public void setUsername(String username) { // Setter cho username
        this.username = username;
    }

    public String getMovieTitle() { // Getter cho movieTitle
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) { // Setter cho movieTitle
        this.movieTitle = movieTitle;
    }
}
