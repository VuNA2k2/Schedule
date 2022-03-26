package com.example.schedule.Model;

import java.util.ArrayList;
import java.util.List;

public class Day{
    private String name;
    private List<Event> events = new ArrayList<>();

    public Day(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
