/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cinema;
import utils.DBContext;

/**
 *
 * @author Nguyen Thanh Long - CE182041
 */
public class CinemaDAO extends DBContext {

    public CinemaDAO() {
        super();
    }

    public int getNextCinemaId() {
        int nextId = 1;
        try {
            String query = "SELECT MAX(CinemaID) AS maxid FROM Cinemas";
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

    public void addCinema(Cinema cinema) {
        String query = "INSERT INTO Cinemas (CinemaID ,Name, Location, TotalRooms) VALUES (?, ?, ?,?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, cinema.getCinemaID());
            ps.setString(2, cinema.getName());
            ps.setString(3, cinema.getLocation());
            ps.setInt(4, cinema.getTotalRoom());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editCinema(Cinema cinema) {
        String query = "UPDATE Cinemas SET Name=?, Location=?, TotalRooms=? WHERE CinemaID=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, cinema.getName());
            ps.setString(2, cinema.getLocation());
            ps.setInt(3, cinema.getTotalRoom());
            ps.setInt(4, cinema.getCinemaID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cinema viewCinema(int id) {
        String query = "SELECT * FROM Cinemas WHERE CinemaID=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Cinema(
                        rs.getInt("CinemaID"),
                        rs.getString("Name"),
                        rs.getString("Location"),
                        rs.getInt("TotalRooms")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cinema> getAllCinemas() {
        List<Cinema> cinemas = new ArrayList<>();
        String query = "SELECT * FROM Cinemas";
        try ( PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cinemas.add(new Cinema(
                        rs.getInt("CinemaID"),
                        rs.getString("Name"),
                        rs.getString("Location"),
                        rs.getInt("TotalRooms")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cinemas;
    }

    public void deleteCinema(int id) {
        String query = "DELETE FROM Cinemas WHERE CinemaID=?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Cinema getCinemaById(int id) {
    Cinema cinema = null;
    String query = "SELECT * FROM Cinemas WHERE cinemaID = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            cinema = new Cinema(
                rs.getInt("cinemaID"),
                rs.getString("name"),
                rs.getString("location")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cinema;
}

}
