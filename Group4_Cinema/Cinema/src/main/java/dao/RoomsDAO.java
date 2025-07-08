package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cinema;
import model.Rooms;
import utils.DBContext;

public class RoomsDAO extends DBContext {

    public RoomsDAO() {
        super();
    }
    
    public List<Cinema> getAllCinemas() {
    List<Cinema> cinemaList = new ArrayList<>();
    String sql = "SELECT cinemaID, Name FROM Cinemas";

    try (
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Cinema cinema = new Cinema();
            cinema.setCinemaID(rs.getInt("cinemaID"));
            cinema.setName(rs.getString("Name"));
            cinemaList.add(cinema);
            
            // Debug - In ra console
            System.out.println("Cinema ID: " + cinema.getCinemaID() + ", Name: " + cinema.getName());
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cinemaList;
}



    public int getNextRoomId() {
        int nextId = 1;
        try {
            String query = "SELECT MAX(RoomID) AS maxid FROM Rooms";
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

    public Rooms getRoomById(int roomId) {
        Rooms room = null;
        try {
            String query = "SELECT * FROM Rooms WHERE RoomID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                room = new Rooms(rs.getInt("RoomID"),
                        rs.getInt("CinemaID"),
                        rs.getString("RoomName"),
                        rs.getInt("SeatCapacity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return room;
    }

    public void addRoom(Rooms room) {
        String query = "INSERT INTO Rooms (RoomID, CinemaID, RoomName, SeatCapacity) VALUES (?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, room.getRoomID());
            ps.setInt(2, room.getCinemaID());
            ps.setString(3, room.getRoomName());
            ps.setInt(4, room.getSeatCapacity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRoom(Rooms room) {
        String query = "UPDATE Rooms SET CinemaID = ?, RoomName = ?, SeatCapacity = ? WHERE RoomID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, room.getCinemaID());
            ps.setString(2, room.getRoomName());
            ps.setInt(3, room.getSeatCapacity());
            ps.setInt(4, room.getRoomID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Rooms viewRoom(int roomID) {
        String query = "SELECT * FROM Rooms WHERE RoomID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, roomID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Rooms(
                        rs.getInt("RoomID"),
                        rs.getInt("CinemaID"),
                        rs.getString("RoomName"),
                        rs.getInt("SeatCapacity")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Rooms> getAllRooms() {
        List<Rooms> rooms = new ArrayList<>();
        String query = "SELECT * FROM Rooms r join Cinemas c on r.CinemaID = c.CinemaID";
        try ( PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rooms.add(new Rooms(
                        rs.getInt("RoomID"),
                        rs.getInt("CinemaID"),
                        rs.getString("RoomName"),
                        rs.getInt("SeatCapacity"),
                        rs.getString("Name"),
                        rs.getString("Location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public void deleteRoom(int roomID) {
        String query = "DELETE FROM Rooms WHERE RoomID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, roomID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
