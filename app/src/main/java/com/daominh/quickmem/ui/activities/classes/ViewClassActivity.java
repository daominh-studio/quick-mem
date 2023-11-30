package com.daominh.quickmem.ui.activities.classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.group.MyViewClassAdapter;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.databinding.ActivityViewClassBinding;
import com.google.android.material.tabs.TabLayout;

public class ViewClassActivity extends AppCompatActivity {
    private ActivityViewClassBinding binding;
    private GroupDAO groupDAO;
    int currentTabPosition = 0;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewClassBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        MyViewClassAdapter myViewClassAdapter = new MyViewClassAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(myViewClassAdapter);

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        id = getIntent().getStringExtra("id");
        groupDAO = new GroupDAO(this);
        Group group = groupDAO.getGroupById(id);
        binding.classNameTv.setText(group.getName());

//        ViewSetsFragment viewSetsFragment = (ViewSetsFragment) myViewClassAdapter.getItem(0);
//        if (viewSetsFragment != null) {
//            int count = viewSetsFragment.getSetsCount();
//            binding.termCountTv.setText("Count: "+count);
//            Toast.makeText(this, "Count: " + count, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_class, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_member) {

        } else if (id == R.id.add_sets){

        } else if (id == R.id.delete_class){
            
        }
        return super.onOptionsItemSelected(item);
    }
}