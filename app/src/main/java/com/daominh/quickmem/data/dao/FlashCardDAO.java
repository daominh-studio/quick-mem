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
        contentValues.put("is_public", flashcard.getIs_public());

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_FLASHCARDS, null, contentValues);
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "insertFlashCard: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //get all flashcards by user_id
    @SuppressLint("Range")
    public ArrayList<FlashCard> getAllFlashCardByUserId(String user_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        ArrayList<FlashCard> flashCards = new ArrayList<>();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_FLASHCARDS + " WHERE user_id = '" + user_id + "' ORDER BY created_at DESC";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {

            if (cursor.moveToFirst()) {
                do {
                    FlashCard flashCard = new FlashCard();
                    flashCard.setId(cursor.getString(cursor.getColumnIndex("id")));
                    flashCard.setName(cursor.getString(cursor.getColumnIndex("name")));
                    flashCard.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    flashCard.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    flashCard.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                    flashCard.setUpdated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
                    flashCard.setIs_public(cursor.getInt(cursor.getColumnIndex("is_public")));

                    flashCards.add(flashCard);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "getAllFlashCardByUserId: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return flashCards;
    }
    public boolean deleteFlashcardAndCards(String flashcardId) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        boolean result = false;

        try {
            sqLiteDatabase.beginTransaction();

            sqLiteDatabase.delete(QMDatabaseHelper.TABLE_CARDS, "flashcard_id = ?", new String[]{flashcardId});
            sqLiteDatabase.delete(QMDatabaseHelper.TABLE_FLASHCARDS, "id = ?", new String[]{flashcardId});

            sqLiteDatabase.setTransactionSuccessful();
            result = true;
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "deleteFlashcardAndCards: " + e);
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
        return result;
    }

    //get flashcard by id
    @SuppressLint("Range")
    public FlashCard getFlashCardById(String id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        FlashCard flashCard = new FlashCard();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_FLASHCARDS + " WHERE id = '" + id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {

            if (cursor.moveToFirst()) {
                do {
                    flashCard.setId(cursor.getString(cursor.getColumnIndex("id")));
                    flashCard.setName(cursor.getString(cursor.getColumnIndex("name")));
                    flashCard.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    flashCard.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    flashCard.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                    flashCard.setUpdated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
                    flashCard.setIs_public(cursor.getInt(cursor.getColumnIndex("is_public")));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "getFlashCardById: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return flashCard;
    }
    // get all flashcard
    @SuppressLint("Range")
    public ArrayList<FlashCard> getAllFlashCard() {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        ArrayList<FlashCard> flashCards = new ArrayList<>();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_FLASHCARDS + " ORDER BY created_at DESC";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {

            if (cursor.moveToFirst()) {
                do {
                    FlashCard flashCard = new FlashCard();
                    flashCard.setId(cursor.getString(cursor.getColumnIndex("id")));
                    flashCard.setName(cursor.getString(cursor.getColumnIndex("name")));
                    flashCard.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    flashCard.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    flashCard.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                    flashCard.setUpdated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
                    flashCard.setIs_public(cursor.getInt(cursor.getColumnIndex("is_public")));

                    flashCards.add(flashCard);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "getAllFlashCardByUserId: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return flashCards;
    }

    //update flashcard
    public long updateFlashCard(FlashCard flashcard) {
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
        contentValues.put("is_public", flashcard.getIs_public());

        //update
        try {
            result = sqLiteDatabase.update(QMDatabaseHelper.TABLE_FLASHCARDS, contentValues, "id = ?", new String[]{flashcard.getId()});
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "updateFlashCard: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //get all flashcard public
    @SuppressLint("Range")
    public ArrayList<FlashCard> getAllFlashCardPublic() {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        ArrayList<FlashCard> flashCards = new ArrayList<>();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_FLASHCARDS + " WHERE is_public = 0 ORDER BY created_at DESC";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {

            if (cursor.moveToFirst()) {
                do {
                    FlashCard flashCard = new FlashCard();
                    flashCard.setId(cursor.getString(cursor.getColumnIndex("id")));
                    flashCard.setName(cursor.getString(cursor.getColumnIndex("name")));
                    flashCard.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    flashCard.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    flashCard.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                    flashCard.setUpdated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
                    flashCard.setIs_public(cursor.getInt(cursor.getColumnIndex("is_public")));

                    flashCards.add(flashCard);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("FlashCardDAO", "getAllFlashCardByUserId: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return flashCards;
    }


}
