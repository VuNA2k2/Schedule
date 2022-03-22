package com.example.schedule.Controller.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schedule.Model.Event;
import com.example.schedule.R;

import java.util.ArrayList;
import java.util.List;

public class EventMiniAdapter extends BaseAdapter {

    private List<Event> events = new ArrayList<>();
    private Activity activity;

    public EventMiniAdapter(List<Event> events, Activity activity) {
        this.events = events;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(activity).inflate(R.layout.event_mini_item, viewGroup, false);
        TextView txtEventMiniName;
        TextView txtEventMiniTime;
        txtEventMiniName = view.findViewById(R.id.txtEventMiniName);
        txtEventMiniTime = view.findViewById(R.id.txtEventMiniTime);
        txtEventMiniName.setText(events.get(i).getName());
        txtEventMiniTime.setText(events.get(i).getTime());
        return view;
    }
}
