package com.example.schedule;

import android.hardware.lights.Light;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Application;
import com.example.schedule.Model.Day;
import com.example.schedule.Model.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity{
    private List<Event> events = new ArrayList<>();
    private RecyclerView listEvent;
    private FloatingActionButton btnAdd;
    private Toolbar topAppBar;
    Adapter.EventAdapter adapter = null;
    String dayName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_event_activity);
        init();
        setData();
        onClick();
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        dayName = bundle.getString("dayName");
        if(bundle != null) {
            for(int i = 0; i < bundle.getInt("eventSize"); i ++){
                events.add(new Event(bundle.getString("eventName" + String.valueOf(i)), bundle.getString("eventTime" + String.valueOf(i)), bundle.getString("dayName")));
            }
        }
        adapter.setData(events);
        listEvent.setAdapter(adapter);
        setSupportActionBar(topAppBar);
        getSupportActionBar().setTitle(dayName);
    }

    private void init() {
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        listEvent = (RecyclerView) findViewById(R.id.listEvent);
        adapter = new Adapter.EventAdapter(events, this);
        topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listEvent.setLayoutManager(linearLayout);
    }

    private void onClick() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });
    }

    private void addEvent() {
        events.add(new Event("Doing something", "12:00", dayName));
        for(int i = 0; i < Application.getInstance().getDays().size(); i ++) {
            if(Application.getInstance().getDays().get(i).getName().equals(dayName)) {
                Application.getInstance().getDays().get(i).getEvents().addAll(events);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
