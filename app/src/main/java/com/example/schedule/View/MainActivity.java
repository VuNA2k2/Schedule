package com.example.schedule.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Application;
import com.example.schedule.Controller.Interface;
import com.example.schedule.Controller.Util;
import com.example.schedule.Controller.Adapter.DayAdapter;
import com.example.schedule.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView listDays;
    private DayAdapter adapter = null;
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

    private void onClick() {

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
