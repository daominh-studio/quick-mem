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
import com.daominh.quickmem.databinding.ActivityChangePasswordBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.profile.SettingsActivity;
import com.daominh.quickmem.utils.PasswordHasher;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    private UserDAO userDAO;
    String currentPass, newPass, reNewPass, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        UserSharePreferences userSharePreferences = new UserSharePreferences(this);
        id = userSharePreferences.getId();

        binding.currentPassEt.setOnFocusChangeListener((view1, b) -> {
            if (!b) {
                validCurrentPass();
            }
        });
        binding.newPassEt.setOnFocusChangeListener((view12, b) -> {
            if (!b) {
                validNewPass();
            }
        });
        binding.confirmPassEt.setOnFocusChangeListener((view13, b) -> {
            if (!b) {
                validConfirmPass();
            }
        });
    }

    private void changePassword() {
        validConfirmPass();
        validNewPass();
        validConfirmPass();

        if (binding.currentPassIL.getHelperText() == null &&
                binding.newPassIL.getHelperText() == null &&
                binding.currentPassIL.getHelperText() == null) {

            userDAO = new UserDAO(this);
            String hashedPassword = PasswordHasher.hashPassword(newPass);
            if (userDAO.updatePasswordUser(id, hashedPassword) > 0) {
                Toast.makeText(this, "Change username SUCCESS", Toast.LENGTH_SHORT).show();
                getOnBackPressedDispatcher().onBackPressed();
            } else {
                Toast.makeText(this, "Change username UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validConfirmPass() {
        reNewPass = binding.confirmPassEt.getText().toString().trim();
        if (reNewPass.isEmpty()) {
            binding.confirmPassIL.setHelperText("Please enter your new password");
            binding.confirmPassIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.confirmPassEt.requestFocus();
        } else if (!reNewPass.equals(newPass)) {
            binding.confirmPassIL.setHelperText("Confirm password must be similar new password");
            binding.confirmPassIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.confirmPassEt.requestFocus();
        } else {
            binding.confirmPassIL.setHelperText(null);
        }
    }

    private void validNewPass() {
        newPass = binding.newPassEt.getText().toString().trim();
        if (newPass.isEmpty()) {
            binding.newPassIL.setHelperText("Please enter your new password");
            binding.newPassIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.newPassEt.requestFocus();
        } else if (newPass.length() < 8) {
            binding.newPassIL.setHelperText(getString(R.string.password_is_invalid));
            binding.newPassIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.newPassEt.requestFocus();
        } else if (newPass.contains(" ")) {
            binding.newPassIL.setHelperText("Password cannot contain spaces");
            binding.newPassIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.newPassEt.requestFocus();
        } else {
            binding.newPassIL.setHelperText(null);
        }
    }

    private void validCurrentPass() {
        userDAO = new UserDAO(this);
        currentPass = binding.currentPassEt.getText().toString().trim();

        if (currentPass.isEmpty()) {
            binding.currentPassIL.setHelperText("Please enter your password");
            binding.currentPassIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.currentPassEt.requestFocus();
        } else if (!Objects.equals(PasswordHasher.hashPassword(currentPass), userDAO.getPasswordUser(id))) {
            Toast.makeText(this, "Pass: " + userDAO.getPasswordUser(id), Toast.LENGTH_SHORT).show();
            binding.currentPassIL.setHelperText("Password is incorrect");
            binding.currentPassIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.currentPassEt.requestFocus();
        } else {
            binding.currentPassIL.setHelperText(null);
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
            changePassword();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

}