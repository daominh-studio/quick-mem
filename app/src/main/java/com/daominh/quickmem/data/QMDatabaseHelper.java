package com.daominh.quickmem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QMDatabaseHelper extends SQLiteOpenHelper {
    //create database name and version
    private static final String DATABASE_NAME = "quickmem.db";
    private static final int DATABASE_VERSION = 1;
    //create table name
    public static final String TABLE_USERS = "users";
    public static final String TABLE_CLASSES = "classes";
    public static final String TABLE_FOLDERS = "folders";
    public static final String TABLE_FLASHCARDS = "flashcards";
    public static final String TABLE_CARDS = "cards";
    public static final String TABLE_CLASSES_USERS = "classes_users";
    public static final String TABLE_CLASSES_FLASHCARDS = "classes_flashcards";
    public static final String TABLE_FOLDERS_FLASHCARDS = "folders_flashcards";
    //command
    public static final String COMMAND_CREATE_TABLE = "CREATE TABLE ";
    public static final String COMMAND_DROP_TABLE = "DROP TABLE IF EXISTS ";
    //create sql query
    public static final String CREATE_TABLE_USERS = COMMAND_CREATE_TABLE + TABLE_USERS + " (" +
            "id TEXT PRIMARY KEY UNIQUE, " +
            "name TEXT NOT NULL, " +
            "email TEXT NOT NULL UNIQUE, " +
            "username TEXT NOT NULL UNIQUE, " +
            "password TEXT NOT NULL, " +
            "avatar TEXT, " +
            "role INTEGER NOT NULL, " +
            "created_at TEXT NOT NULL, " +
            "updated_at TEXT NOT NULL, " +
            "status INTEGER NOT NULL" +
            ");";

    public static final String CREATE_TABLE_CLASSES = COMMAND_CREATE_TABLE + TABLE_CLASSES + " (" +
            "id TEXT PRIMARY KEY, " +
            "name TEXT NOT NULL, " +
            "description TEXT, " +
            "user_id TEXT NOT NULL, " +
            "is_public INTEGER NOT NULL, " +
            "created_at TEXT NOT NULL, " +
            "updated_at TEXT NOT NULL, " +
            "status INTEGER NOT NULL, " +
            "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id)" +
            ");";

    public static final String CREATE_TABLE_FOLDERS = COMMAND_CREATE_TABLE + TABLE_FOLDERS + " (" +
            "id TEXT PRIMARY KEY , " +
            "name TEXT NOT NULL, " +
            "description TEXT, " +
            "user_id TEXT NOT NULL, " +
            "is_public INTEGER, " + // Removed NOT NULL
            "created_at TEXT NOT NULL, " +
            "updated_at TEXT NOT NULL" +
            ");";

    public static final String CREATE_TABLE_FLASHCARDS = COMMAND_CREATE_TABLE + TABLE_FLASHCARDS + " (" +
            "id TEXT PRIMARY KEY , " +
            "name TEXT NOT NULL, " +
            "description TEXT, " +
            "user_id TEXT NOT NULL, " +
            "is_public INTEGER NOT NULL, " +
            "created_at TEXT NOT NULL, " +
            "updated_at TEXT NOT NULL," +
            "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id)" +
            ");";

    public static final String CREATE_TABLE_CARDS = COMMAND_CREATE_TABLE + TABLE_CARDS + " (" +
            "id TEXT PRIMARY KEY , " +
            "front TEXT NOT NULL, " +
            "back TEXT NOT NULL, " +
            "flashcard_id TEXT NOT NULL, " +
            "status INTEGER NOT NULL," + //0: not yet, 1 done, 2 studying
            "is_learned INTEGER NOT NULL, " + //0: not yet, 1 done, 2 studying
            "created_at TEXT NOT NULL, " +
            "updated_at TEXT NOT NULL" +
            ");";

    public static final String CREATE_TABLE_CLASSES_USERS = COMMAND_CREATE_TABLE + TABLE_CLASSES_USERS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "class_id INTEGER NOT NULL, " +
            "user_id TEXT NOT NULL, " +
            "FOREIGN KEY(class_id) REFERENCES " + TABLE_CLASSES + "(id), " +
            "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id)" +
            ");";

    public static final String CREATE_TABLE_CLASSES_FLASHCARDS = COMMAND_CREATE_TABLE + TABLE_CLASSES_FLASHCARDS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "class_id INTEGER NOT NULL, " +
            "flashcard_id INTEGER NOT NULL, " +
            "FOREIGN KEY(class_id) REFERENCES " + TABLE_CLASSES + "(id)" +
            ");";

    public static final String CREATE_TABLE_FOLDERS_FLASHCARDS = COMMAND_CREATE_TABLE + TABLE_FOLDERS_FLASHCARDS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "folder_id INTEGER NOT NULL, " +
            "flashcard_id INTEGER NOT NULL ," +
            "FOREIGN KEY(folder_id) REFERENCES " + TABLE_FOLDERS + "(id)," +
            "UNIQUE(folder_id, flashcard_id)" +
            ");";

    //constructor
    public QMDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CLASSES);
        db.execSQL(CREATE_TABLE_FOLDERS);
        db.execSQL(CREATE_TABLE_FLASHCARDS);
        db.execSQL(CREATE_TABLE_CARDS);
        db.execSQL(CREATE_TABLE_CLASSES_USERS);
        db.execSQL(CREATE_TABLE_CLASSES_FLASHCARDS);
        db.execSQL(CREATE_TABLE_FOLDERS_FLASHCARDS);

        db.execSQL("INSERT INTO users VALUES('1','admin','admin12345@gmail.com','admin','41e5653fc7aeb894026d6bb7b2db7f65902b454945fa8fd65a6327047b5277fb','',0,'2023/11/19','2023/11/19',1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //drop table if exists
        db.execSQL(COMMAND_DROP_TABLE + TABLE_USERS);
        db.execSQL(COMMAND_DROP_TABLE + TABLE_CLASSES);
        db.execSQL(COMMAND_DROP_TABLE + TABLE_FOLDERS);
        db.execSQL(COMMAND_DROP_TABLE + TABLE_FLASHCARDS);
        db.execSQL(COMMAND_DROP_TABLE + TABLE_CARDS);
        db.execSQL(COMMAND_DROP_TABLE + TABLE_CLASSES_USERS);
        db.execSQL(COMMAND_DROP_TABLE + TABLE_CLASSES_FLASHCARDS);
        db.execSQL(COMMAND_DROP_TABLE + TABLE_FOLDERS_FLASHCARDS);

        //create table again
        onCreate(db);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
