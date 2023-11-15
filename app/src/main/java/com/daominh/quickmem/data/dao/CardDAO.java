package com.daominh.quickmem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.Card;

public class CardDAO {
    QMDatabaseHelper qmDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public CardDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    //insert card
    public long insertCard(Card card) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("id", card.getId());
        contentValues.put("front", card.getFront());
        contentValues.put("back", card.getBack());
        contentValues.put("flashcard_id", card.getFlashcard_id());
        contentValues.put("created_at", card.getCreated_at());
        contentValues.put("updated_at", card.getUpdated_at());

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_CARDS, null, contentValues);
        } catch (SQLException e) {
            Log.e("CardDAO", "insertCard: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //count card by flashcard_id
    public int countCardByFlashCardId(String flashcard_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_CARDS + " WHERE flashcard_id = '" + flashcard_id + "'";

        int count = 0;

        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            count = cursor.getCount();
        } catch (SQLException e) {
            Log.e("CardDAO", "countCardByFlashCardId: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return count;
    }
}
