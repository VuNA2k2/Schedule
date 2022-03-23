package com.example.schedule.Controller.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
    Context context;

    public EventAdapter(List<Event> events, Interface.ItemClickListener itemClickListener) {
        this.events = events;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    public void setData(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
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

        if(!events.get(position).getStatus()) holder.txtStatus.setText("");
        else if(events.get(position).isDone()) holder.txtStatus.setText("COMPLETED");
        else holder.txtStatus.setText("UPCOMING");

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menuDelete:
                                itemClickListener.onItemLongClick(position);
                                break;
                            case R.id.menuUpdate:
                                itemClickListener.onItemClick(position);
                                break;
                            case R.id.menuComplete:
                                new AlertDialog.Builder(context)
                                        .setTitle("Completed the task!..").setMessage("Are you sure you want to mark this task as completed.")
                                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                holder.txtStatus.setText("COMPLETED");
                                                events.get(position).setDone(true);
                                            }
                                        })
                                        .setPositiveButton("No", null)
                                        .show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView txtEventName, txtEventTime, txtStatus;
        private ImageView btnMenu;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEventName = (TextView) itemView.findViewById(R.id.txtEventName);
            txtEventTime = (TextView) itemView.findViewById(R.id.txtEventTime);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            btnMenu = (ImageView) itemView.findViewById(R.id.btnMenu);
        }
    }
}
