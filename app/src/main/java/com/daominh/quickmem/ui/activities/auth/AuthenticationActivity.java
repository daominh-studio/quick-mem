package com.daominh.quickmem.ui.activities.auth;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.daominh.quickmem.adapter.OnboardingAdapter;
import com.daominh.quickmem.ui.activities.auth.signin.SigninActivity;
import com.daominh.quickmem.ui.activities.auth.signup.SignupActivity;
import com.daominh.quickmem.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends AppCompatActivity {

    private ActivityAuthenticationBinding binding;

    private OnboardingAdapter onboardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        //onboarding
        onboardingAdapter = new OnboardingAdapter(this);
        binding.onboardingVp.setAdapter(onboardingAdapter);

        binding.indicator.setViewPager(binding.onboardingVp);

        //sign up
        binding.signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });

        //sign in
        binding.signInBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        });

    }
}