package com.daominh.quickmem.ui.activities.auth.signup;

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
import androidx.annotation.Nullable;
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
import com.daominh.quickmem.utils.*;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.swnishan.materialdatetimepicker.datepicker.MaterialDatePickerDialog;
import com.swnishan.materialdatetimepicker.datepicker.MaterialDatePickerView;

import java.time.OffsetDateTime;
import java.util.*;

public class SignUpActivity extends AppCompatActivity {

    private UserDAO userDAO;
    private static final int MAX_LENGTH = 30;
    private static final String link = "https://avatar-nqm.koyeb.app/images";
    public static final String QUICKMEM = "quickmem";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static final int RC_SIGN_IN = 9001;
    private CheckNetWork checkNetWork;
    private ActivitySignupBinding binding;

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
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, googleSignInOptions);
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
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

            int role;
            if (binding.radioYesNo.getCheckedRadioButtonId() == binding.radioYes.getId()) {
                role = 1;
            } else {
                role = 2;
            }
            checkNetWork = new CheckNetWork(this);
            checkNetWork.isConnected().observe(this, aBoolean -> {
                if (aBoolean) {
                    WaitDialog.show(this, getString(R.string.loading));
                    handleSignUp(date, email, password, role);
                } else {
                    TipDialog.show(
                            getString(R.string.no_internet_connection),
                            WaitDialog.TYPE.ERROR,
                            2000
                    );
                }
            });
        });
    }

    private void handleSignUp(String date, String email, String password, int role) {
        CustomDate customDate = new CustomDate();
        // Create a new User object
        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmail(email);
        newUser.setAvatar(randomAvatar());
        newUser.setName("");
        newUser.setPassword(PasswordHasher.hashPassword(password));
        newUser.setUsername(userNameFromEmail(email));
        newUser.setRole(role);
        newUser.setBirthday(date);
        newUser.setCreated_at(customDate.getCurrentDate());
        newUser.setUpdated_at(customDate.getCurrentDate());
        newUser.setStatus(1); // Assuming 1 is the status for active users

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) { // Firebase sign up successful
                        Log.d("SignUpActivity", "handleSignUp: Firebase sign up successful");

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        // Insert the new user into Firebase Firestore
                        Map<String, Object> user = new HashMap<>();
                        assert firebaseUser != null;
                        newUser.setId(firebaseUser.getUid());
                        user.put(UserTable.ID.toString(), newUser.getId());
                        user.put(UserTable.NAME.toString(), newUser.getName());
                        user.put(UserTable.EMAIL.toString(), newUser.getEmail());
                        user.put(UserTable.USERNAME.toString(), newUser.getUsername());
                        user.put(UserTable.PASSWORD.toString(), newUser.getPassword());
                        user.put(UserTable.AVATAR.toString(), newUser.getAvatar());
                        user.put(UserTable.ROLE.toString(), newUser.getRole());
                        user.put(UserTable.BIRTHDAY.toString(), newUser.getBirthday());
                        user.put(UserTable.CREATED_AT.toString(), newUser.getCreated_at());
                        user.put(UserTable.UPDATED_AT.toString(), newUser.getUpdated_at());
                        user.put(UserTable.STATUS.toString(), newUser.getStatus());

                        // Insert the new user into Firebase Firestore
                        firebaseFirestore.collection(Table.USER.toString())
                                .document(newUser.getId())
                                .set(user)
                                .addOnSuccessListener(unused -> {
                                    // Insert the new user into the database
                                    userDAO = new UserDAO(SignUpActivity.this);
                                    long result = userDAO.insertUser(newUser);
                                    // Check the result of the insert operation
                                    if (result > 0) {
                                        // The user was inserted successfully
                                        Toast.makeText(this, getString(R.string.sign_up_successful), Toast.LENGTH_SHORT).show();

                                        // Save the user to shared preferences
                                        saveUserToSharedPreferences(newUser);
                                        WaitDialog.dismiss();

                                        if (task.isSuccessful()){
                                            firebaseUser.sendEmailVerification()
                                                    .addOnCompleteListener(verificationTask -> {
                                                        if (verificationTask.isSuccessful()) {
                                                            Log.d("SignUpActivity", "Email sent.");
                                                        }
                                                    });
                                        }

                                        // Navigate to the main activity
                                        intentToMain();


                                    } else {
                                        // The insert operation failed
                                        Toast.makeText(this, getString(R.string.sign_up_failed), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> Log.d("SignUpActivity", "handleSignUp: Firebase Firestore insert failed"));

                    } else {
                        Log.d("SignUpActivity", "handleSignUp: Firebase sign up failed");
                    }
                })
                .addOnFailureListener(e -> {
                    WaitDialog.dismiss();
                    String errorCode = ((FirebaseAuthException) e).getErrorCode();
                    Log.d("SignUpActivity", "handleSignUp: Firebase sign up failed: " + errorCode);
                    switch (errorCode) {
                        case "ERROR_INVALID_EMAIL":
                            binding.emailTil.setHelperText(getString(R.string.email_is_invalid));
                            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            binding.emailEt.requestFocus();
                            enableButton(false, binding);
                            break;
                        case "ERROR_WEAK_PASSWORD":
                            binding.passwordTil.setHelperText(getString(R.string.password_is_invalid));
                            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            binding.passwordEt.requestFocus();
                            enableButton(false, binding);
                            break;
                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            binding.emailTil.setHelperText(getString(R.string.email_is_exist));
                            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            binding.emailEt.requestFocus();
                            enableButton(false, binding);
                            break;
                        default:
                            Toast.makeText(this, getString(R.string.sign_up_failed), Toast.LENGTH_SHORT).show();
                            enableButton(false, binding);
                            break;
                    }
                    TipDialog.show(getString(R.string.sign_up_failed), WaitDialog.TYPE.ERROR, 2000);
                    enableButton(false, binding);
                });

    }

    //random between 0 and 30
    private String randomAvatar() {
        int random = new Random().nextInt(MAX_LENGTH);
        return link + "/" + random + ".png";
    }

    private String userNameFromEmail(String email) {
        if (email.length() >= 5 && email.length() <= 18) {
            return email.split("@")[0];
        } else {
            return QUICKMEM + new Random().nextInt(1000000);
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
                    .setPositiveButtonText(getString(R.string.ok))
                    .setNegativeButtonText(getString(R.string.cancel))
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

    private boolean handleDateTextChanged(String text, ActivitySignupBinding binding) {
        CustomDate customDate = new CustomDate();
        String currentDate = customDate.getCurrentDate();
        if (text.equals(currentDate)) {
            binding.dateTil.setHelperText(getString(R.string.date_error));
            binding.dateTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.teacherLl.setVisibility(View.GONE);
            enableButton(false, binding);
            binding.dateEt.requestFocus();

            return false;
        } else if (customDate.isDateGreaterThanCurrentDate(text)) {
            binding.dateTil.setHelperText(getString(R.string.date_geater_than_current_date));
            binding.dateTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.teacherLl.setVisibility(View.GONE);
            enableButton(false, binding);
            binding.dateEt.requestFocus();

            return false;
        } else if (customDate.isAgeGreaterThan22(text)) {
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
        }

//        else if (isEmailExist(text)) {
//            binding.emailTil.setHelperText(getString(R.string.email_is_exist));
//            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
//            enableButton(false, binding);
//            binding.emailEt.requestFocus();
//        }

        binding.emailTil.setHelperText(getString(R.string.email));
        binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));
        enableButton(true, binding);
        return true;
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
            binding.signUpBtn.setBackground(AppCompatResources.getDrawable(
                    this,
                    R.drawable.button_background)
            );
        } else {
            binding.signUpBtn.setEnabled(false);
            binding.signUpBtn.setBackground(AppCompatResources.getDrawable(
                    this,
                    R.drawable.background_button_disable)
            );
        }
    }

//    private boolean isEmailExist(String email) {
//        userDAO = new UserDAO(this);
//        return userDAO.checkEmail(email);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(com.google.android.gms.common.api.ApiException.class);
                Log.d("SignUpActivity", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (com.google.android.gms.common.api.ApiException e) {
                //Google Sign In failed, update UI appropriately
                Log.w("SignUpActivity", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "firebaseAuthWithGoogle:success");
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Log.w("SignUpActivity", "firebaseAuthWithGoogle:failure", task.getException());
                        Toast.makeText(this, getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}