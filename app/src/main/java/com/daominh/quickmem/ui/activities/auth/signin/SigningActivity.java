package com.daominh.quickmem.ui.activities.auth.signin;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.ActivitySigninBinding;
import com.daominh.quickmem.databinding.ActivitySignupBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.MainActivity;
import com.daominh.quickmem.ui.activities.auth.AuthenticationActivity;
import com.daominh.quickmem.utils.PasswordHasher;

public class SigningActivity extends AppCompatActivity {
    private UserSharePreferences userSharePreferences;
    private User user;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySigninBinding binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //toolbar
        binding.toolbar.setNavigationOnClickListener(v ->
                startActivity(new Intent(SigningActivity.this, AuthenticationActivity.class))
        );

        //sign in by social
        binding.googleBtn.setOnClickListener(v -> {
            intentToMain();
        });
        binding.facebookBtn.setOnClickListener(v -> {
            intentToMain();
        });


        //on text changed
        binding.emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleEmailTextChanged(s.toString(), binding);
            }

            @Override
            public void afterTextChanged(Editable s) {
                handleEmailTextChanged(s.toString(), binding);
            }
        });

        binding.passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handlePasswordTextChanged(s.toString(), binding);
            }

            @Override
            public void afterTextChanged(Editable s) {
                handlePasswordTextChanged(s.toString(), binding);
            }
        });

        binding.signInBtn.setOnClickListener(v -> {
            final String email = binding.emailEt.getText().toString();
            String password = binding.passwordEt.getText().toString();

            if (handleEmailTextChanged(email, binding) && handlePasswordTextChanged(password, binding)) {
                userDAO = new UserDAO(SigningActivity.this);

                int number = 0;
//                password = PasswordHasher.hashPassword(password);

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!userDAO.checkEmail(email)) {
                        Log.e("SigningActivityy", "onCreateE: " + userDAO.checkEmail(email));
                        Toast.makeText(this, "email", Toast.LENGTH_SHORT).show();
                        binding.emailTil.setHelperText(getString(R.string.email_is_not_exist));
                        binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                        return;
                    }
                    if (!userDAO.checkPasswordEmail(email, password)) {
                        binding.passwordTil.setHelperText(getString(R.string.password_is_not_correct));
                        binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                        return;
                    }
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    number = 1;
                    if (!userDAO.checkUsername(email)) {
                        Log.e("SigningActivityy", "onCreateU: " + userDAO.checkUsername(email));
                        binding.emailTil.setHelperText(getString(R.string.user_is_not_exist));
                        binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                        return;
                    }
                    if (!userDAO.checkPasswordUsername(email, password)) {
                        binding.passwordTil.setHelperText(getString(R.string.password_is_not_correct));
                        binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                        return;
                    }
                }
                user = getUser(number, email);
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void intentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean handleEmailTextChanged(String text, ActivitySigninBinding binding) {
        if (text.isEmpty()) {
            binding.emailTil.setHelperText(getString(R.string.email_is_empty));
            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.emailEt.requestFocus();

            return false;
        } else {
            binding.emailTil.setHelperText(getString(R.string.email));
            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));
            return true;
        }
    }

    private boolean handlePasswordTextChanged(String text, ActivitySigninBinding binding) {
        if (text.isEmpty()) {
            binding.passwordTil.setHelperText(getString(R.string.password_is_empty));
            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.passwordEt.requestFocus();

            return false;
        } else {
            binding.passwordTil.setHelperText(getString(R.string.password));
            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));

            return true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SigningActivity.this, AuthenticationActivity.class));
    }

    private User getUser(int number, String input) {
        userDAO = new UserDAO(SigningActivity.this);
        String email = input;
        if (number == 1) {
            email = userDAO.getEmailByUsername(input);
        }
        return user = userDAO.getUserByEmail(email);
    }
}