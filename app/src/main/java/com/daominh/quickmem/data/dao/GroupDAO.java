package com.daominh.quickmem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.Group;

import java.util.ArrayList;

public class GroupDAO {
    private final QMDatabaseHelper qmDatabaseHelper;

    public GroupDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    public long insertGroup(Group group) {
        SQLiteDatabase sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = createContentValues(group);
        long result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_CLASSES, null, contentValues);
        sqLiteDatabase.close();
        return result;
    }

    public ArrayList<Group> getClassesOwnedByUser(String id) {
        return getGroupsByQuery(QMDatabaseHelper.TABLE_CLASSES, "user_id = ?", new String[]{id});
    }

    public ArrayList<Group> getClassesUserIsMemberOf(String id) {
        String query = "SELECT classes.* FROM " + QMDatabaseHelper.TABLE_CLASSES + " INNER JOIN " + QMDatabaseHelper.TABLE_CLASSES_USERS + " ON " +
                QMDatabaseHelper.TABLE_CLASSES + ".id = " + QMDatabaseHelper.TABLE_CLASSES_USERS + ".class_id WHERE " + QMDatabaseHelper.TABLE_CLASSES_USERS + ".user_id = ?";
        return getGroupsByQuery(query, id, new String[]{id});
    }

    public int getNumberMemberInClass(String id) {
        return getCountByQuery(QMDatabaseHelper.TABLE_CLASSES_USERS, ".class_id = ?", new String[]{id});
    }

    public int getNumberFlashCardInClass(String id) {
        return getCountByQuery(QMDatabaseHelper.TABLE_CLASSES_FLASHCARDS, ".class_id = ?", new String[]{id});
    }

    public ArrayList<Group> getAllClasses() {
        return getGroupsByQuery(QMDatabaseHelper.TABLE_CLASSES, null, null);
    }

    private ContentValues createContentValues(Group group) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", group.getId());
        contentValues.put("name", group.getName());
        contentValues.put("description", group.getDescription());
        contentValues.put("user_id", group.getUser_id());
        contentValues.put("created_at", group.getCreated_at());
        contentValues.put("updated_at", group.getUpdated_at());
        contentValues.put("status", group.getStatus());
        return contentValues;
    }

    private ArrayList<Group> getGroupsByQuery(String query, String selection, String[] selectionArgs) {
        ArrayList<Group> groups = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(query, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                groups.add(createGroupFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return groups;
    }

    private Group createGroupFromCursor(Cursor cursor) {
        Group group = new Group();
        group.setId(cursor.getString(0));
        group.setName(cursor.getString(1));
        group.setDescription(cursor.getString(2));
        group.setUser_id(cursor.getString(3));
        group.setCreated_at(cursor.getString(4));
        group.setUpdated_at(cursor.getString(5));
        group.setStatus(cursor.getInt(6));
        return group;
    }

    private int getCountByQuery(String table, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + selection;
        Cursor cursor = sqLiteDatabase.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return count;
    }
}