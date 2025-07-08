package model;

import java.sql.Date;

public class Movie {

    private int movieID;
    private String title;
    private String genre;
    private int duration;
    private Date releaseDate;
    private String description;
    private String imagePath; // Thêm thuộc tính lưu đường dẫn ảnh

    public Movie() {
    }
    
    

    public Movie(int movieID, String title, String genre, int duration, Date releaseDate, String description, String imagePath) {
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.description = description;
        this.imagePath = imagePath;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }


    public String getImagePath() {
        return (imagePath == null || imagePath.isEmpty()) ? "images/default.jpg" : imagePath;
    }
}
