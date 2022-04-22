package com.example.fabnavrest.object;

import java.sql.Timestamp;
import com.google.type.DateTime;

import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;



public class Parking {

    public enum occupancy {
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
    private Long[] rateHistory;
    //private Timestamp[] rateTimeHistory;
    private Long finalRate;
    private boolean disability;
    private occupancy[] occupancyHistory;
    //private Timestamp[] occupancyTimeHistory;
    private occupancy finalOccupancy;
    private Boolean[] safetyHistory;
    //private Timestamp[] safetyTimeHistory;
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

    public Long[] getRateHistory() {
        return rateHistory;
    }

    public void setRateHistory(Long[] rateHistory) {
        this.rateHistory = rateHistory;
    }

    /*public Timestamp[] getRateTimeHistory() {
        return rateTimeHistory;
    }

    public void setRateTimeHistory(Timestamp[] rateTimeHistory) {
        this.rateTimeHistory = rateTimeHistory;
    }*/

    public Long getFinalRate() {
        return finalRate;
    }

    public void setFinalRate(Long finalRate) {
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

    /*public Timestamp[] getOccupancyTimeHistory() {
        return occupancyTimeHistory;
    }

    public void setOccupancyTimeHistory(Timestamp[] occupancyTimeHistory) {
        this.occupancyTimeHistory = occupancyTimeHistory;
    }*/

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

    /*public Timestamp[] getSafetyTimeHistory() {
        return safetyTimeHistory;
    }

    public void setSafetyTimeHistory(Timestamp[] safetyTimeHistory) {
        this.safetyTimeHistory = safetyTimeHistory;
    }*/

    public Boolean getFinalSafety() {
        return finalSafety;
    }

    public void setFinalSafety(Boolean finalSafety) {
        this.finalSafety = finalSafety;
    }

}
