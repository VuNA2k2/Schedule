package com.example.schedule.View;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import com.bumptech.glide.Glide;
import com.example.schedule.App;
import com.example.schedule.Controller.Adapter.EventAdapter;
import com.example.schedule.Controller.Adapter.MusicAdapter;
import com.example.schedule.Controller.Application;
import com.example.schedule.Controller.Interface;
import com.example.schedule.Controller.MyException;
import com.example.schedule.Controller.Receiver.AlarmReceiver;
import com.example.schedule.Controller.Util;
import com.example.schedule.Database.EventsDatabase;
import com.example.schedule.Model.Event;
import com.example.schedule.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;


public class EventActivity extends AppCompatActivity{
    //private List<Event> events = new ArrayList<>();
    private RecyclerView listEvent;
    private FloatingActionButton btnAdd;
    private Toolbar topAppBar;
    EventAdapter adapter = null;
    String dayName;
    ImageView img;
    private int index = 0;
    private AlarmManager alarmManager;
    private Intent intent;
    private Calendar calendar;
    private MaterialTimePicker picker;
    private TextView txtTime;
    private EditText edtEventName, edtNote;
    private TextView txtEditTime;
    private EditText edtEditEventName, edtEditNote;
    private Spinner spEditMusic, spMusic;
    private MusicAdapter musicAdapter;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch swStatus;
    private final Map<Integer, PendingIntent> pendingIntentMap = new HashMap<>();
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
        btnAdd = findViewById(R.id.btnAdd);
        listEvent = findViewById(R.id.listEvent);
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
        topAppBar = findViewById(R.id.topAppBar);
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
        swipeToDelete();
        img = findViewById(R.id.img);
        Glide.with(getApplicationContext()).load(R.drawable.first_note).into(img);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        intent = new Intent(EventActivity.this, AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        //calendar.setTimeInMillis(System.currentTimeMillis());
        musicAdapter = new MusicAdapter(Application.getInstance().getMusics(), EventActivity.this);
    }

    private void swipeToDelete() {
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
    }

    private void addEvent() {
        LayoutInflater layoutInflater = LayoutInflater.from(EventActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_add_event, null);
        AlertDialog alertDio = new AlertDialog.Builder(EventActivity.this)
                .setView(view)
                .show();
        alertDio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txtTime = view.findViewById(R.id.txtTime);
        edtEventName = view.findViewById(R.id.edtEventName);
        edtNote = view.findViewById(R.id.edtNote);
        Button btnAddEvent = view.findViewById(R.id.btnSave);

        spMusic = view.findViewById(R.id.spMusic);
        spMusic.setAdapter(musicAdapter);


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(System.currentTimeMillis());
        txtTime.setText(currentTime);

        // set time
        txtTime.setOnClickListener(view1 -> showTimePicker(txtTime));

        btnAddEvent.setOnClickListener(view12 -> {
            try {
                if(edtNote.getText().length() == 0) edtNote.setText("");
                Util.getInstance().checkEmptyException(edtEventName.getText().toString().trim());
                Util.getInstance().checkEmptyException(txtTime.getText().toString().trim());
                Event event = new Event(edtEventName.getText().toString().trim(), txtTime.getText().toString().trim(),dayName,true, edtNote.getText().toString().trim());
                int id = (int)EventsDatabase.getInstance(this).eventDAO().insertEvent(event);
                event.setId(id);
                Application.getInstance().getDays().get(index).getEvents().add(event);
                adapter.setData(Application.getInstance().getDays().get(index).getEvents());
                intent.putExtra("music", Application.getInstance().getMusics().get(spMusic.getSelectedItemPosition()).getId());
                sendData(edtEventName.getText().toString().trim(), edtNote.getText().toString().trim(), String.valueOf(id));
                setAlarmManager(id);
                img.setVisibility(View.INVISIBLE);
                alertDio.dismiss();
            } catch (MyException.EmptyException e) {
                Toast.makeText(EventActivity.this, "Empty! Please re-enter.", Toast.LENGTH_SHORT).show();
            }
        });
        update();
    }

