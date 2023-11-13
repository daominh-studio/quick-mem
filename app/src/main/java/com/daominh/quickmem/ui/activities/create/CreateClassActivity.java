package com.daominh.quickmem.ui.activities.create;

import android.view.Menu;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.daominh.quickmem.R;
import com.daominh.quickmem.databinding.ActivityCreateClassBinding;

public class CreateClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreateClassBinding binding = ActivityCreateClassBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);


    }



}