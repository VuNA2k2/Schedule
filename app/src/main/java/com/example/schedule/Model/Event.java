package com.example.schedule.Model;

import java.io.Serializable;

public class Event implements Serializable {
    private String name;
    private String time; // hh:mm
    private String day;
    private boolean status;

    public Event(String name, String time, String day) {
        this.name = name;
        this.time = time;
        this.day = day;
        status = true;
    }

    public Event(String name, String time, String day, Boolean status) {
        this.name = name;
        this.time = time;
        this.day = day;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
