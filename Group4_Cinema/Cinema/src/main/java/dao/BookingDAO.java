/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import utils.DBContext;
import model.Booking;
import model.BookingInfo;

public class BookingDAO extends DBContext {

    public boolean isSeatBooked(int showtimeId, int seatId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE showtime_id = ? AND seat_id = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtimeId);
            stmt.setInt(2, seatId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public boolean bookSeat(int userId, int showtimeId, String seatLabel) {
        String sql = "INSERT INTO Bookings (UserID, ShowtimeID, SeatNumber, Status) VALUES (?, ?, ?, 'Booked')";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, showtimeId);
            pstmt.setString(3, seatLabel);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu đặt ghế thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    
    
    
    public List<BookingInfo> getTicketById(int userID) {
        List<BookingInfo> bookingList = new ArrayList<>();
        String sql = "SELECT m.Title AS MovieTitle, "
                + "s.Price,"
                + "t.TicketID, "
                + "s.StartTime AS Showtime, "
                + "c.Name AS CinemaName, "
                + "r.RoomName AS RoomName, "
                + "c.Location,"
                + "se.SeatNumber, "
                + "b.ShowtimeID, b.SeatID, b.Status, b.DiscountCode, b.EventID, b.BookingDate,"
                + "r.RoomID "
                + "FROM Bookings b "
                + "JOIN Tickets t ON t.BookingID = b.BookingID "
                + "JOIN Showtimes s ON b.ShowtimeID = s.ShowtimeID "
                + "JOIN Movies m ON s.MovieID = m.MovieID "
                + "JOIN Cinemas c ON s.CinemaID = c.CinemaID "
                + "JOIN Rooms r ON s.RoomID = r.RoomID "
                + "JOIN Seats se ON se.SeatID = b.SeatID "
                + "JOIN Users u ON u.UserID = b.UserID "
                + "WHERE u.UserID = ?";

        try ( PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String movieTitle = resultSet.getString("MovieTitle");
                double price = resultSet.getDouble("Price");
                String showtime = resultSet.getString("Showtime");
                String cinemaName = resultSet.getString("CinemaName");
                String roomName = resultSet.getString("RoomName");
                String location = resultSet.getString("Location");
                String seatNumber = resultSet.getString("SeatNumber");
                int showtimeID = resultSet.getInt("ShowtimeID");
                int seatID = resultSet.getInt("SeatID");
                String status = resultSet.getString("Status");
                String discountCode = resultSet.getString("DiscountCode");

                int eventID = resultSet.getInt("EventID");
                Date bookingDate = resultSet.getDate("BookingDate");
                int roomID = resultSet.getInt("RoomID");
                int ticketID = resultSet.getInt("TicketID");

                // Tạo đối tượng BookingInfo với constructor đầy đủ
                BookingInfo bookingInfo = new BookingInfo(movieTitle, price, showtime, cinemaName, roomName, location,
                        showtimeID, seatID, status, discountCode, eventID, bookingDate, roomID, ticketID, seatNumber);
                // Thêm vào danh sách
                bookingList.add(bookingInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Có thể thay bằng logging
        }

        return bookingList;
    }

    public List<BookingInfo> getTicketAll() {
        List<BookingInfo> bookingList = new ArrayList<>();
        String sql = "SELECT m.Title AS MovieTitle, "
                + "s.Price,"
                + "t.TicketID, "
                + "s.StartTime AS Showtime, "
                + "c.Name AS CinemaName, "
                + "r.RoomName AS RoomName, "
                + "c.Location,"
                + "se.SeatNumber, "
                + "b.ShowtimeID, b.SeatID, b.Status, b.DiscountCode, b.EventID, b.BookingDate,"
                + "r.RoomID "
                + "FROM Bookings b "
                + "JOIN Tickets t ON t.BookingID = b.BookingID "
                + "JOIN Showtimes s ON b.ShowtimeID = s.ShowtimeID "
                + "JOIN Movies m ON s.MovieID = m.MovieID "
                + "JOIN Cinemas c ON s.CinemaID = c.CinemaID "
                + "JOIN Rooms r ON s.RoomID = r.RoomID "
                + "JOIN Seats se ON se.SeatID = b.SeatID";

        try ( PreparedStatement statement = conn.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String movieTitle = resultSet.getString("MovieTitle");
                double price = resultSet.getDouble("Price");
                String showtime = resultSet.getString("Showtime");
                String cinemaName = resultSet.getString("CinemaName");
                String roomName = resultSet.getString("RoomName");
                String location = resultSet.getString("Location");
                String seatNumber = resultSet.getString("SeatNumber");
                int showtimeID = resultSet.getInt("ShowtimeID");
                int seatID = resultSet.getInt("SeatID");
                String status = resultSet.getString("Status");
                String discountCode = resultSet.getString("DiscountCode");

                int eventID = resultSet.getInt("EventID");
                Date bookingDate = resultSet.getDate("BookingDate");
                int roomID = resultSet.getInt("RoomID");
                int ticketID = resultSet.getInt("TicketID");

                // Tạo đối tượng BookingInfo với constructor đầy đủ
                BookingInfo bookingInfo = new BookingInfo(movieTitle, price, showtime, cinemaName, roomName, location,
                        showtimeID, seatID, status, discountCode, eventID, bookingDate, roomID, ticketID, seatNumber);
                // Thêm vào danh sách
                bookingList.add(bookingInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Có thể thay bằng logging
        }

        return bookingList;
    }

    public List<BookingInfo> getBookingDetailsByMovieId(int movieId) {
        List<BookingInfo> bookingList = new ArrayList<>();
        String sql = "SELECT m.Title AS MovieTitle, "
                + "s.Price, "
                + "s.StartTime AS Showtime, "
                + "c.Name AS CinemaName, "
                + "r.RoomName AS RoomName, "
                + "c.Location, "
                + "b.ShowtimeID, b.SeatID, b.Status, b.DiscountCode, b.EventID, b.BookingDate,"
                + "r.RoomID "
                + "FROM Bookings b "
                + "JOIN Tickets t ON t.BookingID = b.BookingID "
                + "JOIN Showtimes s ON b.ShowtimeID = s.ShowtimeID "
                + "JOIN Movies m ON s.MovieID = m.MovieID "
                + "JOIN Cinemas c ON s.CinemaID = c.CinemaID "
                + "JOIN Rooms r ON s.RoomID = r.RoomID "
                + "WHERE m.MovieID = ?";

        try ( PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, movieId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String movieTitle = resultSet.getString("MovieTitle");
                double price = resultSet.getDouble("Price");
                String showtime = resultSet.getString("Showtime");
                String cinemaName = resultSet.getString("CinemaName");
                String roomName = resultSet.getString("RoomName");
                String location = resultSet.getString("Location");
                int showtimeID = resultSet.getInt("ShowtimeID");
                int seatID = resultSet.getInt("SeatID");
                String status = resultSet.getString("Status");
                String discountCode = resultSet.getString("DiscountCode");
                int eventID = resultSet.getInt("EventID");
                Date bookingDate = resultSet.getDate("BookingDate");
                int roomID = resultSet.getInt("RoomID");

                // Tạo đối tượng BookingInfo với constructor đầy đủ
                BookingInfo bookingInfo = new BookingInfo(movieTitle, price, showtime, cinemaName, roomName, location,
                        showtimeID, seatID, status, discountCode, eventID, bookingDate, roomID);
                // Thêm vào danh sách
                bookingList.add(bookingInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Có thể thay bằng logging
        }

        return bookingList;
    }

    public List<BookingInfo> getBookingBySeatId(int seatId) {
        List<BookingInfo> bookingList = new ArrayList<>();
        String sql = "SELECT b.BookingID, s.Price "
                + "FROM Bookings b "
                + "JOIN Showtimes s ON b.ShowtimeID = s.ShowtimeID "
                + "WHERE b.SeatID = ?";

        System.out.println("Running Query: " + sql + " with SeatID = " + seatId); // Debug SQL

        try ( PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, seatId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int bookingId = resultSet.getInt("BookingID");  // Debug xem có giá trị không
                double price = resultSet.getDouble("Price");

                System.out.println("Found BookingID: " + bookingId + " with Price: " + price);

                BookingInfo booking = new BookingInfo(price, bookingId);
                bookingList.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Total Bookings Found: " + bookingList.size()); // Debug kết quả
        return bookingList;
    }


    public boolean insertBooking(int userID, int showtimeID, int seatID, String status, String discountCode) {
        String sql = "INSERT INTO Bookings ( UserID, ShowtimeID, SeatID, Status, DiscountCode) VALUES ( ?, ?, ?, ?, ?)";
        try (
                 PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setInt(2, showtimeID);
            ps.setInt(3, seatID);
            ps.setString(4, status);
            ps.setString(5, discountCode);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả các booking
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Bookings";

        try ( PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Lấy danh sách đặt vé theo UserID
    public List<Booking> getBookingHistoryByUser(int userID) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Bookings WHERE UserID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Thêm booking mới
    public boolean addBooking(Booking booking) {
        String sql = "INSERT INTO Bookings (UserID, ShowtimeID, SeatID, Status, DiscountCode, EventID, BookingDate) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getUserID());
            stmt.setInt(2, booking.getShowtimeID());
            stmt.setInt(3, booking.getSeatID());
            stmt.setString(4, booking.getStatus());
            stmt.setString(5, booking.getDiscountCode());
            if (booking.getEventID().isPresent()) {
                stmt.setInt(6, booking.getEventID().get());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setTimestamp(7, Timestamp.valueOf(booking.getBookingDate()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái đặt vé
    public boolean updateBookingStatus(int bookingID, String newStatus) {
        String sql = "UPDATE Bookings SET Status = ? WHERE BookingID = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, bookingID);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa booking theo ID
    public boolean deleteBooking(int bookingID) {
        String sql = "DELETE FROM Bookings WHERE BookingID = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingID);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Chuyển từ ResultSet -> Booking Object
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("BookingID"),
                rs.getInt("UserID"),
                rs.getInt("ShowtimeID"),
                rs.getInt("SeatID"),
                rs.getString("Status"),
                rs.getString("DiscountCode"),
                rs.getObject("EventID") != null ? rs.getInt("EventID") : null,
                rs.getTimestamp("BookingDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }

    public static void main(String[] args) {

    }
}
