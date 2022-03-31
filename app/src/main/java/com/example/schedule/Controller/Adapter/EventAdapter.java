package com.example.schedule.Controller.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.schedule.Controller.Interface;
import com.example.schedule.Model.Event;
import com.example.schedule.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events;
    private final Interface.ItemClickListener itemClickListener;
    Context context;

    @SuppressLint("NotifyDataSetChanged")
    public EventAdapter(List<Event> events, Interface.ItemClickListener itemClickListener) {
        this.events = events;
        this.itemClickListener = itemClickListener;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
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

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (events.get(position) == null) return;
        holder.txtEventName.setText(events.get(position).getName());
        holder.txtEventTime.setText(events.get(position).getTime());
        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(position));
        holder.itemView.setOnLongClickListener(view -> {
            itemClickListener.onItemLongClick(position);
            return true;
        });

        if(!events.get(position).getStatus()) holder.txtStatus.setText("");
        else if(events.get(position).isDone()) holder.txtStatus.setText("COMPLETED");
        else holder.txtStatus.setText("UPCOMING");

        holder.btnMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
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
                                .setNegativeButton("Yes", (dialogInterface, i) -> {
                                    holder.txtStatus.setText("COMPLETED");
                                    events.get(position).setDone(true);
                                })
                                .setPositiveButton("No", null)
                                .show();
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtEventName;
        private final TextView txtEventTime;
        private final TextView txtStatus;
        private final ImageView btnMenu;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEventName = itemView.findViewById(R.id.txtEventName);
            txtEventTime = itemView.findViewById(R.id.txtEventTime);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnMenu = itemView.findViewById(R.id.btnMenu);
        }
    }
}
