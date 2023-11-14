package com.daominh.quickmem.ui.activities.create;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.databinding.ActivityCreateClassBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreateClassActivity extends AppCompatActivity {
    ActivityCreateClassBinding binding;
    private GroupDAO groupDAO;
    private UserSharePreferences userSharePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateClassBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.done) {
            if (validate()) {
                String name = binding.classEt.getText().toString();
                String description = binding.descriptionEt.getText().toString();
                boolean status = binding.privateSt.isChecked();

                String id = genUUID();
                String user_id = getUser_id();
                String created_at = getCurrentDate();
                String updated_at = getCurrentDate();

                groupDAO = new GroupDAO(this);
                Group group = new Group();
                group.setName(name);
                group.setDescription(description);
                group.setStatus(status ? 1 : 0);
                group.setId(id);
                group.setUser_id(user_id);
                group.setCreated_at(created_at);
                group.setUpdated_at(updated_at);

                if (groupDAO.insertGroup(group) > 0) {
                    onBackPressed();
                    Toast.makeText(this, "Create class success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Create class failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter class name", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate() {
        if (binding.classEt.getText().toString().isEmpty()) {
            binding.classTil.setHelperText("Please enter class name");
            return false;
        } else {
            binding.classTil.setHelperText("");
            return true;
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

    private String genUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    private String getUser_id() {
        userSharePreferences = new UserSharePreferences(this);
        return userSharePreferences.getId();
    }
}