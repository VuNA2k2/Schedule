package com.example.schedule.Controller.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Interface;
import com.example.schedule.Model.Event;
import com.example.schedule.R;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events = new ArrayList<>();
    private Interface.ItemClickListener itemClickListener;
    private Interface.ItemChangeStatus itemChangeStatus;

    public EventAdapter(List<Event> events, Interface.ItemClickListener itemClickListener, Interface.ItemChangeStatus itemChangeStatus) {
        this.events = events;
        this.itemClickListener = itemClickListener;
        this.itemChangeStatus = itemChangeStatus;
        notifyDataSetChanged();
    }

    public void setData(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (events.get(position) == null) return;
        holder.txtEventName.setText(events.get(position).getName());
        holder.txtEventTime.setText(events.get(position).getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemClickListener.onItemLongClick(position);
                return true;
            }
        });
        // set status;
        final CheckBox checkBox = holder.swEventOnOff;
        checkBox.setChecked(events.get(position).getStatus());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                itemChangeStatus.changeStatus(position, holder.swEventOnOff.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView txtEventName, txtEventTime;
        private CheckBox swEventOnOff;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEventName = (TextView) itemView.findViewById(R.id.txtEventName);
            txtEventTime = (TextView) itemView.findViewById(R.id.txtEventTime);
            swEventOnOff = (CheckBox) itemView.findViewById(R.id.swEventOnOff);
        }
    }
}
