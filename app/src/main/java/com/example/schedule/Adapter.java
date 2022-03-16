package com.example.schedule;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Interface;
import com.example.schedule.Model.Day;
import com.example.schedule.Model.Event;

import java.util.ArrayList;
import java.util.List;

public class Adapter {

    public static class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
        private List<Day> days = new ArrayList<>();
        private Interface.ItemClickListener itemClickListener;
        private Context context;
        public DayAdapter(List<Day> days, Interface.ItemClickListener itemClickListener) {
            this.days = days;
            this.itemClickListener = itemClickListener;
            notifyDataSetChanged();
        }

        public void setData(List<Day> days) {
            this.days = days;
            notifyDataSetChanged();
        }

        public void setContext(Context context) {
            this.context = context;
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
            if(days.get(position).getEvents().size() != 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL, false);
                holder.listEventMini.setLayoutManager(linearLayoutManager);
                EventMiniAdapter adapter = new EventMiniAdapter(days.get(position).getEvents(), new Interface.ItemClickListener() {
                    @Override
                    public void onItemClick(int _position) {
                        itemClickListener.onItemClick(position);
                    }

                    @Override
                    public void onItemLongClick(int _position) {

                    }
                });
                holder.listEventMini.setAdapter(adapter);
            }

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

            holder.listEventMini.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return days.size();
        }

        public class DayViewHolder extends RecyclerView.ViewHolder{
            private TextView txtDay;
            private RecyclerView listEventMini;
            private EventMiniAdapter adapter = new EventMiniAdapter();
            public DayViewHolder(@NonNull View itemView) {
                super(itemView);
                txtDay = (TextView) itemView.findViewById(R.id.txtDay);
                listEventMini = (RecyclerView) itemView.findViewById(R.id.listEventMini);
                listEventMini.setAdapter(adapter);
            }
        }
    }

    public static class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
        private List<Event> events = new ArrayList<>();
        private Interface.ItemClickListener itemClickListener;

        public EventAdapter(List<Event> events, Interface.ItemClickListener itemClickListener) {
            this.events = events;
            this.itemClickListener = itemClickListener;
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
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder{
            private TextView txtEventName, txtEventTime;
            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                txtEventName = (TextView) itemView.findViewById(R.id.txtEventName);
                txtEventTime = (TextView) itemView.findViewById(R.id.txtEventTime);
            }
        }
    }
    public static class EventMiniAdapter extends RecyclerView.Adapter<EventMiniAdapter.EventMiniViewHolder> {
        private List<Event> events = new ArrayList<>();
        Interface.ItemClickListener itemClickListener;
        public EventMiniAdapter() {
        }

        public EventMiniAdapter(List<Event> events, Interface.ItemClickListener itemClickListener) {
            this.events = events;
            this.itemClickListener = itemClickListener;
            notifyDataSetChanged();
        }

        public void setData(List<Event> events) {
            this.events = events;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EventMiniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_mini_item, parent, false);
            return new EventMiniViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventMiniViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if(events.get(position) == null) return;
            holder.txtEventMiniName.setText(events.get(position).getName());
            holder.txtEventMiniTime.setText(events.get(position).getTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        public class EventMiniViewHolder extends RecyclerView.ViewHolder {
            private TextView txtEventMiniName, txtEventMiniTime;
            public EventMiniViewHolder(@NonNull View itemView) {
                super(itemView);
                txtEventMiniName = (TextView) itemView.findViewById(R.id.txtEventMiniName);
                txtEventMiniTime = (TextView) itemView.findViewById(R.id.txtEventMiniTime);
            }
        }
    }
}
