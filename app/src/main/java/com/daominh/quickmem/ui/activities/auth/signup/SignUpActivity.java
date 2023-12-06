package com.daominh.quickmem.ui.activities.auth.signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.content.res.AppCompatResources;
import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.ActivitySignupBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.MainActivity;
import com.daominh.quickmem.ui.activities.auth.AuthenticationActivity;
import com.daominh.quickmem.utils.PasswordHasher;
import com.swnishan.materialdatetimepicker.datepicker.MaterialDatePickerDialog;
import com.swnishan.materialdatetimepicker.datepicker.MaterialDatePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private UserDAO userDAO;
    private static final int MAX_LENGTH = 30;
    private static final String link = "https://avatar-nqm.koyeb.app/images";

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);

        setupToolbar();
        setupSocialLoginButtons();
        setupDateEditText();
        setupEmailEditText();
        setupPasswordEditText();
        setupSignUpButton();
        setupOnBackPressedCallback();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, AuthenticationActivity.class));
            finish();
        });
    }

    private void setupSocialLoginButtons() {
        binding.facebookBtn.setOnClickListener(v -> {
            // intentToMain();
        });

        binding.googleBtn.setOnClickListener(v -> {
            // intentToMain();
        });
    }

    private void setupDateEditText() {
        binding.dateEt.setOnClickListener(v -> openDialogDatePicker(binding.dateEt::setText));

        binding.dateEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleDateTextChanged(s.toString(), binding);
            }

            @Override
            public void afterTextChanged(Editable s) {
                handleDateTextChanged(s.toString(), binding);
            }
        });
    }

    private void setupEmailEditText() {
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
    }

    private void setupPasswordEditText() {
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
    }

    private void setupSignUpButton() {
        binding.signUpBtn.setOnClickListener(v -> {
            final String date = binding.dateEt.getText().toString();
            final String email = binding.emailEt.getText().toString();
            final String password = binding.passwordEt.getText().toString();

            if (!handleDateTextChanged(date, binding)) return;
            if (!handleEmailTextChanged(email, binding)) return;
            if (!handlePasswordTextChanged(password, binding)) return;

            int role = 2;
            if (binding.radioYesNo.getCheckedRadioButtonId() == binding.radioYes.getId()) {
                role = 1;
            }

            handleSignUp(date, email, password, role);
        });
    }

    private void handleSignUp(String date, String email, String password, int role) {
        // Create a new User object
        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmail(email);
        newUser.setAvatar(randomAvatar());
        newUser.setName("");
        newUser.setUsername(userNameFromEmail(email));
        newUser.setRole(role);
        newUser.setPassword(PasswordHasher.hashPassword(password));
        newUser.setCreated_at(date);
        newUser.setUpdated_at(date);
        newUser.setStatus(1); // Assuming 1 is the status for active users

        // Insert the new user into the database
        userDAO = new UserDAO(this);
        long result = userDAO.insertUser(newUser);

        // Check the result of the insert operation
        if (result > 0) {
            // The user was inserted successfully
            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();

            // Save the user to shared preferences
            saveUserToSharedPreferences(newUser);

            // Navigate to the main activity
            intentToMain();
        } else {
            // The insert operation failed
            Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show();
        }
        Log.d("SignUpActivity", "handleSignUp: " + result + newUser.getAvatar());
    }

    //random between 0 and 30
    private String randomAvatar() {
        int random = new Random().nextInt(MAX_LENGTH);
        return link + "/" + random + ".png";
    }

    private String userNameFromEmail(String email) {
        if (email.contains("@")) {
            String userName = email.substring(0, email.indexOf("@"));
            if (userName.length() > 18 || userName.length() < 4) {
                return "quickmem" + new Random().nextInt(100000);
            }
            return userName;
        } else {
            return email.substring(0, email.indexOf("@"));
        }
    }

    private void saveUserToSharedPreferences(User user) {
        UserSharePreferences userSharePreferences = new UserSharePreferences(this);
        userSharePreferences.saveUser(user);
    }

    private void setupOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(SignUpActivity.this, AuthenticationActivity.class));
                finish();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }


    private void intentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public interface DatePickCallback {
        void onDatePicked(String date);
    }

    private void openDialogDatePicker(DatePickCallback callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            MaterialDatePickerDialog builder = MaterialDatePickerDialog.Builder
                    .setTitle(getString(R.string.date_picker_title))
                    .setPositiveButtonText("OK")
                    .setNegativeButtonText("Cancel")
                    .setDate(OffsetDateTime.now().toInstant().toEpochMilli())
                    .setDateFormat(MaterialDatePickerView.DateFormat.DD_MM_YYYY)
                    .setFadeAnimation(350L, 1050L, .3f, .7f)
                    .build();

            builder.setOnDatePickListener(l -> {
                String date = builder.getDayOfMonth() + "/" + builder.getMonth() + "/" + builder.getYear();
                callback.onDatePicked(date);
            });
            builder.show(getSupportFragmentManager(), "tag");
        }
    }

    private String getCurrentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return currentDate.format(formatter);
        } else {
            // Handle a case for Android versions less than Oreo
            // Here we're using SimpleDateFormat, which is available on all Android versions
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(new Date());
        }
    }

    @SuppressLint("SimpleDateFormat")
    private boolean isDateGreaterThanCurrentDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatter.parse(dateStr);
            Date currentDate = new Date();
            assert date != null;
            return date.after(currentDate);
        } catch (ParseException e) {
            Log.e("SignupActivity", "isDateGreaterThanCurrentDate: Error parsing date. Ensure the date is in the format dd/MM/yyyy.", e);
            return false;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private boolean isAgeGreaterThan18(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            // Handle the case where the date string is empty
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatter.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, -22);
            Date eighteenYearsAgo = calendar.getTime();
            assert date != null;
            return date.before(eighteenYearsAgo);
        } catch (ParseException e) {
            Log.e("SignUpActivity", "isAgeGreaterThan18: Error parsing date. Ensure the date is in the format dd/MM/yyyy.", e);
            return false;
        }
    }


    private boolean handleDateTextChanged(String text, ActivitySignupBinding binding) {
        String currentDate = getCurrentDate();
        if (text.equals(currentDate)) {
            binding.dateTil.setHelperText(getString(R.string.date_error));
            binding.dateTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.teacherLl.setVisibility(View.GONE);
            enableButton(false, binding);
            binding.dateEt.requestFocus();

            return false;
        } else if (isDateGreaterThanCurrentDate(text)) {
            binding.dateTil.setHelperText(getString(R.string.date_geater_than_current_date));
            binding.dateTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.teacherLl.setVisibility(View.GONE);
            enableButton(false, binding);
            binding.dateEt.requestFocus();

            return false;
        } else if (isAgeGreaterThan18(text)) {
            binding.teacherLl.setVisibility(View.VISIBLE);
            binding.dateTil.setHelperText(getString(R.string.date_of_birth));
            binding.dateTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));
            enableButton(true, binding);

            return true;
        } else {
            // clear error if date is valid
            binding.dateTil.setHelperText(getString(R.string.date_of_birth));
            binding.dateTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));
            binding.teacherLl.setVisibility(View.GONE);
            enableButton(true, binding);

            return true;
        }
    }

    private boolean handleEmailTextChanged(String text, ActivitySignupBinding binding) {
        if (text.isEmpty()) {
            binding.emailTil.setHelperText(getString(R.string.email_is_empty));
            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            enableButton(false, binding);
            binding.emailEt.requestFocus();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            binding.emailTil.setHelperText(getString(R.string.email_is_invalid));
            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            enableButton(false, binding);
            binding.emailEt.requestFocus();

            return false;
        } else if (isEmailExist(text)) {
            binding.emailTil.setHelperText(getString(R.string.email_is_exist));
            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            enableButton(false, binding);
            binding.emailEt.requestFocus();
        } else {
            binding.emailTil.setHelperText(getString(R.string.email));
            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));
            enableButton(true, binding);

            return true;
        }
        return false;
    }

    private boolean handlePasswordTextChanged(String text, ActivitySignupBinding binding) {
        if (text.isEmpty()) {
            binding.passwordTil.setHelperText(getString(R.string.password_is_empty));
            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            enableButton(false, binding);
            binding.passwordEt.requestFocus();

            return false;
        } else if (text.length() < 8) {
            binding.passwordTil.setHelperText(getString(R.string.password_is_invalid));
            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            enableButton(false, binding);
            binding.passwordEt.requestFocus();

            return false;
        } else {
            binding.passwordTil.setHelperText(getString(R.string.password));
            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));
            enableButton(true, binding);

            return true;
        }

    }

    private void enableButton(Boolean check, ActivitySignupBinding binding) {
        if (check) {
            binding.signUpBtn.setEnabled(true);
            binding.signUpBtn.setBackground(AppCompatResources.getDrawable(this, R.drawable.button_background));
        } else {
            binding.signUpBtn.setEnabled(false);
            binding.signUpBtn.setBackground(AppCompatResources.getDrawable(this, R.drawable.background_button_disable));
        }
    }

    private boolean isEmailExist(String email) {
        userDAO = new UserDAO(this);
        return userDAO.checkEmail(email);
    }


}