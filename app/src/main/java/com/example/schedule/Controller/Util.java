package com.example.schedule.Controller;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Util {
    private static final String path = "com\\example\\schedule\\DATA";
    private Util() {
    }

    private static Util instance = null;

    public static Util getInstance() {
        if(instance == null) instance = new Util();
        return instance;
    }

    public <T> List<T> reader(String fileName, Context context) {
        List<T> list = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(path + fileName);
            ObjectInputStream ojs = new ObjectInputStream(fis);
            list = (List<T>) ojs.readObject();
            fis.close();
            ojs.close();
        } catch (Exception e) {
            Log.e("Exception","Read from file failed");
        }
        return list;
    }

    public <T> void writer(String fileName, List<T> list, Context context) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(path + fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("Exception","Write to file failed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//        try {
//            FileOutputStream fos = new FileOutputStream(path + fileName);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(list);
//            oos.close();
//            fos.close();
//        } catch (Exception e) {
//            Log.e("Exception","Write to file failed");
//        }
//    }
}
