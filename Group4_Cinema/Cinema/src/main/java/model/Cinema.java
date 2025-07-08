/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nguyen Thanh Long - CE182041
 */
public class Cinema {
    private int CinemaID;
    private String Name;
    private String Location;
    private int TotalRooms;

    public Cinema() {
    }

    public Cinema(int CinemaID, String Name, String Location, int TotalRoom) {
        this.CinemaID = CinemaID;
        this.Name = Name;
        this.Location = Location;
        this.TotalRooms = TotalRoom;
    }

    public Cinema(int CinemaID, String Name, String Location) {
        this.CinemaID = CinemaID;
        this.Name = Name;
        this.Location = Location;
    }

    public int getCinemaID() {
        return CinemaID;
    }

    public void setCinemaID(int CinemaID) {
        this.CinemaID = CinemaID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public int getTotalRoom() {
        return TotalRooms;
    }

    public void setTotalRoom(int TotalRoom) {
        this.TotalRooms = TotalRoom;
    }
    
}
