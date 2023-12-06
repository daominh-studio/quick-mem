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
    private static final String KEY_ROLE = "role_user";
    private static final String KEY_STATUS = "status";
    private static final String KEY_EMAIL = "email";
    public static final String KEY_CLASS_ID = "class_id";

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

    // get, set role user
    public void setRole(int role) {
        sharedPreferences.edit().putInt(KEY_ROLE, role).apply();
    }

    //get role user
    public int getRole() {
        return sharedPreferences.getInt(KEY_ROLE, -1);
    }

    //set,get status
    public void setStatus(int status) {
        sharedPreferences.edit().putInt(KEY_STATUS, status).apply();
    }

    //get status
    public int getStatus() {
        return sharedPreferences.getInt(KEY_STATUS, -1);
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

    //set email
    public void setEmail(String email) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply();
    }

    //get email
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    //set class id
    public void setClassId(String classId) {
        sharedPreferences.edit().putString(KEY_CLASS_ID, classId).apply();
    }

    //get class id
    public String getClassId() {
        return sharedPreferences.getString(KEY_CLASS_ID, "");
    }

    //remove class id
    public void removeClassId() {
        sharedPreferences.edit().remove(KEY_CLASS_ID).apply();
    }

    //set user
    public void saveUser(User user) {
        setId(user.getId());
        setUserName(user.getUsername());
        setEmail(user.getEmail());
        setAvatar(user.getAvatar());
        setRole(user.getRole());
        setStatus(user.getStatus());
        setLogin(true);
    }

    //clear
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
