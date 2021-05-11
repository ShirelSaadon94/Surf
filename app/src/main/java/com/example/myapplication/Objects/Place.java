package com.example.myapplication.Objects;

public class Place {
    private String reference;
    public String name;
    public String isOpen;
    public String distance;

    public Place() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", isOpen='" + isOpen + '\'' +
                ", distance=" + distance +
                '}';
    }
}
