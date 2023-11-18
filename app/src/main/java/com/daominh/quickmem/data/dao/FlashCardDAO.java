package com.daominh.quickmem.data.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.FlashCard;

import java.util.ArrayList;

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

    //get all flashcard by user_id
    @SuppressLint("Range")
    public ArrayList<FlashCard> getAllFlashCardByUserId(String user_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        ArrayList<FlashCard> flashCards = new ArrayList<>();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_FLASHCARDS + " WHERE user_id = '" + user_id + "' ORDER BY created_at DESC";

        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    FlashCard flashCard = new FlashCard();
                    flashCard.setId(cursor.getString(cursor.getColumnIndex("id")));
                    flashCard.setName(cursor.getString(cursor.getColumnIndex("name")));
                    flashCard.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    flashCard.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    flashCard.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                    flashCard.setUpdated_at(cursor.getString(cursor.getColumnIndex("updated_at")));

                    flashCards.add(flashCard);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "getAllFlashCardByUserId: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
        return flashCards;
    }

    //delete flashcard by id
    public long deleteFlashCardById(String id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        try {
            result = sqLiteDatabase.delete(QMDatabaseHelper.TABLE_FLASHCARDS, "id = ?", new String[]{id});
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "deleteFlashCardById: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }
}
