package com.example.mynotify;

public class Item {
    String Name,Quantity,Description,Image;

    public Item(){

    }

    public Item(String name, String quantity, String description, String image) {
        Name = name;
        Quantity = quantity;
        Description = description;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
