package com.daominh.quickmem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.FlashCard;

public class FlashCardDAO {
    QMDatabaseHelper qmDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public FlashCardDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    //insert flashcard
    public long insertFlashCard(FlashCard flashcard) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("id", flashcard.getId());
        contentValues.put("name", flashcard.getName());
        contentValues.put("description", flashcard.getDescription());
        contentValues.put("user_id", flashcard.getUser_id());
        contentValues.put("created_at", flashcard.getCreated_at());
        contentValues.put("updated_at", flashcard.getUpdated_at());

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_FLASHCARDS, null, contentValues);
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "insertFlashCard: " + e);
        } finally {
//            sqLiteDatabase.close();
        }
        return result;
    }
}
