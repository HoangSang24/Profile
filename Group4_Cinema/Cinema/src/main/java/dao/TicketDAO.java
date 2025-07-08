/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Ticket;
import utils.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO extends DBContext {

    public TicketDAO() {
        super();
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT t.TicketID, t.Price, t.Status, b.BookingID, b.UserID, b.ShowtimeID, b.SeatID, b.Status AS BookingStatus "
                + "FROM Tickets t "
                + "JOIN Bookings b ON t.BookingID = b.BookingID";

        try (
                 PreparedStatement preparedStatement = conn.prepareStatement(query);  ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int ticketID = resultSet.getInt("TicketID");
                double price = resultSet.getDouble("Price");
                String status = resultSet.getString("Status");
                int bookingID = resultSet.getInt("BookingID");
                int userID = resultSet.getInt("UserID");
                int showtimeID = resultSet.getInt("ShowtimeID");
                int seatID = resultSet.getInt("SeatID");
                String bookingStatus = resultSet.getString("BookingStatus");

                Ticket ticket = new Ticket(ticketID, price, status, bookingID, userID, showtimeID, seatID, bookingStatus);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> getTicketsByUser(int userID) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT t.TicketID, t.BookingID, t.Price, t.Status, t.SeatID, t.ShowtimeID "
                + "FROM Tickets t "
                + "JOIN Bookings b ON t.BookingID = b.BookingID "
                + "WHERE b.UserID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(new Ticket(
                            rs.getInt("TicketID"),
                            rs.getInt("BookingID"),
                            rs.getDouble("Price"),
                            rs.getString("Status"),
                            rs.getInt("SeatID"),
                            rs.getInt("ShowtimeID")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> getUserBookingHistory(int userID) {
        List<Ticket> history = new ArrayList<>();
        String query = "SELECT \n"
                + "    t.TicketID, \n"
                + "    t.Status AS TicketStatus, \n"
                + "    t.Price, \n"
                + "    s.SeatNumber, \n"
                + "    st.StartTime, \n"
                + "    m.Title AS MovieTitle\n"
                + "FROM Tickets t\n"
                + "JOIN Bookings b ON t.BookingID = b.BookingID\n"
                + "JOIN Seats s ON b.SeatID = s.SeatID\n"
                + "JOIN Showtimes st ON b.ShowtimeID = st.ShowtimeID\n"
                + "JOIN Movies m ON st.MovieID = m.MovieID\n"
                + "WHERE b.UserID = ?;";

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = new Ticket(
                            rs.getInt("TicketID"),
                            rs.getString("TicketStatus"), // Đổi từ Status sang TicketStatus
                            rs.getDouble("Price"),
                            rs.getString("SeatNumber"),
                            rs.getTimestamp("StartTime").toLocalDateTime(),
                            rs.getString("MovieTitle")
                    );
                    history.add(ticket);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
    
    public int getNextTicketId() {
    int nextId = 1;
    String query = "SELECT COALESCE(MAX(TicketID), 0) + 1 AS nextId FROM Tickets"; // Xử lý NULL
    try (PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            nextId = rs.getInt("nextId");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return nextId;
}

    
   public boolean addTicket(int bookingID, double price, String status) {
    // Lấy TicketID tiếp theo
    int ticketID = getNextTicketId(); 

    String insertSQL = "INSERT INTO Tickets (TicketID, BookingID, Price, Status) VALUES (?, ?, ?, ?)";

    try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
        insertStmt.setInt(1, ticketID);
        insertStmt.setInt(2, bookingID);
        insertStmt.setDouble(3, price);
        insertStmt.setString(4, status);

        return insertStmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


     

    // 🟡 Cập nhật trạng thái vé
    public boolean updateTicket(int ticketID, String status) {
        String sql = "UPDATE Tickets SET Status = ? WHERE TicketID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, ticketID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    // 🔴 Xóa vé
    public boolean deleteTicket(int ticketID) {
        String sql = "DELETE FROM Tickets WHERE TicketID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public Ticket getTicketDetails(int ticketID) {
        String query = "SELECT * FROM Tickets WHERE TicketID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ticketID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(
                            rs.getInt("TicketID"),
                            rs.getInt("BookingID"),
                            rs.getDouble("Price"),
                            rs.getString("Status"),
                            rs.getInt("SeatID"),
                            rs.getInt("ShowtimeID")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkSeatAvailability(int seatID, int showtimeID) {
        String query = "SELECT COUNT(*) FROM Tickets WHERE SeatID = ? AND ShowtimeID = ? AND Status = 'Booked'";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, seatID);
            ps.setInt(2, showtimeID);
            try ( ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean cancelTicket(int ticketID) {
        String query = "UPDATE Tickets SET Status = 'Canceled' WHERE TicketID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ticketID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

}
