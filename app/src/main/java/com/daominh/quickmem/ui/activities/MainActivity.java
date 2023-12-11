package com.daominh.quickmem.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.daominh.quickmem.R;
import com.daominh.quickmem.databinding.ActivityMainBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private ActivityMainBinding binding;

    UserSharePreferences userSharePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupNavigation();

    }

    private void setupNavigation() {

        userSharePreferences = new UserSharePreferences(MainActivity.this);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        int navGraphId;
        int menuId;

        navGraphId = R.navigation.main_nav;
        menuId = R.menu.menu_nav;


        navController.setGraph(navGraphId);
        binding.bottomNavigationView.getMenu().clear();
        binding.bottomNavigationView.inflateMenu(menuId);

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
