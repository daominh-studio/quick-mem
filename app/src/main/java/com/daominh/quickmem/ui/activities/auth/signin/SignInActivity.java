package com.daominh.quickmem.ui.activities.auth.signin;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.ActivitySigninBinding;
import com.daominh.quickmem.databinding.DialogForGotPasswordBinding;
import com.daominh.quickmem.databinding.DialogForgotUsernameBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.MainActivity;
import com.daominh.quickmem.ui.activities.auth.AuthenticationActivity;

public class SignInActivity extends AppCompatActivity {
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
                startActivity(new Intent(SignInActivity.this, AuthenticationActivity.class))
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
                userDAO = new UserDAO(SignInActivity.this);

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
                userSharePreferences = new UserSharePreferences(SignInActivity.this);
                userSharePreferences.saveUser(user);
                userSharePreferences.setLogin(true);
                userSharePreferences.setUserName(user.getUsername());
                userSharePreferences.setAvatar(user.getAvatar());
                intentToMain();
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();

            }
        });

        //forgot username
        binding.usernameTv.setOnClickListener(v -> {
            openDialogUsername();
        });

        //forgot password
        binding.passwordTv.setOnClickListener(v -> {
            openDialogPassword();
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
        startActivity(new Intent(SignInActivity.this, AuthenticationActivity.class));
    }

    private User getUser(int number, String input) {
        userDAO = new UserDAO(SignInActivity.this);
        String email = input;
        if (number == 1) {
            email = userDAO.getEmailByUsername(input);
        }
        return user = userDAO.getUserByEmail(email);
    }

    private void openDialogUsername() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DialogForgotUsernameBinding binding = DialogForgotUsernameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        builder.setView(view);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        binding.cancelTv.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        //show keyboard automatically
        binding.emailEt.postDelayed(() -> {
            binding.emailEt.requestFocus();
            binding.emailEt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            binding.emailEt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }, 100);
        binding.emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handleEmailUserTextChanged(s.toString(), binding);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleEmailUserTextChanged(s.toString(), binding);
            }

            @Override
            public void afterTextChanged(Editable s) {
                handleEmailUserTextChanged(s.toString(), binding);
            }
        });

        binding.okTv.setOnClickListener(v -> {
            String email = binding.emailEt.getText().toString();
            if (handleEmailUserTextChanged(email, binding)) {
                userDAO = new UserDAO(SignInActivity.this);
                if (!userDAO.checkEmail(email)) {
                    binding.emailEt.setError(getString(R.string.email_is_not_exist));
                    return;
                } else {
                    binding.emailEt.setError(null);
                    alertDialog.dismiss();
                }
                user = userDAO.getUserByEmail(email);
                Toast.makeText(this, getString(R.string.check_your_email), Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();

    }

    private void openDialogPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DialogForGotPasswordBinding binding = DialogForGotPasswordBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();

        binding.cancelTv.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        binding.emailOrUsernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handleEmailUserForgotPasswordTextChanged(s.toString(), binding);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleEmailUserForgotPasswordTextChanged(s.toString(), binding);
            }

            @Override
            public void afterTextChanged(Editable s) {
                handleEmailUserForgotPasswordTextChanged(s.toString(), binding);
            }
        });

        binding.okTv.setOnClickListener(v -> {
            String emailOrUsername = binding.emailOrUsernameEt.getText().toString();
            if (handleEmailUserForgotPasswordTextChanged(emailOrUsername, binding)) {
                userDAO = new UserDAO(SignInActivity.this);
                if (Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()) {
                    if (!userDAO.checkEmail(emailOrUsername)) {
                        binding.emailOrUsernameEt.setError(getString(R.string.email_is_not_exist));
                        return;
                    } else {
                        binding.emailOrUsernameEt.setError(null);
                        alertDialog.dismiss();
                    }
                    Toast.makeText(this, getString(R.string.check_your_email), Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()) {
                    if (!userDAO.checkUsername(emailOrUsername)) {
                        binding.emailOrUsernameEt.setError(getString(R.string.user_is_not_exist));
                        return;
                    } else {
                        binding.emailOrUsernameEt.setError(null);
                        alertDialog.dismiss();
                    }
                    Toast.makeText(this, getString(R.string.check_your_email), Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    private boolean handleEmailUserTextChanged(String text, DialogForgotUsernameBinding binding) {
        if (text.isEmpty()) {
            binding.emailEt.requestFocus();
            binding.emailEt.setError(getString(R.string.email_is_empty));

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            binding.emailEt.requestFocus();
            binding.emailEt.setError(getString(R.string.email_is_invalid));
            return false;
        } else {
            binding.emailEt.setError(null);
            return true;
        }
    }

    //check format email and username
    private boolean handleEmailUserForgotPasswordTextChanged(String text, DialogForGotPasswordBinding binding) {
        if (text.isEmpty()) {
            binding.emailOrUsernameEt.requestFocus();
            binding.emailOrUsernameEt.setError(getString(R.string.email_is_empty));

            return false;
        } else if (text.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            binding.emailOrUsernameEt.requestFocus();
            binding.emailOrUsernameEt.setError(getString(R.string.email_is_invalid));
            return false;
        } else {
            binding.emailOrUsernameEt.setError(null);
            return true;
        }
    }

}