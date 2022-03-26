package com.example.schedule.Controller.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Interface;
import com.example.schedule.Model.Day;
import com.example.schedule.Model.Event;
import com.example.schedule.R;

import java.util.ArrayList;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<Day> days;
    private final Interface.ItemClickListener itemClickListener;
    private Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public DayAdapter(List<Day> days, Interface.ItemClickListener itemClickListener) {
        this.days = days;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Day> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (days.get(position) == null) return;

        // set day
        holder.txtDay.setText(days.get(position).getName());

        // set list event mini
        List<Event> tmp = new ArrayList<>();
        for (Event event : days.get(position).getEvents()) {
            if (event.getStatus()) tmp.add(event);
        }
        EventMiniAdapter adapter = new EventMiniAdapter(tmp, activity);
        holder.spMiniEvent.setAdapter(adapter);

        //set on click
        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(position));

        holder.itemView.setOnLongClickListener(view -> {
            itemClickListener.onItemLongClick(position);
            return true;
        });

        if (tmp.size() == 0) holder.txtMessage.setText("No tasks yet");
        else holder.txtMessage.setText(tmp.size() + " tasks to complete");
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDay;
        private final TextView txtMessage;
        private final Spinner spMiniEvent;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.txtDay);
            spMiniEvent = itemView.findViewById(R.id.spMiniEvent);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }
}
