package com.daominh.quickmem.ui.activities.profile.change;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.databinding.ActivityChangeUsernameBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.profile.SettingsActivity;

public class ChangeUsernameActivity extends AppCompatActivity {
    private ActivityChangeUsernameBinding binding;
    private UserDAO userDAO;
    private String oldUsername, newUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeUsernameBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.usernameEt.setOnFocusChangeListener((view1, b) -> {
            if (!b){
                validUsername();
            }
        });
    }

    private void updateUsername() {
        userDAO = new UserDAO(this);
        newUsername = binding.usernameEt.getText().toString().trim();
        UserSharePreferences userSharePreferences = new UserSharePreferences(ChangeUsernameActivity.this);
        oldUsername = userSharePreferences.getUserName();
        String id = userSharePreferences.getId();
        validUsername();
        if (binding.textIL.getHelperText() == null){
            if (userDAO.updateUsernameUser(id,newUsername) > 0) {
                userSharePreferences.setUserName(newUsername);
                Toast.makeText(this, "Change username SUCCESS", Toast.LENGTH_SHORT).show();
                getOnBackPressedDispatcher().onBackPressed();
            } else {
                Toast.makeText(this, "Change username UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validUsername() {
        if (newUsername.isEmpty()) {
            binding.textIL.setHelperText("Please enter your new username");
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.usernameEt.requestFocus();
        } else if (newUsername.length() > 15 || newUsername.length() < 5) {
            binding.textIL.setHelperText("Username must be > 5 characters and less than 15 characters");
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.usernameEt.requestFocus();
        } else if (newUsername.contains(" ")){
            binding.textIL.setHelperText("Username cannot contain spaces");
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.usernameEt.requestFocus();
        } else if (newUsername.equals(oldUsername)) {
            binding.textIL.setHelperText("New username must be different with old user name");
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.usernameEt.requestFocus();
        } else if (userDAO.checkUsername(newUsername)) {
            binding.textIL.setHelperText("Username already exists");
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.usernameEt.requestFocus();
        } else {
            binding.textIL.setHelperText(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            // code save
            updateUsername();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}