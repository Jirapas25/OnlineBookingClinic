package com.example.brainwashing.onlinebookingclinic.Models;


import java.io.Serializable;
import java.util.List;

public class Booking_time_slots implements Serializable {
    private final static long serialVersionUID = 7587525179371352004L;
    private List<String> fri = null;
    private List<String> mon = null;
    private List<String> sat = null;
    private List<String> sun = null;
    private List<String> thu = null;
    private List<String> tue = null;
    private List<String> wed = null;

    public List<String> getFri() {
        return fri;
    }

    public void setFri(List<String> fri) {
        this.fri = fri;
    }

    public List<String> getMon() {
        return mon;
    }

    public void setMon(List<String> mon) {
        this.mon = mon;
    }

    public List<String> getSat() {
        return sat;
    }

    public void setSat(List<String> sat) {
        this.sat = sat;
    }

    public List<String> getSun() {
        return sun;
    }

    public void setSun(List<String> sun) {
        this.sun = sun;
    }

    public List<String> getThu() {
        return thu;
    }

    public void setThu(List<String> thu) {
        this.thu = thu;
    }

    public List<String> getTue() {
        return tue;
    }

    public void setTue(List<String> tue) {
        this.tue = tue;
    }

    public List<String> getWed() {
        return wed;
    }

    public void setWed(List<String> wed) {
        this.wed = wed;
    }

}
