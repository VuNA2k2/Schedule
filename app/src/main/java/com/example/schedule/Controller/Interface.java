package com.example.schedule.Controller;

import android.view.View;

public class Interface {
    public static interface ItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
    public static interface ItemChangeStatus {
        void changeStatus(int position, boolean bool);
    }
}
