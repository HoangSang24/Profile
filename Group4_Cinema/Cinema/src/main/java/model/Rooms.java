/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nguyen Thanh Long - CE182041
 */
public class Rooms {
    private int RoomID;
    private int CinemaID;
    private String RoomName;
    private int SeatCapacity;
    private String CName;
    private String Location;

    public Rooms(int RoomID, int CinemaID, String RoomName, int SeatCapacity, String CName, String Location) {
        this.RoomID = RoomID;
        this.CinemaID = CinemaID;
        this.RoomName = RoomName;
        this.SeatCapacity = SeatCapacity;
        this.CName = CName;
        this.Location = Location;
    }
    
    
    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public Rooms() {
    }

    public Rooms(int RoomID, int CinemaID, String RoomName, int SeatCapacity) {
        this.RoomID = RoomID;
        this.CinemaID = CinemaID;
        this.RoomName = RoomName;
        this.SeatCapacity = SeatCapacity;
    }

    public int getRoomID() {
        return RoomID;
    }

    public void setRoomID(int RoomID) {
        this.RoomID = RoomID;
    }

    public int getCinemaID() {
        return CinemaID;
    }

    public void setCinemaID(int CinemaID) {
        this.CinemaID = CinemaID;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String RoomName) {
        this.RoomName = RoomName;
    }

    public int getSeatCapacity() {
        return SeatCapacity;
    }

    public void setSeatCapacity(int SeatCapacity) {
        this.SeatCapacity = SeatCapacity;
    }
    
    
    
}
