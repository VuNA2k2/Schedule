package com.example.schedule.Controller;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public void checkEmptyException(String s) throws MyException.EmptyException {
        if(s.length() == 0) throw new MyException.EmptyException();
    }


}
