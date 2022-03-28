package com.example.schedule.Model;

import android.net.Uri;

import java.io.Serializable;

public class Music implements Serializable {
    private String name;
    private int id;

    public Music() {
    }

    public Music(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
