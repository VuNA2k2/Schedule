package com.example.schedule.Controller.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.schedule.Model.Music;
import com.example.schedule.R;
import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends BaseAdapter {
    private List<Music> musics;
    private Activity activity;

    public MusicAdapter(List<Music> musics, Activity activity) {
        this.musics = musics;
        this.activity = activity;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int i) {
        return musics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(activity).inflate(R.layout.music_item, viewGroup, false);
        TextView txtMusicName;
        txtMusicName = view.findViewById(R.id.txtMusicName);
        txtMusicName.setText(musics.get(i).getName());
        return view;
    }
}
