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

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
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
            sqLiteDatabase.close();
        }
        return folders;
    }

    //add flashcard to folder
    public long addFlashCardToFolder(String folder_id, String flashcard_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("folder_id", folder_id);
        contentValues.put("flashcard_id", flashcard_id);

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS, null, contentValues);
        } catch (SQLException e) {
            Log.e("FolderDAO", "addFlashCardToFolder: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //get all flashcard by folder_id join with flashcard table
    @SuppressLint("Range")
    public ArrayList<FlashCard> getAllFlashCardByFolderId(String folder_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        ArrayList<FlashCard> flashCards = new ArrayList<>();

        String query = "SELECT flashcards.* FROM " + QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS
                + " JOIN " + QMDatabaseHelper.TABLE_FLASHCARDS
                + " ON " + QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS + ".flashcard_id = " + QMDatabaseHelper.TABLE_FLASHCARDS + ".id"
                + " WHERE " + QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS + ".folder_id = '" + folder_id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    FlashCard flashCard = new FlashCard();
                    flashCard.setId(cursor.getString(0));
                    flashCard.setName(cursor.getString(1));
                    flashCard.setDescription(cursor.getString(2));
                    flashCard.setUser_id(cursor.getString(3));
                    flashCard.setCreated_at(cursor.getString(4));
                    flashCard.setUpdated_at(cursor.getString(5));
                    flashCards.add(flashCard);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("FolderDAO", "getAllFlashCardByFolderId: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return flashCards;
    }

}
