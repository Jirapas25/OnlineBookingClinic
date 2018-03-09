package com.example.brainwashing.onlinebookingclinic.Models;

import java.io.Serializable;

public class Disable_time_slot  implements Serializable {
    private final static long serialVersionUID = -4253778683918290398L;
    private String date;
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}