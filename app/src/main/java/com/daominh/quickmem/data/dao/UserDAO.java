package com.daominh.quickmem.data.dao;

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

    QMDatabaseHelper qmDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public UserDAO(Context context) {
        qmDatabaseHelper = new QMDatabaseHelper(context);
    }

    //insert user
    public long insertUser(User user) {
        // First image load

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
            sqLiteDatabase.close();
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

    //get avatar by id user
    public String getAvatarUser(String id) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE id = '" + id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int avatarIndex = cursor.getColumnIndex("avatar");
                if (avatarIndex != -1) {
                    String avatar = cursor.getString(avatarIndex);
                    Log.e("UserDAO", "getAvatarUser: " + avatar);
                    return avatar;
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getAvatarUser: " + e);
        }
        return null;
    }

    //get password by id user
    public String getPasswordUser(String id) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE id = '" + id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int passwordIndex = cursor.getColumnIndex("password");
                if (passwordIndex != -1) {
                    String password = cursor.getString(passwordIndex);
                    Log.e("UserDAO", "getPasswordUser: " + password);
                    return password;
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getPasswordUser: " + e);
        }
        return null;
    }

    //update status user by id
    public long updateStatusUser(String id, int status) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("status", status);

        //update
        try {
            result = sqLiteDatabase.update(QMDatabaseHelper.TABLE_USERS, contentValues, "id = ?", new String[]{id});
        } catch (SQLException e) {
            Log.e("UserDAO", "updateStatusUser: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }


    //update email by id user
    public long updateEmailUser(String id, String email) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("email", email);

        //update
        try {
            result = sqLiteDatabase.update(QMDatabaseHelper.TABLE_USERS, contentValues, "id = ?", new String[]{id});
        } catch (SQLException e) {
            Log.e("UserDAO", "updateEmailUser: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //update username by id user
    public long updateUsernameUser(String id, String username) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("username", username);

        //update
        try {
            result = sqLiteDatabase.update(QMDatabaseHelper.TABLE_USERS, contentValues, "id = ?", new String[]{id});
        } catch (SQLException e) {
            Log.e("UserDAO", "updateUsernameUser: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //update password by id user
    public long updatePasswordUser(String id, String password) {
        sqLiteDatabase = qmDatabaseHelper.getWritableDatabase();

        long result = 0;

        ContentValues contentValues = new ContentValues();

        //put
        contentValues.put("password", password);

        //update
        try {
            result = sqLiteDatabase.update(QMDatabaseHelper.TABLE_USERS, contentValues, "id = ?", new String[]{id});
        } catch (SQLException e) {
            Log.e("UserDAO", "updatePasswordUser: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    //get all user not contain password
    public ArrayList<User> getAllUser() {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        ArrayList<User> users = new ArrayList<>();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS;

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int emailIndex = cursor.getColumnIndex("email");
                    int usernameIndex = cursor.getColumnIndex("username");
                    int avatarIndex = cursor.getColumnIndex("avatar");
                    int roleIndex = cursor.getColumnIndex("role");
                    int createdAtIndex = cursor.getColumnIndex("created_at");
                    int updatedAtIndex = cursor.getColumnIndex("updated_at");
                    int statusIndex = cursor.getColumnIndex("status");

                    if (idIndex != -1 && nameIndex != -1 && emailIndex != -1 && usernameIndex != -1 && avatarIndex != -1 && roleIndex != -1 && createdAtIndex != -1 && updatedAtIndex != -1 && statusIndex != -1) {
                        String id = cursor.getString(idIndex);
                        String name = cursor.getString(nameIndex);
                        String email = cursor.getString(emailIndex);
                        String username = cursor.getString(usernameIndex);
                        String avatar = cursor.getString(avatarIndex);
                        int role = cursor.getInt(roleIndex);
                        String created_at = cursor.getString(createdAtIndex);
                        String updated_at = cursor.getString(updatedAtIndex);
                        int status = cursor.getInt(statusIndex);
                        Log.e("UserDAO", "getAllUser: " + id + " " + name + " " + email + " " + username + " " + avatar + " " + role + " " + created_at + " " + updated_at + " " + status);

                        users.add(new User(id, name, email, username, null, avatar, role, created_at, updated_at, status));
                    }
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getAllUser: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return users;
    }

    //get user by id not contain password
    public User getUserById(String id) {
        sqLiteDatabase = qmDatabaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + QMDatabaseHelper.TABLE_USERS + " WHERE id = '" + id + "'";

        try (Cursor cursor = sqLiteDatabase.rawQuery(query, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int nameIndex = cursor.getColumnIndex("name");
                int emailIndex = cursor.getColumnIndex("email");
                int usernameIndex = cursor.getColumnIndex("username");
                int avatarIndex = cursor.getColumnIndex("avatar");
                int roleIndex = cursor.getColumnIndex("role");
                int createdAtIndex = cursor.getColumnIndex("created_at");
                int updatedAtIndex = cursor.getColumnIndex("updated_at");
                int statusIndex = cursor.getColumnIndex("status");

                if (nameIndex != -1 && emailIndex != -1 && usernameIndex != -1 && avatarIndex != -1 && roleIndex != -1 && createdAtIndex != -1 && updatedAtIndex != -1 && statusIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String email = cursor.getString(emailIndex);
                    String username = cursor.getString(usernameIndex);
                    String avatar = cursor.getString(avatarIndex);
                    int role = cursor.getInt(roleIndex);
                    String created_at = cursor.getString(createdAtIndex);
                    String updated_at = cursor.getString(updatedAtIndex);
                    int status = cursor.getInt(statusIndex);
                    Log.e("UserDAO", "getUserById: " + id + " " + name + " " + email + " " + username + " " + avatar + " " + role + " " + created_at + " " + updated_at + " " + status);

                    return new User(id, name, email, username, null, avatar, role, created_at, updated_at, status);
                }
            }
        } catch (SQLException e) {
            Log.e("UserDAO", "getUserById: " + e);
        } finally {
            sqLiteDatabase.close();
        }
        return null;
    }

}
