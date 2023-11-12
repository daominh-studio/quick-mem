package com.daominh.quickmem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.User;

public class UserDAO {
    QMDatabaseHelper qmDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public UserDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    //insert user
    public long insertUser(User user) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("id", user.getId());
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        contentValues.put("avatar", user.getAvatar());
        contentValues.put("role", user.getRole());
        contentValues.put("created_at", user.getCreated_at());
        contentValues.put("updated_at", user.getUpdated_at());
        contentValues.put("status", user.getStatus());

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_USERS, null, contentValues);
        } catch (SQLException e) {
            Log.e("UserDAO", "insertUser: " + e);
        } finally {
//            sqLiteDatabase.close();
        }
        return result;
    }

    //check email is exist
    public boolean checkEmail(String email) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE email = '" + email + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                return true;
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "checkEmail: " + e);
        }
        return false;
    }
}
