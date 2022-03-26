package com.example.schedule.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "events")
public class Event{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String time; // hh:mm
    private final String day;
    private boolean status;
    private String note;
    private boolean isDone;

    public Event(String name, String time, String day, Boolean status, String note) {
        this.name = name;
        this.time = time;
        this.day = day;
        this.note = note;
        this.status = status;
        this.isDone = false;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
