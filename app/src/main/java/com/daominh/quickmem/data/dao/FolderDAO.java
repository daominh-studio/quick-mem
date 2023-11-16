package com.daominh.quickmem.data.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.Folder;

import java.util.ArrayList;

public class FolderDAO {
    QMDatabaseHelper qmDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public FolderDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    //insert folder
    public long insertFolder(Folder folder) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("id", folder.getId());
        contentValues.put("name", folder.getName());
        contentValues.put("description", folder.getDescription());
        contentValues.put("user_id", folder.getUser_id());
        contentValues.put("created_at", folder.getCreated_at());
        contentValues.put("updated_at", folder.getUpdated_at());

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_FOLDERS, null, contentValues);
        } catch (SQLException e) {
            Log.e("FolderDAO", "insertFolder: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //get all folder by user_id
    @SuppressLint("Range")
    public ArrayList<Folder> getAllFolderByUserId(String user_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        ArrayList<Folder> folders = new ArrayList<>();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_FOLDERS + " WHERE user_id = '" + user_id + "' ORDER BY created_at DESC";

        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Folder folder = new Folder();
                    folder.setId(cursor.getString(0));
                    folder.setName(cursor.getString(1));
                    folder.setDescription(cursor.getString(2));
                    folder.setUser_id(cursor.getString(3));
                    folder.setCreated_at(cursor.getString(4));
                    folder.setUpdated_at(cursor.getString(5));
                    folders.add(folder);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("FolderDAO", "getAllFolderByUserId: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
        return folders;
    }

}
