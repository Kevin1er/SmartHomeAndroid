package com.example.smarthome;

public class Category
{
    private String category;

    public Category(){}
    public Category(String category)
    {
        this.category = category;
    }

    //Getter / Setter
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}