package com.example.myapplication.Objects;


import java.util.Date;

public class DayForecast {
    private String day;
    private int Wave;
    private Date d;

    public DayForecast(String day, int wave, Date d) {
        day = day;
        Wave = wave;
        this.d = d;
    }

    public java.util.Date getD() {
        return d;
    }

    public void setD(java.util.Date d) {
        this.d = d;
    }

    public String getDate() {
        return day;
    }

    public void setDate(String date) {
        day = day;
    }

    public int getWave() {
        return Wave;
    }

    public void setWave(int wave) {
        Wave = wave;
    }

    @Override
    public String toString() {
        return "DayForecast{" +
                "Date='" + day + '\'' +
                ", Wave=" + Wave +
                ", d=" + d +
                '}';
    }
}
