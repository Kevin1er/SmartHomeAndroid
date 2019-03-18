package com.example.smarthome;

public class Object
{
    private String name;
    private String objectType;
    private String state;
    private String category;
    private int id;
    private int id_room;

    public Object(){}

    public Object(String name, String objectType, String state, String category, int id, int id_room) {
        this.name = name;
        this.objectType = objectType;
        this.state = state;
        this.category = category;
        this.id = id;
        this.id_room = id_room;
    }

    //Getters
    public String getName() { return name; }
    public String getObjectType() {
        return objectType;
    }
    public String getState() {
        return state;
    }
    public String getCategory() { return category; }
    public int getId() { return id; }
    public int getId_room() { return id_room; }

    //Setters
    public void setName(String name) { this.name = name; }
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setCategory(String category) { this.category = category; }
    public void setId(int id) { this.id = id; }
    public void setId_room(int id_room) { this.id_room = id_room; }
}
