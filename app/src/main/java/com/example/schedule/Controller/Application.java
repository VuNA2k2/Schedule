package com.example.schedule.Controller;


import android.util.Log;

import com.example.schedule.Model.Day;
import com.example.schedule.Model.Music;
import com.example.schedule.R;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private List<Day> days = new ArrayList<>();
    private static Application instance;
    private List<Music> musics = new ArrayList<>();

    private Application() {
        days.add(new Day("Monday"));
        days.add(new Day("Tuesday"));
        days.add(new Day("Wednesday"));
        days.add(new Day("Thursday"));
        days.add(new Day("Friday"));
        days.add(new Day("Saturday"));
        days.add(new Day("Sunday"));

        musics.add(new Music("Default", R.raw.sound_strong)); // 1800004
        musics.add(new Music("MixiGaming Remix", R.raw.mixi_remix)); // 1800000
        musics.add(new Music("Strong Music", R.raw.sound_strong)); // 1800004
        musics.add(new Music("Army Normal", R.raw.army_normal)); // 1800003
        musics.add(new Music("Army Remix", R.raw.army_remix)); // 1800002
        musics.add(new Music("Good Morning", R.raw.good_morning)); // 1800001

        for(int i = 0; i < musics.size(); i ++) {
            Log.e("Music", String.valueOf(musics.get(i).getId()));
        }
    }



    public static Application getInstance() {
        if(instance == null) instance = new Application();
        return instance;
    }

    public List<Day> getDays() {
        return days;
    }

    public List<Music> getMusics() {
        return musics;
    }
}
