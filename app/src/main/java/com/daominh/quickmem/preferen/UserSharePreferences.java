package com.daominh.quickmem.preferen;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharePreferences {

    //create variables

    public static final String KEY_CLASS_ID = "class_id";

    private final SharedPreferences sharedPreferences;

    public UserSharePreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    //clear
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
