package com.example.schedule.Controller;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Interface;
import com.example.schedule.Model.Day;
import com.example.schedule.Model.Event;
import com.example.schedule.R;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public class Adapter {

    public static class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
        private List<Day> days = new ArrayList<>();
        private Interface.ItemClickListener itemClickListener;
        private Activity activity;
        public DayAdapter(List<Day> days, Interface.ItemClickListener itemClickListener) {
            this.days = days;
            this.itemClickListener = itemClickListener;
            notifyDataSetChanged();
        }

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

        @Override
        public void onBindViewHolder(@NonNull DayViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if(days.get(position) == null) return;

            // set day
            holder.txtDay.setText(days.get(position).getName());

            // set list event mini
            List<Event> tmp = new ArrayList<>();
            for(Event event : days.get(position).getEvents()) {
                if(event.getStatus() == true) tmp.add(event);
            }
            EventMiniAdapter adapter = new EventMiniAdapter(tmp, activity);
            holder.spMiniEvent.setAdapter(adapter);
            
            //set on click
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

            if(tmp.size() == 0) holder.txtMessage.setText("No tasks yet");
            else holder.txtMessage.setText(tmp.size() + " tasks to complete");
        }

        @Override
        public int getItemCount() {
            return days.size();
        }

        public class DayViewHolder extends RecyclerView.ViewHolder{
            private TextView txtDay, txtMessage;
            private Spinner spMiniEvent;
            public DayViewHolder(@NonNull View itemView) {
                super(itemView);
                txtDay = (TextView) itemView.findViewById(R.id.txtDay);
                spMiniEvent = (Spinner) itemView.findViewById(R.id.spMiniEvent);
                txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            }
        }
    }

    public static class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
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
            if(events.get(position) == null) return;
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
            holder.swEventOnOff.setChecked(events.get(position).getStatus());
            holder.swEventOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    itemChangeStatus.changeStatus(position, b);
                }
            });
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder{
            private TextView txtEventName, txtEventTime;
            private Switch swEventOnOff;
            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                txtEventName = (TextView) itemView.findViewById(R.id.txtEventName);
                txtEventTime = (TextView) itemView.findViewById(R.id.txtEventTime);
                swEventOnOff = (Switch) itemView.findViewById(R.id.swEventOnOff);
            }
        }
    }
    public static class EventMiniAdapter extends BaseAdapter {

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
}
