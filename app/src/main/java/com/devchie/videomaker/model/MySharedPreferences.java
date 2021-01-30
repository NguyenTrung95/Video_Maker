package com.devchie.videomaker.model;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static MySharedPreferences instance;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private MySharedPreferences(Context context) {
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("my_data", 0);
        this.sharedPreferences = sharedPreferences2;
        this.editor = sharedPreferences2.edit();
    }

    public static MySharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences(context);
        }
        return instance;
    }

    public void putBoolean(String str, boolean z) {
        this.editor.putBoolean(str, z);
        this.editor.commit();
    }

    public boolean getBoolean(String str, boolean z) {
        return this.sharedPreferences.getBoolean(str, z);
    }
}
