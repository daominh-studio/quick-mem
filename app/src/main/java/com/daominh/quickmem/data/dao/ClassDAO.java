package com.daominh.quickmem.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.daominh.quickmem.data.QMDatabaseHelper;

public class ClassDAO {
    QMDatabaseHelper qmDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public ClassDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }
}
