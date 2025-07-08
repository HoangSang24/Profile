/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Seats;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatsDAO extends DBContext {

    // Lấy tất cả các ghế
    public List<Seats> getAllSeats() {
        List<Seats> seats = new ArrayList<>();
        String sql = "SELECT * FROM Seats";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Seats seat = new Seats(
                        rs.getInt("SeatID"),
                        rs.getInt("RoomID"),
                        rs.getString("SeatNumber"),
                        rs.getString("Status")
                );
                seats.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    // Lấy ghế theo ID
    public Seats getSeatById(int seatID) {
        String sql = "SELECT * FROM Seats WHERE SeatID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Seats(
                            rs.getInt("SeatID"),
                            rs.getInt("RoomID"),
                            rs.getString("SeatNumber"),
                            rs.getString("Status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Kiểm tra ghế có sẵn không
    public boolean isSeatAvailable(int showtimeId, int seatId) {
        String sql = "SELECT COUNT(*) FROM Bookings WHERE ShowtimeID = ? AND SeatID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            ps.setInt(2, seatId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Nếu COUNT(*) = 0 thì ghế còn trống
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách ID ghế đã đặt theo roomID
    public Set<Integer> getBookedSeatIDs(int roomID) {
        Set<Integer> bookedSeats = new HashSet<>();
        String sql = "SELECT seatID FROM Bookings WHERE roomID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bookedSeats.add(rs.getInt("seatID")); // Thêm ID ghế vào danh sách
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }
    
    
     // Lấy danh sách ID ghế đã đặt theo roomID và showtimeID
    public Set<Integer> getBookedSeatIDs(int roomID, int showtimeID) {
        Set<Integer> bookedSeats = new HashSet<>();
        String sql = "SELECT b.SeatID FROM Bookings b " +
                     "JOIN Showtimes s ON b.ShowtimeID = s.ShowtimeID " +
                     "WHERE s.RoomID = ? AND b.ShowtimeID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomID);
            ps.setInt(2, showtimeID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bookedSeats.add(rs.getInt("SeatID")); // Thêm ID ghế vào danh sách
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }

    // Lấy các ghế còn trống theo suất chiếu
    public List<Seats> getAvailableSeatsByShowtime(int showtimeId) {
        List<Seats> availableSeats = new ArrayList<>();
        String sql = "SELECT s.* FROM Seats s "
                + "JOIN Rooms r ON s.RoomID = r.RoomID "
                + "JOIN Showtimes st ON r.RoomID = st.RoomID "
                + "WHERE st.ShowtimeID = ? AND s.Status = 'Available'";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seats seat = new Seats(
                            rs.getInt("SeatID"),
                            rs.getInt("RoomID"),
                            rs.getString("SeatNumber"),
                            rs.getString("Status")
                    );
                    availableSeats.add(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableSeats;
    }

    // Thêm một ghế mới
    public boolean addSeat(Seats seat) {
        String sql = "INSERT INTO Seats (SeatID ,RoomID, SeatNumber, Status) VALUES (?, ?, ?,?)";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seat.getSeatID());
            ps.setInt(2, seat.getRoomID());
            ps.setString(3, seat.getSeatNumber());
            ps.setString(4, seat.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin ghế
    public boolean updateSeat(Seats seat) {
        String sql = "UPDATE Seats SET RoomID = ?, SeatNumber = ?, Status = ? WHERE SeatID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seat.getRoomID());
            ps.setString(2, seat.getSeatNumber());
            ps.setString(3, seat.getStatus());
            ps.setInt(4, seat.getSeatID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa ghế
    public boolean deleteSeat(int seatID) {
        String sql = "DELETE FROM Seats WHERE SeatID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra ghế có sẵn không (theo tên ghế)
    public boolean isSeatAvailable(int showtimeId, String seatLabel) {
        String sql = "SELECT s.Status "
                + "FROM Seats s "
                + "JOIN Rooms r ON s.RoomID = r.RoomID "
                + "JOIN Showtimes st ON r.RoomID = st.RoomID "
                + "LEFT JOIN Bookings b ON st.ShowtimeID = b.ShowtimeID AND s.SeatNumber = ? "
                + "WHERE st.ShowtimeID = ? AND s.SeatNumber = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, seatLabel);
            ps.setInt(2, showtimeId);
            ps.setString(3, seatLabel);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("Status");
                    System.out.println("Trạng thái ghế " + seatLabel + ": " + status); // Log để kiểm tra
                    return "Available".equals(status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách các ghế đã đặt trong một phòng
    public Set<String> getBookedSeats(int roomId) {
        Set<String> bookedSeats = new HashSet<>();
        String sql = "SELECT SeatNumber FROM Seats WHERE RoomID = ? AND Status = 'Booked'";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookedSeats.add(rs.getString("SeatNumber"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }

}