    private void sendData(String data1, String data2, String data3) {
        intent.putExtra("eventName", data1);
        intent.putExtra("eventNote", data2);
        intent.putExtra("eventId", data3);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void setAlarmManager(int rqc) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EventActivity.this, rqc, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntentMap.putIfAbsent(rqc, pendingIntent);
        final int WEEK = 604800000;
        if(System.currentTimeMillis() > calendar.getTimeInMillis()) alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + WEEK, pendingIntentMap.get(rqc));
        else alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentMap.get(rqc));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void showTimePicker(TextView txt) {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(Integer.parseInt(txt.getText().toString().substring(0,2)))
                .setMinute(Integer.parseInt(txt.getText().toString().substring(3,5)))
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(),"Schedule");

        picker.addOnPositiveButtonClickListener(v -> {
            txt.setText(String.format("%02d", picker.getHour()) + ":" + String.format("%02d", picker.getMinute()));

            setCalendar(picker.getHour(), picker.getMinute());
        });
    }

    private void setCalendar(int hourOfDay, int minute) {
        switch (dayName) {
            case "Monday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case "Tuesday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case "Wednesday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case "Thursday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case "Friday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case "Saturday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
            case "Sunday":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            default:
                break;
        }
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
    }

    @SuppressLint("SetTextI18n")
    private void editEvent(int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(EventActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_edit_event, null);
        AlertDialog alertDio = new AlertDialog.Builder(EventActivity.this)
                .setView(view)
                .show();
        alertDio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txtEditTime = view.findViewById(R.id.txtEditTime);
        edtEditEventName = view.findViewById(R.id.edtEditEventName);
        edtEditNote = view.findViewById(R.id.edtEditNote);
        Button btnSave = view.findViewById(R.id.btnSave);
        swStatus = view.findViewById(R.id.swEditStatus);

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


        spEditMusic = view.findViewById(R.id.spEditMusic);
        spEditMusic.setAdapter(musicAdapter);

        txtEditTime.setOnClickListener(view1 -> showTimePicker(txtEditTime));

        btnSave.setOnClickListener(view12 -> {
            try {
                if(edtEditNote.getText().length() == 0) edtEditNote.setText("");
                Util.getInstance().checkEmptyException(edtEditEventName.getText().toString().trim());
                Util.getInstance().checkEmptyException(txtEditTime.getText().toString().trim());
                Application.getInstance().getDays().get(index).getEvents().get(position).setName(edtEditEventName.getText().toString().trim());
                Application.getInstance().getDays().get(index).getEvents().get(position).setTime(txtEditTime.getText().toString().trim());
                Application.getInstance().getDays().get(index).getEvents().get(position).setNote(edtEditNote.getText().toString().trim());
                EventsDatabase.getInstance(EventActivity.this).eventDAO().updateEvent( Application.getInstance().getDays().get(index).getEvents().get(position));
                adapter.setData(Application.getInstance().getDays().get(index).getEvents());
                intent.putExtra("music", Application.getInstance().getMusics().get(spEditMusic.getSelectedItemPosition()).getId());
                sendData(edtEditEventName.getText().toString().trim(), edtEditNote.getText().toString().trim(), String.valueOf( Application.getInstance().getDays().get(index).getEvents().get(position).getId()));
                if(swStatus.isChecked()) setAlarmManager(Application.getInstance().getDays().get(index).getEvents().get(position).getId());
                else if(pendingIntentMap.get(Application.getInstance().getDays().get(index).getEvents().get(position).getId()) != null) alarmManager.cancel(pendingIntentMap.get(Application.getInstance().getDays().get(index).getEvents().get(position).getId()));
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
                .setPositiveButton("Yes", (dialogInterface, id) -> deleteEvent(position))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteEvent(int position) {
        EventsDatabase.getInstance(this).eventDAO().deleteEvent(Application.getInstance().getDays().get(index).getEvents().get(position));
        if(pendingIntentMap.get(Application.getInstance().getDays().get(index).getEvents().get(position).getId()) != null)alarmManager.cancel(pendingIntentMap.get(Application.getInstance().getDays().get(index).getEvents().get(position).getId()));
        Application.getInstance().getDays().get(index).getEvents().remove(position);
        adapter.setData(Application.getInstance().getDays().get(index).getEvents());
        Toast.makeText(EventActivity.this, "Delete successfully!...", Toast.LENGTH_SHORT).show();
        if(Application.getInstance().getDays().get(index).getEvents().size() == 0) img.setVisibility(View.VISIBLE);
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
