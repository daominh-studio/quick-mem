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
        contentValues.put("is_public", 0);
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

        // Check if the record already exists
        if (isFlashCardInFolder(folder_id, flashcard_id)) {
            return -1; // or throw an exception
        }

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
        }
        return result;
    }

    //get all flashcards by folder_id join with flashcard table
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

    //get folder by id
    @SuppressLint("Range")
    public Folder getFolderById(String folder_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        Folder folder = new Folder();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_FOLDERS + " WHERE id = '" + folder_id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                folder.setId(cursor.getString(0));
                folder.setName(cursor.getString(1));
                folder.setDescription(cursor.getString(2));
                folder.setUser_id(cursor.getString(3));
                folder.setCreated_at(cursor.getString(4));
                folder.setUpdated_at(cursor.getString(5));
            }
        } catch (SQLException e) {
            Log.e("FolderDAO", "getFolderById: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return folder;
    }

    //check if flashcard is in the folder
    @SuppressLint("Range")
    public boolean isFlashCardInFolder(String folder_id, String flashcard_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        boolean result = false;

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS
                + " WHERE folder_id = '" + folder_id + "'"
                + " AND flashcard_id = '" + flashcard_id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor != null && cursor.getCount() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e("FolderDAO", "isFlashCardInFolder: " + e);
        }
        return result;
    }


    //delete folder
    public long deleteFolder(String folder_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        try {
            sqLiteDatabase.delete(QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS, "folder_id = ?", new String[]{folder_id});
            result = sqLiteDatabase.delete(QMDatabaseHelper.TABLE_FOLDERS, "id = ?", new String[]{folder_id});

        } catch (SQLException e) {
            Log.e("FolderDAO", "deleteFolder: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //update folder
    public long updateFolder(Folder folder) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("name", folder.getName());
        contentValues.put("description", folder.getDescription());
        contentValues.put("updated_at", folder.getUpdated_at());

        //update
        try {
            result = sqLiteDatabase.update(QMDatabaseHelper.TABLE_FOLDERS, contentValues, "id = ?", new String[]{folder.getId()});
        } catch (SQLException e) {
            Log.e("FolderDAO", "updateFolder: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //get all flashcards by folder_id join with a flashcard table return list id flashcard
    @SuppressLint("Range")
    public ArrayList<String> getAllFlashCardIdByFolderId(String folder_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        ArrayList<String> flashCards = new ArrayList<>();

        String query = "SELECT flashcards.id FROM " + QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS
                + " JOIN " + QMDatabaseHelper.TABLE_FLASHCARDS
                + " ON " + QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS + ".flashcard_id = " + QMDatabaseHelper.TABLE_FLASHCARDS + ".id"
                + " WHERE " + QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS + ".folder_id = '" + folder_id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    flashCards.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("FolderDAO", "getAllFlashCardByFolderId: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return flashCards;
    }

    //remove flashcard from folder
    public long removeFlashCardFromFolder(String folder_id, String flashcard_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        try {
            result = sqLiteDatabase.delete(QMDatabaseHelper.TABLE_FOLDERS_FLASHCARDS, "folder_id = ? AND flashcard_id = ?", new String[]{folder_id, flashcard_id});
        } catch (SQLException e) {
            Log.e("FolderDAO", "removeFlashCardFromFolder: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }


}
