/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Seats {

    private int seatID;
    private int roomID;
    private String seatNumber;
    private String status;

    public Seats() {
    }

    public Seats(int seatID, int roomID, String seatNumber, String status) {
        this.seatID = seatID;
        this.roomID = roomID;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public int getSeatID() {
        return seatID;
    }

    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Seats{"
                + "seatID=" + seatID
                + ", roomID=" + roomID
                + ", seatNumber='" + seatNumber + '\''
                + ", status='" + status + '\''
                + '}';
    }
}
