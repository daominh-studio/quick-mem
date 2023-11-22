package com.daominh.quickmem.data.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.daominh.quickmem.data.QMDatabaseHelper;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.utils.PasswordHasher;

import java.util.ArrayList;

public class UserDAO {

    private final QMDatabaseHelper qmDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public UserDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    //insert user
    public long insertUser(User user) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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

        try {
            return sqLiteDatabase.insert(QMDatabaseHelper.TABLE_USERS, null, contentValues);
        } catch (SQLException e) {
            Log.e("UserDAO", "insertUser: " + e);
            return 0;
        } finally {
            sqLiteDatabase.close();
        }
    }

    //check email is exist
    public boolean checkEmail(String email) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE email = '" + email + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e("UserDAO", "checkEmail: " + e);
            return false;
        }finally {
            sqLiteDatabase.close();
        }
    }

    //check if username is existed
    public boolean checkUsername(String username) {
        return checkEmail(username);
    }

    //check password with email
    public boolean checkPasswordEmail(String email, String password) {
        return checkPassword(email, password);
    }

    //check password with username
    public boolean checkPasswordUsername(String username, String password) {
        return checkPassword(username, password);
    }

    private boolean checkPassword(String identifier, String password) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        password = PasswordHasher.hashPassword(password);
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE email = '" + identifier + "' OR username = '" + identifier + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int passwordIndex = cursor.getColumnIndex("password");
                if (passwordIndex != -1) {
                    String hashedPassword = cursor.getString(passwordIndex);
                    return password.equals(hashedPassword);
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "checkPassword: " + e);
        }finally {
            sqLiteDatabase.close();
        }
        return false;
    }

    //get user by email not contain password
    public User getUserByEmail(String email) {
        return getUserByIdentifier(email);
    }

    //get email by username
    public String getEmailByUsername(String username) {
        User user = getUserByIdentifier(username);
        return user != null ? user.getEmail() : null;
    }

    @SuppressLint("Range")
    private User getUserByIdentifier(String identifier) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE email = '" + identifier + "' OR username = '" + identifier + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                int role = cursor.getInt(cursor.getColumnIndex("role"));
                String created_at = cursor.getString(cursor.getColumnIndex("created_at"));
                String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));

                return new User(id, name, email, username, null, avatar, role, created_at, updated_at, status);
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getUserByIdentifier: " + e);
        }
        finally {
            sqLiteDatabase.close();
        }
        return null;
    }

    //get avatar by id user
    public String getAvatarUser(String id) {
        User user = getUserByIdentifier(id);
        return user != null ? user.getAvatar() : null;
    }

    //get password by id user
    public String getPasswordUser(String id) {
        User user = getUserByIdentifier(id);
        return user != null ? user.getPassword() : null;
    }

    //update status user by id
    public long updateUser(String id, ContentValues contentValues) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        try {
            return sqLiteDatabase.update(QMDatabaseHelper.TABLE_USERS, contentValues, "id = ?", new String[]{id});
        } catch (SQLException e) {
            Log.e("UserDAO", "updateUser: " + e);
            return 0;
        } finally {
            sqLiteDatabase.close();
        }
    }

    //update email by id user
    public long updateEmailUser(String id, String email) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        return updateUser(id, contentValues);
    }

    //update username by id user
    public long updateUsernameUser(String id, String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        return updateUser(id, contentValues);
    }

    //update password by id user
    public long updatePasswordUser(String id, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        return updateUser(id, contentValues);
    }

    //update status by id user
    public void updateStatusUser(String id, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        updateUser(id, contentValues);
    }

    @SuppressLint("Range")
    //get all user not contain password
    public ArrayList<User> getAllUser() {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS;

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                int role = cursor.getInt(cursor.getColumnIndex("role"));
                String created_at = cursor.getString(cursor.getColumnIndex("created_at"));
                String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));

                users.add(new User(id, name, email, username, null, avatar, role, created_at, updated_at, status));
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getAllUser: " + e);
        }finally {
            sqLiteDatabase.close();
        }
        return users;
    }

    @SuppressLint("Range")
    //get user by id not contain password
    public User getUserById(String id) {

        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE id = '" + id + "'";
        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                int role = cursor.getInt(cursor.getColumnIndex("role"));
                String created_at = cursor.getString(cursor.getColumnIndex("created_at"));
                String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));

                return new User(id, name, email, username, null, avatar, role, created_at, updated_at, status);
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getUserById: " + e);
        }finally {
            sqLiteDatabase.close();
        }
        return null;
    }
}