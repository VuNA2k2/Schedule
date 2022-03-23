package com.example.schedule.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.Controller.Adapter.EventAdapter;
import com.example.schedule.Controller.Application;
import com.example.schedule.Controller.Interface;
import com.example.schedule.Controller.MyException;
import com.example.schedule.Controller.Util;
import com.example.schedule.Database.EventsDatabase;
import com.example.schedule.Model.Event;
import com.example.schedule.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;


public class EventActivity extends AppCompatActivity{
    //private List<Event> events = new ArrayList<>();
    private RecyclerView listEvent;
    private FloatingActionButton btnAdd;
    private Toolbar topAppBar;
    EventAdapter adapter = null;
    String dayName;
    ImageView img;
    private int index = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_event_activity);
        init();
        setData();
        onClick();
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        dayName = bundle.getString("dayName");
        for(int i = 0; i < Application.getInstance().getDays().size(); i ++) {
            if(Application.getInstance().getDays().get(i).getName().equals(dayName)) {
                index = i;
                break;
            }
        }
        //events.addAll(Application.getInstance().getDays().get(index).getEvents());
        adapter.setData(Application.getInstance().getDays().get(index).getEvents());
        listEvent.setAdapter(adapter);
        setSupportActionBar(topAppBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(dayName);

        if(Application.getInstance().getDays().get(index).getEvents().size() != 0) img.setVisibility(View.INVISIBLE);
        else img.setVisibility(View.VISIBLE);
    }

    private void init() {
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        listEvent = (RecyclerView) findViewById(R.id.listEvent);
        adapter = new EventAdapter(Application.getInstance().getDays().get(index).getEvents(), new Interface.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editEvent(position);
            }

            @Override
            public void onItemLongClick(int position) {
                removeEvent(position);
            }
        });
        adapter.setContext(this);
        topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listEvent.setLayoutManager(linearLayout);
        listEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) btnAdd.hide();
                else btnAdd.show();
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteEvent(position);
            }
        });

        itemTouchHelper.attachToRecyclerView(listEvent);

        img = (ImageView) findViewById(R.id.img);
    }

    private void addEvent() {
        LayoutInflater layoutInflater = LayoutInflater.from(EventActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_add_event, null);
        AlertDialog alertDio = new AlertDialog.Builder(EventActivity.this)
                .setView(view)
                .show();
        alertDio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnAddEvent;
        TextView txtTime;
        EditText edtEventName, edtNote;
        txtTime = (TextView) view.findViewById(R.id.txtEditTime);
        edtEventName = (EditText) view.findViewById(R.id.edtEditEventName);
        edtNote = (EditText) view.findViewById(R.id.edtEditNote);
        btnAddEvent = (Button) view.findViewById(R.id.btnSave);

        txtTime.setOnClickListener(view1 -> {
            final int[] lastSelectedHour = new int[1];
            final int[] lastSelectedMinute = new int[1];
            @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog.OnTimeSetListener timeSetListener = (view11, hourOfDay, minute) -> {
                txtTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                lastSelectedHour[0] = hourOfDay;
                lastSelectedMinute[0] = minute;
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this,
                    timeSetListener, lastSelectedHour[0], lastSelectedMinute[0], true);
            timePickerDialog.show();
        });

        btnAddEvent.setOnClickListener(view12 -> {
            try {
                Util.getInstance().checkEmptyException(edtEventName.getText().toString().trim());
                Util.getInstance().checkEmptyException(txtTime.getText().toString().trim());
                Event event = new Event(edtEventName.getText().toString().trim(), txtTime.getText().toString().trim(),dayName,true, edtNote.getText().toString().trim());
                Application.getInstance().getDays().get(index).getEvents().add(event);
                EventsDatabase.getInstance(this).eventDAO().insertEvent(event);
                adapter.setData(Application.getInstance().getDays().get(index).getEvents());
                alertDio.dismiss();
            } catch (MyException.EmptyException e) {
                Toast.makeText(EventActivity.this, "Empty! Please re-enter.", Toast.LENGTH_SHORT).show();
            }
        });
        update();
        img.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void editEvent(int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(EventActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_edit_event, null);
        AlertDialog alertDio = new AlertDialog.Builder(EventActivity.this)
                .setView(view)
                .show();
        alertDio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnSave;
        TextView txtEditTime;
        EditText edtEditEventName, edtEditNote;
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch swStatus;

        txtEditTime = (TextView) view.findViewById(R.id.txtEditTime);
        edtEditEventName = (EditText) view.findViewById(R.id.edtEditEventName);
        edtEditNote = (EditText) view.findViewById(R.id.edtEditNote);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        swStatus = (Switch) view.findViewById(R.id.swEditStatus);

        txtEditTime.setText(Application.getInstance().getDays().get(index).getEvents().get(position).getTime());
        edtEditEventName.setText(Application.getInstance().getDays().get(index).getEvents().get(position).getName());
        edtEditNote.setText(Application.getInstance().getDays().get(index).getEvents().get(position).getNote());
        swStatus.setChecked(Application.getInstance().getDays().get(index).getEvents().get(position).getStatus());
        if(Application.getInstance().getDays().get(index).getEvents().get(position).getStatus()) swStatus.setText("On");
        else swStatus.setText("Off");

        swStatus.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) swStatus.setText("On");
            else swStatus.setText("Off");
            Application.getInstance().getDays().get(index).getEvents().get(position).setStatus(b);
        });

        txtEditTime.setOnClickListener(view1 -> {
            int selectedHour = Integer.parseInt(Application.getInstance().getDays().get(index).getEvents().get(position).getTime().substring(0,2));
            int selectedMinute = Integer.parseInt(Application.getInstance().getDays().get(index).getEvents().get(position).getTime().substring(3,5));
            final int[] lastSelectedHour = new int[1];
            final int[] lastSelectedMinute = new int[1];
            @SuppressLint("DefaultLocale") TimePickerDialog.OnTimeSetListener timeSetListener = (view11, hourOfDay, minute) -> {
                txtEditTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                lastSelectedHour[0] = hourOfDay;
                lastSelectedMinute[0] = minute;
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this,
                    timeSetListener, selectedHour, selectedMinute, true);
            timePickerDialog.show();
        });

        btnSave.setOnClickListener(view12 -> {
            try {
                Util.getInstance().checkEmptyException(edtEditEventName.getText().toString().trim());
                Util.getInstance().checkEmptyException(txtEditTime.getText().toString().trim());
                Application.getInstance().getDays().get(index).getEvents().get(position).setName(edtEditEventName.getText().toString().trim());
                Application.getInstance().getDays().get(index).getEvents().get(position).setTime(txtEditTime.getText().toString().trim());
                Application.getInstance().getDays().get(index).getEvents().get(position).setNote(edtEditNote.getText().toString().trim());
                EventsDatabase.getInstance(EventActivity.this).eventDAO().updateEvent( Application.getInstance().getDays().get(index).getEvents().get(position));
                adapter.setData(Application.getInstance().getDays().get(index).getEvents());
                alertDio.dismiss();
            } catch (MyException.EmptyException e) {
                Toast.makeText(EventActivity.this, "Empty! Please re-enter.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeEvent(int position) {
        new AlertDialog.Builder(EventActivity.this)
                .setIcon(R.drawable.delete_icon)
                .setTitle("Delete.")
                .setMessage("Do you want to delete this event?")
                .setPositiveButton("Yes", (dialogInterface, id) -> {
                    deleteEvent(position);
                })
                .setNegativeButton("No", null)
                .show();
        if(Application.getInstance().getDays().get(index).getEvents().size() == 0) img.setVisibility(View.VISIBLE);
    }

    private void deleteEvent(int position) {
        EventsDatabase.getInstance(this).eventDAO().deleteEvent(Application.getInstance().getDays().get(index).getEvents().get(position));
        Application.getInstance().getDays().get(index).getEvents().remove(position);
        adapter.setData(Application.getInstance().getDays().get(index).getEvents());
        Toast.makeText(EventActivity.this, "Delete successfully!...", Toast.LENGTH_SHORT).show();
    }

    private void onClick() {
        btnAdd.setOnClickListener(view -> addEvent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        update();
    }

    @Override
    protected void onStop() {
        super.onStop();
        update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        update();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void update() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
