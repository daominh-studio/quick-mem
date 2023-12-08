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
        } finally {
            sqLiteDatabase.close();
        }
    }

    //check username exists
    public boolean checkUsername(String username) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE username = '" + username + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e("UserDAO", "checkUsername: " + e);
            return false;
        } finally {
            sqLiteDatabase.close();
        }
    }

    //check if username is existed

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
                    assert password != null;
                    return password.equals(hashedPassword);
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "checkPassword: " + e);
        } finally {
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
        } finally {
            sqLiteDatabase.close();
        }
        return null;
    }

    //get password by id user
    @SuppressLint("Range")
    public String getPasswordUser(String id) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE id = '" + id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndex("password"));
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getPasswordUser: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return null;
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
    //get all user to not contain password
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
        } finally {
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
        } finally {
            sqLiteDatabase.close();
        }
        return null;
    }

    //get list user by id class just need id, username, avatar
    public ArrayList<User> getListUserByIdClass(String idClass) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE id IN (SELECT user_id FROM " + QMDatabaseHelper.TABLE_CLASSES_USERS + " WHERE class_id = '" + idClass + "')";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String avatar = cursor.getString(cursor.getColumnIndex("avatar"));

                users.add(new User(id, null, null, username, null, avatar, 0, null, null, 0));
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getListUserByIdClass: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return users;
    }

    //get all user just need id, username, avatar
    public ArrayList<User> getAllUserJustNeed() {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS;

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                Log.e("UserDAO", "getAllUserJustNeed: " + id + " " + username + " " + avatar);

                users.add(new User(id, null, null, username, null, avatar, 0, null, null, 0));
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getAllUserJustNeed: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        for (User user : users) {
            if (user.getUsername().equals("admin")) {
                users.remove(user);
                break;
            }

        }
        return users;
    }

    //get user by id class just need username, avatar by id
    public User getUserByIdClass(String id) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE id = '" + id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String avatar = cursor.getString(cursor.getColumnIndex("avatar"));

                return new User(id, null, null, username, null, avatar, 0, null, null, 0);
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getUserByIdClass: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return null;
    }

    // check if user is in class
    public boolean checkUserInClass(String userId, String classId) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_CLASSES_USERS + " WHERE user_id = '" + userId + "' AND class_id = '" + classId + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e("UserDAO", "checkUserInClass: " + e);
            return false;
        } finally {
            sqLiteDatabase.close();
        }
    }

    // remove user from class
    public long removeUserFromClass(String userId, String classId) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        String query = "DELETE FROM " + QMDatabaseHelper.TABLE_CLASSES_USERS + " WHERE user_id = '" + userId + "' AND class_id = '" + classId + "'";

        try {
            return sqLiteDatabase.delete(QMDatabaseHelper.TABLE_CLASSES_USERS, "user_id = ? AND class_id = ?", new String[]{userId, classId});
        } catch (SQLException e) {
            Log.e("UserDAO", "removeUserFromClass: " + e);
            return 0;
        } finally {
            sqLiteDatabase.close();
        }
    }

    // add user to class
    public long addUserToClass(String userId, String classId) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("class_id", classId);

        try {
            return sqLiteDatabase.insert(QMDatabaseHelper.TABLE_CLASSES_USERS, null, contentValues);
        } catch (SQLException e) {
            Log.e("UserDAO", "addUserToClass: " + e);
            return 0;
        } finally {
            sqLiteDatabase.close();
        }
    }

}