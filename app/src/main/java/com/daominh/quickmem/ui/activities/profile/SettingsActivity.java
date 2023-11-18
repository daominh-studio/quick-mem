package com.daominh.quickmem.ui.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.daominh.quickmem.R;

import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.databinding.ActivitySettingsBinding;

import com.daominh.quickmem.databinding.DialogChangeEmailBinding;
import com.daominh.quickmem.databinding.DialogChangeUsernameBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.auth.signin.SignInActivity;
import com.daominh.quickmem.utils.PasswordHasher;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private UserSharePreferences userSharePreferences;
    private AlertDialog detailDialog;
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        onClickItemSetting();

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void onClickItemSetting() {
        binding.usernameCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogChangeUsername();
            }
        });
        binding.emailCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogChangeEmail();
            }
        });
        binding.passwordCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogChangePassword();
            }
        });

        binding.logOutBtn.setOnClickListener(v -> {
            userSharePreferences = new UserSharePreferences(SettingsActivity.this);
            userSharePreferences.clear();
            startActivity(new Intent(SettingsActivity.this, SignInActivity.class));

        });
    }

    private void openDialogChangePassword() {

    }

    private void openDialogChangeEmail() {
        userDAO = new UserDAO(SettingsActivity.this);
        DialogChangeEmailBinding changeUsernameBinding = DialogChangeEmailBinding.inflate(LayoutInflater.from(SettingsActivity.this));
        View view = changeUsernameBinding.getRoot();

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setView(view);
        detailDialog = builder.create();
        detailDialog.setCanceledOnTouchOutside(false);
        detailDialog.show();
        detailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        changeUsernameBinding.cancelChangeEmailBtn.setOnClickListener(view1 -> detailDialog.dismiss());

        changeUsernameBinding.submitChangeEmailBtn.setOnClickListener(v -> {

            String password = changeUsernameBinding.passwordEt.getText().toString().trim();
            userSharePreferences = new UserSharePreferences(SettingsActivity.this);
            String id = userSharePreferences.getId();
            if (password.isEmpty()) {
                changeUsernameBinding.textIL.setHelperText("Please enter your password");
                return;
            } else if (!PasswordHasher.hashPassword(password).equals(userDAO.getPasswordUser(id))) {
                changeUsernameBinding.textIL.setHelperText("Password is incorrect");
                return;
            } else {
                changeUsernameBinding.textIL.setHelperText("");
            }

        });
    }

    private void openDialogChangeUsername() {
        DialogChangeUsernameBinding changeUsernameBinding = DialogChangeUsernameBinding.inflate(LayoutInflater.from(SettingsActivity.this));
        View view = changeUsernameBinding.getRoot();

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setView(view);
        detailDialog = builder.create();
        detailDialog.setCanceledOnTouchOutside(false);
        detailDialog.show();
        detailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        changeUsernameBinding.cancelChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailDialog.dismiss();
            }
        });
    }
}