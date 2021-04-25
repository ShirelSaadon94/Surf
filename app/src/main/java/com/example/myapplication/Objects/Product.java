package com.example.myapplication.Objects;

import java.util.Date;
import java.util.UUID;

public class Product {
    String key="";
    String name;
    String category;
    String state;
    String price;
    String description;
    String pictures;
    Date date;

    public Product() {
    }

    public Product(String name, String category, String state, String price, String description) {
        this.name = name;
        this.category = category;
        this.state = state;
        this.price = price;
        this.description = description;
        this.key = UUID.randomUUID().toString();
        this.date= new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
