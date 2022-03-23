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
    private static final String path = "com\\example\\schedule\\DATA\\";
    private Util() {
    }

    private static Util instance = null;

    public static Util getInstance() {
        if(instance == null) instance = new Util();
        return instance;
    }

    public <T> List<T> reader(String fileName, Context context) {
        FileInputStream fis = null;
        ObjectInputStream objin = null;
        List<T> list = null;

        try {
            fis = context.openFileInput(path + fileName);
            objin = new ObjectInputStream(fis);
            list = (List<T>) objin.readObject();

        } catch (Exception e) {
            Log.e("Error", "Read failed");
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (objin != null) {
                    objin.close();
                }
            } catch (Exception e) {
            }
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

    public void checkEmptyException(String s) throws MyException.EmptyException {
        if(s.length() == 0) throw new MyException.EmptyException();
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
