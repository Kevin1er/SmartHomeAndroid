package com.example.smarthome;

public class Room
{
    private String roomName;

    public Room(){}
    public Room(String roomName)
    {
        this.roomName = roomName;
    }

    //Getter / Setter
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
