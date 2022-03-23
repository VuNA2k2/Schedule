package com.example.schedule.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.schedule.Model.Event;

@Database(entities = {Event.class}, version = 2)
public abstract class EventsDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "event.db";
    private static EventsDatabase instance;
    public static synchronized EventsDatabase getInstance(Context context) {
        if(instance == null) instance = Room.databaseBuilder(context, EventsDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        return instance;
    }
    public abstract EventDAO eventDAO();
}
