package com.example.myapplication.Objects;

import java.util.Date;

public class WaveForecast {
    private Date date;
    private int windSpeed;
    private int swel;
    private int waterTemp;
    private int swellDirection;
    private int windDirection;
    private int maxWaveHeight;
    private int minWaveHeight;

    public WaveForecast() {
    }

    @Override
    public String toString() {
        return "WaveForecast{" +
                "date=" + date +
                ", windSpeed=" + windSpeed +
                ", swel=" + swel +
                ", waterTemp=" + waterTemp +
                ", swellDirection=" + swellDirection +
                ", windDirection=" + windDirection +
                ", maxWaveHeight=" + maxWaveHeight +
                ", minWaveHeight=" + minWaveHeight +
                '}';
    }

    public WaveForecast(Date date, int windSpeed, int swel, int waterTemp, int swellDirection, int windDirection, int maxWaveHeight, int minWaveHeight) {
        this.date = date;
        this.windSpeed = windSpeed;
        this.swel = swel;
        this.waterTemp = waterTemp;
        this.swellDirection = swellDirection;
        this.windDirection = windDirection;
        this.maxWaveHeight = maxWaveHeight;
        this.minWaveHeight = minWaveHeight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getSwel() {
        return swel;
    }

    public void setSwel(int swel) {
        this.swel = swel;
    }

    public int getWaterTemp() {
        return waterTemp;
    }

    public void setWaterTemp(int waterTemp) {
        this.waterTemp = waterTemp;
    }

    public int getSwellDirection() {
        return swellDirection;
    }

    public void setSwellDirection(int swellDirection) {
        this.swellDirection = swellDirection;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public int getMaxWaveHeight() {
        return maxWaveHeight;
    }

    public void setMaxWaveHeight(int maxWaveHeight) {
        this.maxWaveHeight = maxWaveHeight;
    }

    public int getMinWaveHeight() {
        return minWaveHeight;
    }

    public void setMinWaveHeight(int minWaveHeight) {
        this.minWaveHeight = minWaveHeight;
    }
}
