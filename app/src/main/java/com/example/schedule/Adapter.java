package com.example.schedule;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Interface;
import com.example.schedule.Model.Day;
import com.example.schedule.Model.Event;

import java.util.ArrayList;
import java.util.List;

public class Adapter {

    public static class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
        private List<Day> days = new ArrayList<>();
        private Context context;
        private MainActivity mainActivity;
        private Interface.ItemClickListener itemClickListener;
        public DayAdapter(List<Day> days, Interface.ItemClickListener itemClickListener) {
            this.days = days;
            this.itemClickListener = itemClickListener;
            mainActivity = new MainActivity();
            notifyDataSetChanged();
        }

        public void setData(List<Day> days) {
            this.days = days;
            notifyDataSetChanged();
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
            holder.txtDay.setText(days.get(position).getName());
            if(days.get(position).getEvents().size() != 0) holder.txtEvent.setText(days.get(position).getEvents().get(0).getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemClickListener.onItemLongClick(view, position);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return days.size();
        }

        public class DayViewHolder extends RecyclerView.ViewHolder{
            private TextView txtDay, txtEvent;
            public DayViewHolder(@NonNull View itemView) {
                super(itemView);
                txtDay = (TextView) itemView.findViewById(R.id.txtDay);
                txtEvent = (TextView) itemView.findViewById(R.id.txtEvent);
            }
        }
    }

    public static class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
        private List<Event> events = new ArrayList<>();
        private Context context;

        public EventAdapter(List<Event> events, Context context) {
            this.events = events;
            this.context = context;
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
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            if(events.get(position) == null) return;
            holder.txtEventName.setText(events.get(position).getName());
            holder.txtEventTime.setText(events.get(position).getTime());
            holder.setItemClickListener(new Interface.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private TextView txtEventName, txtEventTime;
            private Interface.ItemClickListener itemClickListener;
            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                txtEventName = (TextView) itemView.findViewById(R.id.txtEventName);
                txtEventTime = (TextView) itemView.findViewById(R.id.txtEventTime);
            }

            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }

            @Override
            public boolean onLongClick(View view) {
                itemClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }

            public void setItemClickListener(Interface.ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
            }
        }
    }
}
