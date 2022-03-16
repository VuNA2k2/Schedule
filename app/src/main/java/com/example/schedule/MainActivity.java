package com.example.schedule;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Application;
import com.example.schedule.Controller.Interface;
import com.example.schedule.Model.Day;
import com.example.schedule.Model.Event;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView listDays;
    private Adapter.DayAdapter adapter = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
        onClick();
        //onLongClick();
    }
    private void init() {
        listDays = (RecyclerView) findViewById(R.id.listDays);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listDays.setLayoutManager(linearLayout);
        listDays.setHasFixedSize(true);
        adapter = new Adapter.DayAdapter(Application.getInstance().getDays(), new Interface.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeState(position);
            }

            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(MainActivity.this, Application.getInstance().getDays().get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
        listDays.setAdapter(adapter);
    }

    private void onClick() {

    }

    public void changeState(int position) {
        Intent intent = new Intent(MainActivity.this, EventActivity.class);
        intent.putExtra("dayName", Application.getInstance().getDays().get(position).getName());
        intent.putExtra("eventSize", Application.getInstance().getDays().get(position).getEvents().size());
        for(int i = 0; i < Application.getInstance().getDays().get(position).getEvents().size(); i ++) {
            intent.putExtra("eventName" + String.valueOf(i), Application.getInstance().getDays().get(position).getEvents().get(i).getName());
            intent.putExtra("eventTime" + String.valueOf(i), Application.getInstance().getDays().get(position).getEvents().get(i).getTime());
        }
        startActivity(intent);
    }

//    private void onLongClick() {
//        listDays.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                new AlertDialog.Builder(MainActivity.this)
//                        .setIcon(R.drawable.delete_icon)
//                        .setTitle("Delete.")
//                        .setMessage("Do you want to delete this event?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                days.remove(pos);
//                                adapter.notifyDataSetChanged();
//                            }
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
//                listDays.setAdapter(adapter);
//
//                  time picker
//                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//
//                    }
//                },24,60,true).show();
//                return true;
//          }
//        });
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }
}
