package com.example.schedule.Database;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.schedule.Model.Event;

import java.util.List;

@Dao
public interface EventDAO {
    @Insert
    void insertEvent(Event event);

    @Query("SELECT * FROM events")
    List<Event> getListEvents();

    @Update
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);
}
