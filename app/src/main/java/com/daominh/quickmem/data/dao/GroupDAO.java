package com.daominh.quickmem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.Group;

public class GroupDAO {
    QMDatabaseHelper qmDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public GroupDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    //insert class
    public long insertGroup(Group group) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("id", group.getId());
        contentValues.put("name", group.getName());
        contentValues.put("description", group.getDescription());
        contentValues.put("user_id", group.getUser_id());
        contentValues.put("created_at", group.getCreated_at());
        contentValues.put("updated_at", group.getUpdated_at());
        contentValues.put("status", group.getStatus());

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_CLASSES, null, contentValues);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }
}
