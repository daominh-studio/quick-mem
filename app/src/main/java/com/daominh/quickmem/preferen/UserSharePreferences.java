package com.daominh.quickmem.preferen;

import android.content.Context;
import android.content.SharedPreferences;
import com.daominh.quickmem.data.model.User;

public class UserSharePreferences {

    //create variables
    public static final String KEY_LOGIN = "login";
    public static final String KEY_ID = "id";
    private static final String KEY_AVATAR = "avatar";

    private static final String KEY_USER_NAME = "user_name";

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

    //set avatar
    public void setAvatar(String avatar) {
        sharedPreferences.edit().putString(KEY_AVATAR, avatar).apply();
    }

    //get avatar
    public String getAvatar() {
        return sharedPreferences.getString(KEY_AVATAR, "");
    }

    //set username
    public void setUserName(String userName) {
        sharedPreferences.edit().putString(KEY_USER_NAME, userName).apply();
    }

    //get username
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    //clear
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
