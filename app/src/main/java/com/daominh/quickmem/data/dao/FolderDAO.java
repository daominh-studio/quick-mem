package com.daominh.quickmem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.Folder;

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
//            sqLiteDatabase.close();
        }
        return result;
    }

}
