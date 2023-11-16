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
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private User user;
    private UserDAO userDAO;
    private UserSharePreferences userSharePreferences;
    private static final String MAX_LEGHT = "30";
    private static final String link = "https://avatar-nqm.koyeb.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignupBinding binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //icon navigation
        binding.toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, AuthenticationActivity.class));
            finish();
        });

        //login by social
        binding.facebookBtn.setOnClickListener(v -> {
//            intentToMain();
        });

        binding.googleBtn.setOnClickListener(v -> {
//            intentToMain();
        });

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

        binding.signUpBtn.setOnClickListener(v -> {
            final String date = binding.dateEt.getText().toString();
            final String email = binding.emailEt.getText().toString();
            final String password = binding.passwordEt.getText().toString();

            if (!handleDateTextChanged(date, binding)) return;
            if (!handleEmailTextChanged(email, binding)) return;
            if (!handlePasswordTextChanged(password, binding)) return;

            if (date.isEmpty()) {
                binding.dateTil.setHelperText(getString(R.string.date_is_empty));
                binding.dateTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                binding.dateEt.requestFocus();
            } else if (email.isEmpty()) {
                binding.emailTil.setHelperText(getString(R.string.email_is_empty));
                binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                binding.emailEt.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailTil.setHelperText(getString(R.string.email_is_invalid));
                binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                binding.emailEt.requestFocus();
            } else if (password.isEmpty()) {
                binding.passwordTil.setHelperText(getString(R.string.password_is_empty));
                binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                binding.passwordEt.requestFocus();
            } else if (password.length() < 8) {
                binding.passwordTil.setHelperText(getString(R.string.password_is_invalid));
                binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                binding.passwordEt.requestFocus();
            } else {

                userDAO = new UserDAO(this);

                String uuid = UUID.randomUUID().toString();
                String username = email.split("@")[0];
                if (username.length() > 18 || username.length() < 6) {
                    username = "quickmem" + new Random().nextInt(1000000);
                } else if (userDAO.checkUsername(username)) {
                    username = username + new Random().nextInt(100);
                }

                String hashedPassword = PasswordHasher.hashPassword(password);
                int role = 0;
                if (binding.teacherLl.getVisibility() == View.VISIBLE) {
                    if (binding.radioYes.isChecked()) {
                        role = 1;
                    } else {
                        role = 2;
                    }
                }
                String createdAt = getCurrentDate();
                String updatedAt = getCurrentDate();
                int status = 1;
                user = new User();
                final int finalRandom = (int) (Math.random() * (Integer.parseInt(MAX_LEGHT) - 1 + 1) + 1);

                //save link avatar + random
                String linkAvatar = link + "/images/" + finalRandom + ".png";


                user.setId(uuid);
                user.setName("");
                user.setEmail(email);
                user.setUsername(username);
                user.setPassword(hashedPassword);
                user.setRole(role);
                user.setAvatar(linkAvatar);
                user.setCreated_at(createdAt);
                user.setUpdated_at(updatedAt);
                user.setStatus(status);


                if (userDAO.insertUser(user) > 0) {
                    userSharePreferences = new UserSharePreferences(this);
                    userSharePreferences.setLogin(true);
                    userSharePreferences.saveUser(user);
                    userSharePreferences.setAvatar(linkAvatar);
                    userSharePreferences.setUserName(username);
                    intentToMain();
                } else {
                    Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show();
                }


            }
        });
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
                    .setDate(OffsetDateTime.now().plusDays(10).toInstant().toEpochMilli())
                    .setDateFormat(MaterialDatePickerView.DateFormat.DD_MMM_YYYY)
                    .setFadeAnimation(350L, 1050L, .3f, .7f)
                    .build();

            builder.setOnDatePickListener(l -> {
                String date = builder.getDayOfMonth() + "/" + (builder.getMonth() + 1) + "/" + builder.getYear();
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
            // Handle case for Android versions less than Oreo
            // Here we're using SimpleDateFormat which is available on all Android versions
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(new Date());
        }
    }

    private boolean isDateGreaterThanCurrentDate(String dateStr) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            LocalDate currentDate = LocalDate.now();
            return date.isAfter(currentDate);
        } else {
            // Handle case for Android versions less than Oreo
            // Here we're using SimpleDateFormat which is available on all Android versions
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
            try {
                Date date = sdf.parse(dateStr);
                Date currentDate = new Date();
                assert date != null;
                return date.after(currentDate);
            } catch (ParseException e) {
                Log.e("SignupActivity", "isDateGreaterThanCurrentDate: ", e);
                return false;
            }
        }
    }

    private boolean isAgeGreaterThan18(String dateStr) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate birthday = LocalDate.parse(dateStr, formatter);
            LocalDate today = LocalDate.now();
            Period period = Period.between(birthday, today);
            return period.getYears() >= 18;
        } else {
            // Handle case for Android versions less than Oreo
            // Here we're using Calendar which is available on all Android versions
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
            try {
                Date birthday = sdf.parse(dateStr);
                Calendar birthdate = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                assert birthday != null;
                birthdate.setTime(birthday);
                int age = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < birthdate.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
                return age >= 18;
            } catch (ParseException e) {
                Log.e("SignupActivity", "isAgeGreaterThan18: ", e);
                return false;
            }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this, AuthenticationActivity.class));
    }
}