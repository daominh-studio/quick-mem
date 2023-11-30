package com.daominh.quickmem.ui.activities.auth;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.daominh.quickmem.adapter.viewpager.OnboardingAdapter;
import com.daominh.quickmem.databinding.ActivityAuthenticationBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.MainActivity;
import com.daominh.quickmem.ui.activities.auth.signin.SignInActivity;
import com.daominh.quickmem.ui.activities.auth.signup.SignUpActivity;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserSharePreferences userSharePreferences = new UserSharePreferences(this);

        // Assuming that userSharePreferences is initialized somewhere else
        if (userSharePreferences.getLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        // Inflate the layout
        ActivityAuthenticationBinding binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        // Setup onboarding
        OnboardingAdapter onboardingAdapter = new OnboardingAdapter(this);
        binding.onboardingVp.setAdapter(onboardingAdapter);
        binding.indicator.setViewPager(binding.onboardingVp);

        // Setup sign up button
        binding.signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });

        // Setup sign in button
        binding.signInBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
    }
}