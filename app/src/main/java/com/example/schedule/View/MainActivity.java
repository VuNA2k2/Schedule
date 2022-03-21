package com.example.schedule.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Adapter;
import com.example.schedule.Controller.Application;
import com.example.schedule.Controller.Interface;
import com.example.schedule.Controller.Util;
import com.example.schedule.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView listDays;
    private Adapter.DayAdapter adapter = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        loadData();
        init();
        onClick();
        //onLongClick();
    }

    private void loadData() {
        if(Util.getInstance().reader("Days.DAT",this).size() == 0) {
            Util.getInstance().writer("Days.DAT", Application.getInstance().getDays(), this);
        }
        else {
            Application.getInstance().setDays(Util.getInstance().reader("Days.DAT", this));
        }
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
        adapter.setActivity(MainActivity.this);
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
            intent.putExtra("status" + String.valueOf(i), Application.getInstance().getDays().get(position).getEvents().get(i).getStatus());
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
        Util.getInstance().writer("Days.DAT", Application.getInstance().getDays(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.getInstance().writer("Days.DAT", Application.getInstance().getDays(), MainActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Util.getInstance().writer("Days.DAT", Application.getInstance().getDays(), MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Util.getInstance().writer("Days.DAT", Application.getInstance().getDays(), MainActivity.this);
    }
}
