package com.example.myapplication;

import androidx.annotation.NonNull;

public class Listing {

    private String itemName;
    private double price;
    private String itemDescription;
    private String email;

    public Listing() {
    }

    public Listing(String itemName, double price, String itemDescription, String email) {
        this.itemName = itemName;
        this.price = price;
        this.itemDescription = itemDescription;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @NonNull
    @Override
    public String toString() {
        return this.itemName + " $" + this.price;
    }
}
