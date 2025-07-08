/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Movie;
import utils.DBContext;

/**
 *
 * @author HoangSang
 */
public class MovieDAO extends DBContext {

    public MovieDAO() {
        super();
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try {
            String query = "SELECT * FROM Movies";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("MovieID"),
                        rs.getString("Title"),
                        rs.getString("Genre"),
                        rs.getInt("Duration"),
                        rs.getDate("ReleaseDate"),
                        rs.getString("Description"),
                        rs.getString("ImagePath")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> searchMoviesByName(String movieName) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movies WHERE title LIKE ?";
        try (
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + movieName + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieID(rs.getInt("movieID"));
                movie.setTitle(rs.getString("title"));
                movie.setDescription(rs.getString("description"));
                movie.setImagePath(rs.getString("imagePath"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public int getNextMovieId() {
        int nextId = 1;
        try {
            String query = "SELECT MAX(MovieID) AS maxid FROM Movies";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("maxid") != 0) {
                nextId = rs.getInt("maxid") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    public void addMovie(int movieId, String title, String genre, int duration, Date releaseDate, String description, String imagePath) {
        try {
            String query = "INSERT INTO Movies (MovieID, Title, Genre, Duration, ReleaseDate, Description, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, movieId);
            ps.setString(2, title);
            ps.setString(3, genre);
            ps.setInt(4, duration);
            ps.setDate(5, releaseDate);
            ps.setString(6, description);
            ps.setString(7, imagePath);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Movie getMovieById(int id) {
        try {
            String query = "SELECT * FROM Movies WHERE MovieID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Movie(
                        rs.getInt("MovieID"),
                        rs.getString("Title"),
                        rs.getString("Genre"),
                        rs.getInt("Duration"),
                        rs.getDate("ReleaseDate"),
                        rs.getString("Description"),
                        rs.getString("ImagePath")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Movie getMovieByName(String name) {
        try {
            String query = "SELECT * FROM Movies WHERE Title = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name); // Truyền tên phim chính xác
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Movie(
                        rs.getInt("MovieID"),
                        rs.getString("Title"),
                        rs.getString("Genre"),
                        rs.getInt("Duration"),
                        rs.getDate("ReleaseDate"),
                        rs.getString("Description"),
                        rs.getString("ImagePath")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMovie(Movie movie) {
        String query = "UPDATE Movies SET Title=?, Genre=?, Duration=?, ReleaseDate=?, Description=?, ImagePath=? WHERE MovieID=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getDuration());
            ps.setDate(4, movie.getReleaseDate());
            ps.setString(5, movie.getDescription());
            ps.setString(6, movie.getImagePath());
            ps.setInt(7, movie.getMovieID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteMovie(int id) {
        String sql = "DELETE FROM Movies WHERE MovieID = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0; // Nếu có dòng bị ảnh hưởng, nghĩa là xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUniqueFileName(String directory, String fileName) {
        File file = new File(directory, fileName);
        if (!file.exists()) {
            return fileName; // Nếu chưa tồn tại, dùng tên gốc
        }

        String name = fileName.substring(0, fileName.lastIndexOf(".")); // Tên file (không có đuôi)
        String extension = fileName.substring(fileName.lastIndexOf(".")); // Đuôi mở rộng (.jpg, .png)
        int count = 1;

        while (file.exists()) {
            String newFileName = name + "(" + count + ")" + extension;
            file = new File(directory, newFileName);
            count++;
        }
        return file.getName(); // Trả về tên mới không bị trùng
    }

    public boolean saveMovieToDatabase(String title, String genre, int duration, Date releaseDate, String description, String filePath) {

        String sql = "INSERT INTO Movies (Title, Genre, Duration, ReleaseDate, Description, ImagePath) VALUES (?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, genre);
            ps.setInt(3, duration);
            ps.setDate(4, releaseDate);
            ps.setString(5, description);
            ps.setString(6, filePath);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
