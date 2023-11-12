package com.daominh.quickmem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.utils.PasswordHasher;

public class UserDAO {
    QMDatabaseHelper qmDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public UserDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    //insert user
    public long insertUser(User user) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("id", user.getId());
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        contentValues.put("avatar", user.getAvatar());
        contentValues.put("role", user.getRole());
        contentValues.put("created_at", user.getCreated_at());
        contentValues.put("updated_at", user.getUpdated_at());
        contentValues.put("status", user.getStatus());

        //insert
        try {
            result = sqLiteDatabase.insert(QMDatabaseHelper.TABLE_USERS, null, contentValues);
        } catch (SQLException e) {
            Log.e("UserDAO", "insertUser: " + e);
        } finally {
//            sqLiteDatabase.close();
        }
        return result;
    }

    //check email is exist
    public boolean checkEmail(String email) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE email = '" + email + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                Log.e("UserDAO", "checkEmail: " + cursor.getCount());
                return true;
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "checkEmail: " + e);
        }
        return false;
    }

    //check if username is existed
    public boolean checkUsername(String username) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE username = '" + username + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                Log.e("UserDAO", "checkUsername: " + cursor.getCount());
                return true;
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "checkUsername: " + e);
        }
        return false;
    }

    //check password with email
    public boolean checkPasswordEmail(String email, String password) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        password = PasswordHasher.hashPassword(password);

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE email = '" + email + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int passwordIndex = cursor.getColumnIndex("password");
                if (passwordIndex != -1) {
                    String hashedPassword = cursor.getString(passwordIndex);
                    assert password != null;
                    Log.e("UserDAOP", "checkPasswordUsername: " + password + " " + hashedPassword + password.equals(hashedPassword));
                    return password.equals(hashedPassword);
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "checkPassword: " + e);
        }
        return false;
    }

    //check password with username
    public boolean checkPasswordUsername(String username, String password) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        password = PasswordHasher.hashPassword(password);

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE username = '" + username + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int passwordIndex = cursor.getColumnIndex("password");
                if (passwordIndex != -1) {
                    String hashedPassword = cursor.getString(passwordIndex);
                    assert password != null;
                    Log.e("UserDAOP", "checkPasswordUsername: " + password + " " + hashedPassword + password.equals(hashedPassword));
                    return password.equals(hashedPassword);
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "checkPassword: " + e);
        }
        return false;
    }

    //get user by email not contain password
    public User getUserByEmail(String email) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE email = '" + email + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int usernameIndex = cursor.getColumnIndex("username");
                int avatarIndex = cursor.getColumnIndex("avatar");
                int roleIndex = cursor.getColumnIndex("role");
                int createdAtIndex = cursor.getColumnIndex("created_at");
                int updatedAtIndex = cursor.getColumnIndex("updated_at");
                int statusIndex = cursor.getColumnIndex("status");

                if (idIndex != -1 && nameIndex != -1 && usernameIndex != -1 && avatarIndex != -1 && roleIndex != -1 && createdAtIndex != -1 && updatedAtIndex != -1 && statusIndex != -1) {
                    String id = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);
                    String username = cursor.getString(usernameIndex);
                    String avatar = cursor.getString(avatarIndex);
                    int role = cursor.getInt(roleIndex);
                    String created_at = cursor.getString(createdAtIndex);
                    String updated_at = cursor.getString(updatedAtIndex);
                    int status = cursor.getInt(statusIndex);
                    Log.e("UserDAO", "getUserByEmail: " + id + " " + name + " " + username + " " + avatar + " " + role + " " + created_at + " " + updated_at + " " + status);

                    return new User(id, name, email, username, null, avatar, role, created_at, updated_at, status);
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getUserByEmail: " + e);
        }
        return null;
    }

    //get email by username
    public String getEmailByUsername(String username) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE username = '" + username + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int emailIndex = cursor.getColumnIndex("email");
                if (emailIndex != -1) {
                    String email = cursor.getString(emailIndex);
                    Log.e("UserDAO", "getEmailByUsername: " + email);
                    return email;
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getEmailByUsername: " + e);
        }
        return null;
    }

}
