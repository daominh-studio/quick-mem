package com.daominh.quickmem.ui.activities.profile.change;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.databinding.ActivityChangeEmailBinding;
import com.daominh.quickmem.databinding.ActivityChangeUsernameBinding;
import com.daominh.quickmem.databinding.ActivitySignupBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.profile.SettingsActivity;

public class ChangeEmailActivity extends AppCompatActivity {
    private ActivityChangeEmailBinding binding;
    private UserSharePreferences userSharePreferences;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeEmailBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handleEmailTextChanged(charSequence.toString(), binding);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                handleEmailTextChanged(editable.toString(), binding);
            }
        });
    }

    private void updateEmail() {
        userDAO = new UserDAO(this);
        userSharePreferences = new UserSharePreferences(this);
        String id = userSharePreferences.getId();
        final String email = binding.emailEt.getText().toString();
        if (!handleEmailTextChanged(email, binding)) return;
        if (email.isEmpty()) {
            binding.textIL.setHelperText(getString(R.string.email_is_empty));
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.emailEt.requestFocus();
        } else if (userDAO.updateEmailUser(id, email) > 0) {
            userSharePreferences.setEmail(email);
            Toast.makeText(this, "Change email SUCCESS", Toast.LENGTH_SHORT).show();
            getOnBackPressedDispatcher().onBackPressed();
        } else {
            Toast.makeText(this, "Change username UNSUCCESS", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean handleEmailTextChanged(String text, ActivityChangeEmailBinding binding) {
        if (text.isEmpty()) {
            binding.textIL.setHelperText(getString(R.string.email_is_empty));
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.emailEt.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            binding.textIL.setHelperText(getString(R.string.email_is_invalid));
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.emailEt.requestFocus();
            return false;
        } else if (isEmailExist(text)) {
            binding.textIL.setHelperText(getString(R.string.email_is_exist));
            binding.textIL.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.emailEt.requestFocus();
        } else {
            binding.textIL.setHelperText("");
            return true;
        }
        return false;
    }

    private boolean isEmailExist(String email) {
        userDAO = new UserDAO(this);
        return userDAO.checkEmail(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            // code save
            updateEmail();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}