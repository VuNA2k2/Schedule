package com.example.schedule.View;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.TextClock;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Application;
import com.example.schedule.Controller.Interface;
import com.example.schedule.Controller.Receiver.AlarmReceiver;
import com.example.schedule.Controller.Util;
import com.example.schedule.Controller.Adapter.DayAdapter;
import com.example.schedule.Database.EventsDatabase;
import com.example.schedule.Model.Day;
import com.example.schedule.Model.Event;
import com.example.schedule.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView listDays;
    private DayAdapter adapter = null;
    private TextClock textClock, textDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadData();
        init();
        Log.e("Exception", "On create");
        //onLongClick();
    }
    private void loadData() {
        Application.getInstance();
        List<Event> events = EventsDatabase.getInstance(this).eventDAO().getListEvents();
        for(Event event : events) {
            switch (event.getDay()) {
                case "Monday":
                    Application.getInstance().getDays().get(0).getEvents().add(event);
                    break;
                case "Tuesday":
                    Application.getInstance().getDays().get(1).getEvents().add(event);
                    break;
                case "Wednesday":
                    Application.getInstance().getDays().get(2).getEvents().add(event);
                    break;
                case "Thursday":
                    Application.getInstance().getDays().get(3).getEvents().add(event);
                    break;
                case "Friday":
                    Application.getInstance().getDays().get(4).getEvents().add(event);
                    break;
                case "Saturday":
                    Application.getInstance().getDays().get(5).getEvents().add(event);
                    break;
                case "Sunday":
                    Application.getInstance().getDays().get(6).getEvents().add(event);
                    break;
                default:
                    break;
            }
        }
    }
    private void init() {
        listDays = (RecyclerView) findViewById(R.id.listDays);
        textClock = findViewById(R.id.textClock);
        textDate = findViewById(R.id.textDate);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listDays.setLayoutManager(linearLayout);
        listDays.setHasFixedSize(true);
        adapter = new DayAdapter(Application.getInstance().getDays(), new Interface.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeState(position);
            }

            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(MainActivity.this, Application.getInstance().getDays().get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setActivity(MainActivity.this);
        adapter.notifyDataSetChanged();
        listDays.setAdapter(adapter);
    }

    public void changeState(int position) {
        Intent intent = new Intent(MainActivity.this, EventActivity.class);
        intent.putExtra("dayName", Application.getInstance().getDays().get(position).getName());
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
