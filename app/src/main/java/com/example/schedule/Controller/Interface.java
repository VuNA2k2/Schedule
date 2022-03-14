package com.example.schedule.Controller;

import android.view.View;

public class Interface {
    public static interface ItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
