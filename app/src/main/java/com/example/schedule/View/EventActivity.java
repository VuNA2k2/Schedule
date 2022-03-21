package com.example.schedule.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Adapter;
import com.example.schedule.Controller.Application;
import com.example.schedule.Controller.Interface;
import com.example.schedule.Controller.Util;
import com.example.schedule.Model.Event;
import com.example.schedule.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                events.add(new Event(bundle.getString("eventName" + String.valueOf(i)), bundle.getString("eventTime" + String.valueOf(i)), bundle.getString("dayName"), bundle.getBoolean("status" + String.valueOf(i))));
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
        adapter = new Adapter.EventAdapter(events, new Interface.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // doing something
            }

            @Override
            public void onItemLongClick(int position) {
                new AlertDialog.Builder(EventActivity.this)
                        .setIcon(R.drawable.delete_icon)
                        .setTitle("Delete.")
                        .setMessage("Do you want to delete this event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                events.remove(position);
                                adapter.setData(events);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        }, new Interface.ItemChangeStatus() {
            @Override
            public void changeStatus(int position, boolean bool) {
                events.get(position).setStatus(bool);
            }
        });
        topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listEvent.setLayoutManager(linearLayout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SCREEN_ORIENTATION_CHANGED);
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
        adapter.setData(events);
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
        update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        update();
    }

    @Override
    protected void onStop() {
        super.onStop();
        update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        update();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void update() {
        for(int i = 0; i < Application.getInstance().getDays().size(); i ++) {
            if(Application.getInstance().getDays().get(i).getName().equals(dayName)) {
                Application.getInstance().getDays().get(i).setEvents(events);
                Util.getInstance().writer("Days.DAT", Application.getInstance().getDays(), EventActivity.this);
                break;
            }
        }
    }
}