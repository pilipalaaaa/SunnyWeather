package com.sunnyweather.android.logic.model;


import java.util.ArrayList;
import java.util.List;

public class PlaceResponse {
    private String status = new String();
    private List<Place> place = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Place> getPlace() {
        return place;
    }

    public void setPlace(List<Place> place) {
        this.place = place;
    }
}
