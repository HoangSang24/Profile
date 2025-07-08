/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Showtimes;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowtimesDAO extends DBContext {

// Lấy tất cả các suất chiếu
    public List<Showtimes> getAllShowtimes() {
    List<Showtimes> showtimes = new ArrayList<>();
    String sql = "SELECT * FROM Showtimes";
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Showtimes showtime = new Showtimes(
                rs.getInt("ShowtimeID"),
                rs.getInt("MovieID"),
                rs.getInt("CinemaID"),
                rs.getInt("RoomID"),
                rs.getTimestamp("StartTime"),
                rs.getDouble("Price") // Thêm cột giá vé
            );
            showtimes.add(showtime);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return showtimes;
}

    // Lấy suất chiếu theo ID
    public Showtimes getShowtimeById(int showtimeID) {
        String sql = "SELECT * FROM Showtimes WHERE ShowtimeID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showtimeID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Showtimes(
                            rs.getInt("ShowtimeID"),
                            rs.getInt("MovieID"),
                            rs.getInt("CinemaID"),
                            rs.getInt("RoomID"),
                            rs.getTimestamp("StartTime"),
                            rs.getDouble("Price") // Thêm cột giá vé
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
public List<Showtimes> getShowtimesByMovie() {
    List<Showtimes> showtimesList = new ArrayList<>();
    String sql = "SELECT s.MovieID, s.ShowtimeID, s.StartTime, c.CinemaID, c.Name, c.Location, s.Price, r.RoomName \n" +
"                FROM Showtimes s JOIN Cinemas c ON s.CinemaID = c.CinemaID\n" +
"				join Rooms r on s.RoomID = r.RoomID";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Showtimes showtime = new Showtimes();
            showtime.setMovieID(rs.getInt("MovieID"));
            showtime.setShowtimeID(rs.getInt("ShowtimeID"));
            showtime.setStartTime(rs.getTimestamp("StartTime"));
            showtime.setCinemaID(rs.getInt("CinemaID"));
            showtime.setCinemaName(rs.getString("Name")); 
            showtime.setAddress(rs.getString("Location")); 
            showtime.setRoomName(rs.getString("RoomName")); 
            showtime.setPrice(rs.getDouble("Price")); // Sửa lỗi thiếu cột Price

            showtimesList.add(showtime);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return showtimesList;
}

    
    public List<Showtimes> getShowtimesByMovie(int movieID) {
        List<Showtimes> showtimesList = new ArrayList<>();
        String sql = "SELECT s.ShowtimeID, s.StartTime, c.CinemaID, c.Name, c.Location \n" +
"                     FROM Showtimes s JOIN Cinemas c ON s.CinemaID = c.CinemaID \n" +
"                     WHERE s.MovieID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Showtimes showtime = new Showtimes();
                showtime.setShowtimeID(rs.getInt("ShowtimeID"));
                showtime.setStartTime(rs.getTimestamp("StartTime"));
                showtime.setCinemaID(rs.getInt("CinemaID"));
                showtime.setCinemaName(rs.getString("Name")); // Thêm tên rạp
                showtime.setAddress(rs.getString("Location")); // Thêm địa chỉ

                showtimesList.add(showtime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimesList;
    }

    public boolean addShowtime(Showtimes showtime) {
        String getMaxIdQuery = "SELECT MAX(ShowtimeID) AS maxid FROM Showtimes";
        int nextId = 1; // Nếu bảng rỗng, ID bắt đầu từ 1

        try ( PreparedStatement psGetMaxId = conn.prepareStatement(getMaxIdQuery);  
                ResultSet rsGetMaxId = psGetMaxId.executeQuery()) {

            if (rsGetMaxId.next() && rsGetMaxId.getInt("maxid") > 0) {
                nextId = rsGetMaxId.getInt("maxid") + 1;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy ID lớn nhất: " + e.getMessage());
            return false;
        }

        String insertQuery = "INSERT INTO Showtimes (ShowtimeID, MovieID, CinemaID, RoomID, StartTime,Price) VALUES (?, ?, ?, ?, ?,?)";
        try ( PreparedStatement ps = conn.prepareStatement(insertQuery)) {
            ps.setInt(1, nextId);
            ps.setInt(2, showtime.getMovieID());
            ps.setInt(3, showtime.getCinemaID());
            ps.setInt(4, showtime.getRoomID());
            ps.setTimestamp(5, new Timestamp(showtime.getStartTime().getTime()));
            ps.setDouble(6, showtime.getPrice());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi chèn suất chiếu: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật suất chiếu
    public boolean updateShowtime(Showtimes showtime) {
        String sql = "UPDATE Showtimes SET MovieID = ?, CinemaID = ?, RoomID = ?, StartTime = ?, Price = ? WHERE ShowtimeID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showtime.getMovieID());
            ps.setInt(2, showtime.getCinemaID());
            ps.setInt(3, showtime.getRoomID());
            ps.setTimestamp(4, new Timestamp(showtime.getStartTime().getTime()));
            ps.setDouble(5, showtime.getPrice());
            ps.setInt(6, showtime.getShowtimeID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa suất chiếu
    public boolean deleteShowtime(int showtimeID) {
        String sql = "DELETE FROM Showtimes WHERE ShowtimeID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showtimeID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
