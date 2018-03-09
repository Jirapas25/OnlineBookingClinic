
package com.example.brainwashing.onlinebookingclinic.Models;


import java.io.Serializable;

public class Location implements Serializable {
    private final static long serialVersionUID = -3781945279697370526L;
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
