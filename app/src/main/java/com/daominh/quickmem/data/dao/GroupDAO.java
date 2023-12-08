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
        contentValues.put("is_public", 0);
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
        Cursor cursor = sqLiteDatabase.query(
                QMDatabaseHelper.TABLE_CLASSES, null, null, null, null, null, null
        );
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

    //remove flashcard from class
    public long removeFlashCardFromClass(String class_id, String flashcard_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        //delete
        try {
            result = sqLiteDatabase.delete(
                    QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS,
                    "class_id = ? AND flashcard_id = ?",
                    new String[]{class_id, flashcard_id}
            );
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //check if flashcard is in class
    public boolean isFlashCardInClass(String class_id, String flashcard_id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        boolean result = false;

        String query = "SELECT * FROM "
                + QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS +
                " WHERE " + QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS + ".class_id = ? AND "
                + QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS + ".flashcard_id = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{class_id, flashcard_id});
        if (cursor.moveToFirst()) {
            result = true;
        }
        cursor.close();
        sqLiteDatabase.close();
        return result;
    }

    //get all flashcards in class return list id flashcard
    public ArrayList<String> getAllFlashCardInClass(String class_id) {
        ArrayList<String> flashcards = new ArrayList<>();
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        String query = "SELECT flashcard_id FROM " + QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS + " WHERE " + QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS + ".class_id = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{class_id});
        if (cursor.moveToFirst()) {
            do {
                flashcards.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return flashcards;
    }

    //get class by id

    public Group getGroupById(String id) {
        Group group = null;
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(QMDatabaseHelper.TABLE_CLASSES, null, "id = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            group = new Group();
            group.setId(cursor.getString(0));
            group.setName(cursor.getString(1));
            group.setDescription(cursor.getString(2));
            group.setUser_id(cursor.getString(3));
            group.setCreated_at(cursor.getString(4));
            group.setUpdated_at(cursor.getString(5));
            group.setStatus(cursor.getInt(6));
        }
        cursor.close();
        sqLiteDatabase.close();
        return group;
    }

    //get only public classes
    public ArrayList<Group> getPublicClasses() {
        ArrayList<Group> classes = new ArrayList<>();
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(QMDatabaseHelper.TABLE_CLASSES, null, "status = ?", new String[]{"1"}, null, null, null);
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

    //delete class contain in table classes_flashcards and classes_users
    public long deleteClass(String id) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        //delete
        try {
            sqLiteDatabase.delete(
                    QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS,
                    "class_id = ?",
                    new String[]{id}
            );
            sqLiteDatabase.delete(
                    QMDatabaseHelper.TABLE_CLASSES_USERS,
                    "class_id = ?",
                    new String[]{id}
            );
            result = sqLiteDatabase.delete(
                    QMDatabaseHelper.TABLE_CLASSES,
                    "id = ?",
                    new String[]{id}
            );
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //update class
    public long updateClass(Group group) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("name", group.getName());
        contentValues.put("description", group.getDescription());
        contentValues.put("status", group.getStatus());
        contentValues.put("updated_at", group.getUpdated_at());

        //update
        try {
            result = sqLiteDatabase.update(
                    QMDatabaseHelper.TABLE_CLASSES,
                    contentValues,
                    "id = ?",
                    new String[]{group.getId()}
            );
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

}
