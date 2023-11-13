package com.daominh.quickmem.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.daominh.quickmem.R;
import com.daominh.quickmem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupNavigation();

    }

    private void setupNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
