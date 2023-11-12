package com.daominh.quickmem.preferen;

import android.content.Context;
import android.content.SharedPreferences;
import com.daominh.quickmem.data.model.User;

public class UserSharePreferences {

    //create variables
    public static final String KEY_LOGIN = "login";
    public static final String KEY_ID = "id";

    private final SharedPreferences sharedPreferences;

    public UserSharePreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    //set login
    public void setLogin(boolean login) {
        sharedPreferences.edit().putBoolean(KEY_LOGIN, login).apply();
    }

    //get login
    public boolean getLogin() {
        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    //set id
    public void setId(String id) {
        sharedPreferences.edit().putString(KEY_ID, id).apply();
    }

    //get id
    public String getId() {
        return sharedPreferences.getString(KEY_ID, "");
    }

    public void saveUser(User user) {
        setId(user.getId());
        setLogin(true);
    }
}
