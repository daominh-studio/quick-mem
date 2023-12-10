package com.daominh.quickmem.ui.activities.auth.signin;

import android.app.Dialog;
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
import com.daominh.quickmem.utils.CheckNetWork;
import com.daominh.quickmem.utils.PasswordHasher;
import com.daominh.quickmem.utils.Table;
import com.daominh.quickmem.utils.UserTable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;



public class SignInActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CheckNetWork checkNetWork;

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
        setupSocialSignIn(binding);

        //on text changed
        setupTextChangedListeners(binding);

        //sign in button
        setupSignInButton(binding);

        //forgot username
        binding.usernameTv.setOnClickListener(v -> openDialogUsername());

        //forgot password
        binding.passwordTv.setOnClickListener(v -> openDialogPassword());
    }

    private void setupSocialSignIn(ActivitySigninBinding binding) {
        binding.googleBtn.setOnClickListener(v -> {
            //intentToMain();
        });
        binding.facebookBtn.setOnClickListener(v -> {
            //intentToMain();
        });
    }

    private void setupTextChangedListeners(ActivitySigninBinding binding) {
        binding.emailEt.addTextChangedListener(createEmailTextChangedListener(binding));
        binding.passwordEt.addTextChangedListener(createPasswordTextChangedListener(binding));
    }

    private TextWatcher createEmailTextChangedListener(ActivitySigninBinding binding) {
        return new TextWatcher() {
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
        };
    }

    private TextWatcher createPasswordTextChangedListener(ActivitySigninBinding binding) {
        return new TextWatcher() {
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
        };
    }

    private void setupSignInButton(ActivitySigninBinding binding) {
        binding.signInBtn.setOnClickListener(v -> {
            final String email = binding.emailEt.getText().toString();
            String password = binding.passwordEt.getText().toString();

            checkNetWork = new CheckNetWork(SignInActivity.this);
            checkNetWork.isConnected().observe(this, aBoolean -> {
                if (aBoolean) {
                    WaitDialog.show(this, getString(R.string.loading));
                    if (handleEmailTextChanged(email, binding) && handlePasswordTextChanged(password, binding)) {
                        handleSignIn(email, password, binding);
                    }
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

    private void handleSignIn(String email, String password, ActivitySigninBinding binding) {
        userDAO = new UserDAO(SignInActivity.this);


        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            handleEmailSignIn(email, password, binding);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            handleUsernameSignIn(email, password, binding);
        }
    }

    private void handleEmailSignIn(String email, String password, ActivitySigninBinding binding) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        assert firebaseUser != null;
                        getUser(firebaseUser.getUid());

                    } else {
                        WaitDialog.dismiss();
                        TipDialog.show(
                                getString(R.string.loginFailed),
                                WaitDialog.TYPE.ERROR,
                                2000
                        );
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                    WaitDialog.dismiss();
                    String errorCode = ((FirebaseAuthException) e).getErrorCode();
                    switch (errorCode) {
                        case "ERROR_INVALID_EMAIL":
                            binding.emailTil.setHelperText(getString(R.string.email_is_invalid));
                            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            break;
                        case "ERROR_WRONG_PASSWORD":
                            binding.passwordTil.setHelperText(getString(R.string.password_is_not_correct));
                            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            break;
                        case "ERROR_USER_NOT_FOUND":
                            binding.emailTil.setHelperText(getString(R.string.email_is_not_exist));
                            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            break;
                        case "ERROR_USER_DISABLED":
                            binding.emailTil.setHelperText(getString(R.string.user_is_blocked));
                            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            break;
                        case "ERROR_TOO_MANY_REQUESTS":
                            binding.emailTil.setHelperText(getString(R.string.too_many_request));
                            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            break;
                        case "ERROR_OPERATION_NOT_ALLOWED":
                            binding.emailTil.setHelperText(getString(R.string.operation_not_allowed));
                            binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            break;
                    }
                });

    }

    private void handleUsernameSignIn(String username, String password, ActivitySigninBinding binding) {
        password = PasswordHasher.hashPassword(password);
        String finalPassword = password;
        firebaseFirestore.collection(Table.USER.toString())
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        binding.emailTil.setHelperText(getString(R.string.user_is_not_exist));
                        binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                        WaitDialog.dismiss();
                        return;
                    }
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {

                        if (queryDocumentSnapshots.getDocuments().get(i).getString(UserTable.PASSWORD.toString()).equals(finalPassword)) {
                            String uid = queryDocumentSnapshots.getDocuments().get(i).getId();
                            getUser(uid);
                        } else {
                            binding.passwordTil.setHelperText(getString(R.string.password_is_not_correct));
                            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            WaitDialog.dismiss();
                            return;

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                    WaitDialog.dismiss();
                });
    }

    private void handleUserStatus(User user) {
        if (user.getStatus() == 0) {
            Log.d("SignInActivityy", "handleUserStatus: " + user.getStatus());
            PopupDialog.getInstance(SignInActivity.this)
                    .setStyle(Styles.FAILED)
                    .setHeading(getString(R.string.loginFailed))
                    .setDescription(getString(R.string.you_are_blocked))
                    .setPopupDialogIcon(R.drawable.ic_delete)
                    .setCancelable(true)
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(Dialog dialog) {
                            super.onDismissClicked(dialog);
                            dialog.dismiss();
                        }
                    });

        } else {
            UserSharePreferences userSharePreferences = new UserSharePreferences(SignInActivity.this);
            userSharePreferences.setRole(user.getRole());
            userSharePreferences.saveUser(user);
            userSharePreferences.setLogin(true);
            userSharePreferences.setUserName(user.getUsername());
            userSharePreferences.setAvatar(user.getAvatar());
            userSharePreferences.setEmail(user.getEmail());
            intentToMain();
            Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show();
        }
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
        }

        binding.emailTil.setHelperText(getString(R.string.email));
        binding.emailTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));
        return true;
    }

    private boolean handlePasswordTextChanged(String text, ActivitySigninBinding binding) {
        if (text.isEmpty()) {
            binding.passwordTil.setHelperText(getString(R.string.password_is_empty));
            binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.RED));
            binding.passwordEt.requestFocus();
            return false;
        }

        binding.passwordTil.setHelperText(getString(R.string.password));
        binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(Color.GRAY));
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignInActivity.this, AuthenticationActivity.class));
    }

    private User getUser(String uid) {
        Log.d("SignInActivity", "getUser: " + uid);
        userDAO = new UserDAO(SignInActivity.this);
        User user = new User();
        firebaseFirestore.collection(Table.USER.toString()).document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    user.setId(documentSnapshot.getString("id"));
                    user.setName(documentSnapshot.getString("name"));
                    user.setEmail(documentSnapshot.getString("email"));
                    user.setUsername(documentSnapshot.getString("username"));
                    user.setPassword(documentSnapshot.getString("password"));
                    user.setAvatar(documentSnapshot.getString("avatar"));
                    user.setRole(documentSnapshot.getLong("role").intValue());
                    user.setBirthday(documentSnapshot.getString("birthday"));
                    user.setCreated_at(documentSnapshot.getString("created_at"));
                    user.setUpdated_at(documentSnapshot.getString("updated_at"));
                    user.setStatus(documentSnapshot.getLong("status").intValue());
                    Log.d("SignInActivityy", "getUser: " + user.toString());
                    handleUserStatus(user);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                    WaitDialog.dismiss();
                });
        WaitDialog.dismiss();
        return user;
    }

    private void openDialogUsername() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DialogForgotUsernameBinding binding = DialogForgotUsernameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        builder.setView(view);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        binding.cancelTv.setOnClickListener(v -> alertDialog.dismiss());

        //show keyboard automatically
        showKeyboardAutomatically(binding);

        binding.emailEt.addTextChangedListener(createEmailTextWatcher(binding));

        binding.okTv.setOnClickListener(v -> handleOkButtonClick(binding, alertDialog));

        alertDialog.show();
    }

    //show keyboard automatically email and username

    private void showKeyboardAutomatically(DialogForgotUsernameBinding binding) {
        binding.emailEt.postDelayed(() -> {
            binding.emailEt.requestFocus();
            binding.emailEt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            binding.emailEt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }, 100);
    }


    //check format email
    private TextWatcher createEmailTextWatcher(DialogForgotUsernameBinding binding) {
        return new TextWatcher() {
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
        };
    }

    //handle ok button click
    private void handleOkButtonClick(DialogForgotUsernameBinding binding, AlertDialog alertDialog) {
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
            User user = userDAO.getUserByEmail(email);
            Toast.makeText(this, getString(R.string.check_your_email), Toast.LENGTH_SHORT).show();
        }
    }

    //check format email

    private void openDialogPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DialogForGotPasswordBinding binding = DialogForGotPasswordBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();

        //show keyboard automatically
        showKeyboardAutomaticallyPassword(binding);

        binding.cancelTv.setOnClickListener(v -> alertDialog.dismiss());

        binding.emailOrUsernameEt.addTextChangedListener(createEmailUserForgotPasswordTextWatcher(binding));

        binding.okTv.setOnClickListener(v -> handleOkPasswordButtonClick(binding, alertDialog));

        alertDialog.show();
    }


    //show keyboard automatically password
    private void showKeyboardAutomaticallyPassword(DialogForGotPasswordBinding binding) {
        binding.emailOrUsernameEt.postDelayed(() -> {
            binding.emailOrUsernameEt.requestFocus();
            binding.emailOrUsernameEt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            binding.emailOrUsernameEt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }, 100);
    }

    private TextWatcher createEmailUserForgotPasswordTextWatcher(DialogForGotPasswordBinding binding) {
        return new TextWatcher() {
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
        };
    }

    //handle ok button click

    private void handleOkPasswordButtonClick(DialogForGotPasswordBinding binding, AlertDialog alertDialog) {
        String emailOrUsername = binding.emailOrUsernameEt.getText().toString();
        if (!handleEmailUserForgotPasswordTextChanged(emailOrUsername, binding)) {
            return;
        }

        userDAO = new UserDAO(SignInActivity.this);
        if (Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()) {
            if (!userDAO.checkEmail(emailOrUsername)) {
                binding.emailOrUsernameEt.setError(getString(R.string.email_is_not_exist));
                return;
            }
            binding.emailOrUsernameEt.setError(null);
            alertDialog.dismiss();
            Toast.makeText(this, getString(R.string.check_your_email), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userDAO.checkUsername(emailOrUsername)) {
            binding.emailOrUsernameEt.setError(getString(R.string.user_is_not_exist));
            return;
        }
        binding.emailOrUsernameEt.setError(null);
        alertDialog.dismiss();
        String email = binding.emailOrUsernameEt.getText().toString();


        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.check_your_email), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show());
    }

    //check format email

    private boolean handleEmailUserTextChanged(String text, DialogForgotUsernameBinding binding) {
        if (text.isEmpty()) {
            binding.emailEt.requestFocus();
            binding.emailEt.setError(getString(R.string.email_is_empty));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            binding.emailEt.requestFocus();
            binding.emailEt.setError(getString(R.string.email_is_invalid));
            return false;
        }

        binding.emailEt.setError(null);
        return true;
    }

    //check format email and username
    private boolean handleEmailUserForgotPasswordTextChanged(String text, DialogForGotPasswordBinding binding) {
        if (text.isEmpty()) {
            binding.emailOrUsernameEt.requestFocus();
            binding.emailOrUsernameEt.setError(getString(R.string.email_is_empty));
            return false;
        }

        if (text.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            binding.emailOrUsernameEt.requestFocus();
            binding.emailOrUsernameEt.setError(getString(R.string.email_is_invalid));
            return false;
        }

        binding.emailOrUsernameEt.setError(null);
        return true;
    }
}