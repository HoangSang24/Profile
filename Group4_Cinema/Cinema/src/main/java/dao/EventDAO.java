/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.BookingDetail;
import model.Event;
import utils.DBContext;

/**
 *
 * @author HoangSang
 */
public class EventDAO extends DBContext {

    public EventDAO() {
        super();
    }

    public List<BookingDetail> getAllBookingsWithDiscount() {
        List<BookingDetail> bookings = new ArrayList<>();
        String query = "SELECT b.BookingID, u.Username, m.Title AS MovieTitle, e.Name AS EventName, "
                + "e.DiscountPercentage, t.Price AS OriginalPrice, "
                + "(t.Price * (1 - e.DiscountPercentage / 100)) AS DiscountedPrice "
                + "FROM Bookings b "
                + "JOIN Users u ON b.UserID = u.UserID "
                + "JOIN Showtimes st ON b.ShowtimeID = st.ShowtimeID "
                + "JOIN Movies m ON st.MovieID = m.MovieID "
                + "LEFT JOIN Events e ON b.EventID = e.EventID "
                + "JOIN Tickets t ON b.BookingID = t.BookingID";

        try ( PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                bookings.add(new BookingDetail(
                        rs.getInt("BookingID"),
                        rs.getString("Username"),
                        rs.getString("MovieTitle"),
                        rs.getString("EventName"),
                        rs.getDouble("DiscountPercentage"),
                        rs.getDouble("OriginalPrice"),
                        rs.getDouble("DiscountedPrice")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM Events";
        try ( PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("EventID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getDate("EventDate"),
                        rs.getInt("CinemaID"),
                        rs.getDouble("DiscountPercentage"),
                        rs.getString("ImagePath")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public int getNextEventId() {
        int nextId = 1;
        try {
            String query = "SELECT MAX(EventID) AS maxid FROM Events";
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

    public void addEvent(Event event) {
        String query = "INSERT INTO Events (EventID, Name, Description, EventDate, CinemaID, DiscountPercentage, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, event.getEventID());
            ps.setString(2, event.getName());
            ps.setString(3, event.getDescription());
            ps.setDate(4, event.getEventDate());
            ps.setInt(5, event.getCinemaID());
            ps.setDouble(6, event.getDiscountPercentage());
            ps.setString(7, event.getImagePath());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void updateEvent(Event event) {
        String query = "UPDATE Events SET Name=?, Description=?, EventDate=?, CinemaID=?, DiscountPercentage=?, ImagePath=? WHERE EventID=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, event.getName());
            ps.setString(2, event.getDescription());
            ps.setDate(3, event.getEventDate());
            ps.setInt(4, event.getCinemaID());
            ps.setDouble(5, event.getDiscountPercentage());
            ps.setString(6, event.getImagePath());
            ps.setInt(7, event.getEventID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Hàm lấy ảnh cũ từ database
    public String getExistingImagePath(int eventId) {
        String query = "SELECT ImagePath FROM Events WHERE EventID=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("ImagePath");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteEvent(int eventID) {
        String query = "DELETE FROM Events WHERE EventID=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, eventID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất một dòng bị xóa
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }

    public Event getEventById(int eventID) {
        String query = "SELECT * FROM Events WHERE EventID=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, eventID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getInt("EventID"),
                            rs.getString("Name"),
                            rs.getString("Description"),
                            rs.getDate("EventDate"),
                            rs.getInt("CinemaID"),
                            rs.getDouble("DiscountPercentage"),
                            rs.getString("ImagePath")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
