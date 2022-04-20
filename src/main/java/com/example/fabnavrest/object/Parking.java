package com.example.fabnavrest.object;

import com.google.type.DateTime;

import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;



public class Parking {

    private enum occupancy {
        EMPTY,
        SOMEWHAT_FULL,
        HALF_FULL,
        ALMOST_FULL,
        FULL
    }

    private String id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer[] rateHistory;
    private Date[] rateTimeHistory;
    private Integer finalRate;
    private boolean disability;
    private occupancy[] occupancyHistory;
    private Date[] occupancyTimeHistory;
    private occupancy finalOccupancy;
    private Boolean[] safetyHistory;
    private Date[] safetyTimeHistory;
    private Boolean finalSafety;

    public Parking(String id, String name, Double latitude, Double longitude) {
        super();
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public Parking() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer[] getRateHistory() {
        return rateHistory;
    }

    public void setRateHistory(Integer[] rateHistory) {
        this.rateHistory = rateHistory;
    }

    public Date[] getRateTimeHistory() {
        return rateTimeHistory;
    }

    public void setRateTimeHistory(Date[] rateTimeHistory) {
        this.rateTimeHistory = rateTimeHistory;
    }

    public Integer getFinalRate() {
        return finalRate;
    }

    public void setFinalRate(Integer finalRate) {
        this.finalRate = finalRate;
    }

    public boolean isDisability() {
        return disability;
    }

    public void setDisability(boolean disability) {
        this.disability = disability;
    }

    public occupancy[] getOccupancyHistory() {
        return occupancyHistory;
    }

    public void setOccupancyHistory(occupancy[] occupancyHistory) {
        this.occupancyHistory = occupancyHistory;
    }

    public Date[] getOccupancyTimeHistory() {
        return occupancyTimeHistory;
    }

    public void setOccupancyTimeHistory(Date[] occupancyTimeHistory) {
        this.occupancyTimeHistory = occupancyTimeHistory;
    }

    public occupancy getFinalOccupancy() {
        return finalOccupancy;
    }

    public void setFinalOccupancy(occupancy finalOccupancy) {
        this.finalOccupancy = finalOccupancy;
    }

    public Boolean[] getSafetyHistory() {
        return safetyHistory;
    }

    public void setSafetyHistory(Boolean[] safetyHistory) {
        this.safetyHistory = safetyHistory;
    }

    public Date[] getSafetyTimeHistory() {
        return safetyTimeHistory;
    }

    public void setSafetyTimeHistory(Date[] safetyTimeHistory) {
        this.safetyTimeHistory = safetyTimeHistory;
    }

    public Boolean getFinalSafety() {
        return finalSafety;
    }

    public void setFinalSafety(Boolean finalSafety) {
        this.finalSafety = finalSafety;
    }

}
