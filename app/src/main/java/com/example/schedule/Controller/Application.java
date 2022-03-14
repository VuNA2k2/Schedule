package com.example.schedule.Controller;

import com.example.schedule.Model.Day;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private static List<Day> days = new ArrayList<>();
    private static Application instance;
    private Application() {
        days.add(new Day("Monday"));
        days.add(new Day("Tuesday"));
        days.add(new Day("Wednesday"));
        days.add(new Day("Thursday"));
        days.add(new Day("Friday"));
        days.add(new Day("Saturday"));
        days.add(new Day("Sunday"));
    }

    public static Application getInstance() {
        if(instance == null) instance = new Application();
        return instance;
    }

    public List<Day> getDays() {
        return days;
    }
}
