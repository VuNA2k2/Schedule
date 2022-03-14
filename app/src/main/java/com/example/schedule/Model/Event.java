package com.example.schedule.Model;

public class Event {
    private String name;
    private String time; // hh:mm
    private String day;

    public Event(String name, String time, String day) {
        this.name = name;
        this.time = time;
        this.day = day;
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
}
