package com.daominh.quickmem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.Group;

import java.util.ArrayList;

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

    //get a classes owner by user_id
    public ArrayList<Group> getClassesOwnedByUser(String id) {
        ArrayList<Group> groups = new ArrayList<>();
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(QMDatabaseHelper.TABLE_CLASSES, null, "user_id = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Group group = new Group();
                group.setId(cursor.getString(0));
                group.setName(cursor.getString(1));
                group.setDescription(cursor.getString(2));
                group.setUser_id(cursor.getString(3));
                group.setCreated_at(cursor.getString(4));
                group.setUpdated_at(cursor.getString(5));
                group.setStatus(cursor.getInt(6));
                groups.add(group);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return groups;

    }

    //get classes user is member of
    public ArrayList<Group> getClassesUserIsMemberOf(String id) {
        ArrayList<Group> classes = new ArrayList<>();
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        String query = "SELECT classes.* FROM " + QMDatabaseHelper.TABLE_CLASSES + " INNER JOIN " + QMDatabaseHelper.TABLE_CLASSES_USERS + " ON " +
                QMDatabaseHelper.TABLE_CLASSES + ".id = " + QMDatabaseHelper.TABLE_CLASSES_USERS + ".class_id WHERE " + QMDatabaseHelper.TABLE_CLASSES_USERS + ".user_id = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{id});
        if (cursor.moveToFirst()) {
            do {
                Group group = new Group();
                group.setId(cursor.getString(0));
                group.setName(cursor.getString(1));
                group.setDescription(cursor.getString(2));
                group.setUser_id(cursor.getString(3));
                group.setCreated_at(cursor.getString(4));
                group.setUpdated_at(cursor.getString(5));
                group.setStatus(cursor.getInt(6));
                classes.add(group);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return classes;

    }

    //get number member in class by id class
    public int getNumberMemberInClass(String id) {
        int number = 0;
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM " + QMDatabaseHelper.TABLE_CLASSES_USERS + " WHERE " + QMDatabaseHelper.TABLE_CLASSES_USERS + ".class_id = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{id});
        if (cursor.moveToFirst()) {
            do {
                number = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return number;
    }

    //get number flashcard in class by id class
    public int getNumberFlashCardInClass(String id) {
        int number = 0;
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM " + QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS + " WHERE " + QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS + ".class_id = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{id});
        if (cursor.moveToFirst()) {
            do {
                number = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return number;
    }

    //get all classes
    public ArrayList<Group> getAllClasses() {
        ArrayList<Group> classes = new ArrayList<>();
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(QMDatabaseHelper.TABLE_CLASSES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Group group = new Group();
                group.setId(cursor.getString(0));
                group.setName(cursor.getString(1));
                group.setDescription(cursor.getString(2));
                group.setUser_id(cursor.getString(3));
                group.setCreated_at(cursor.getString(4));
                group.setUpdated_at(cursor.getString(5));
                group.setStatus(cursor.getInt(6));
                classes.add(group);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return classes;
    }

    //add flashcard to class
    public long addFlashCardToClass(String class_id, String flashcard_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("class_id", class_id);
        contentValues.put("flashcard_id", flashcard_id);

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS, null, contentValues);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }


}
