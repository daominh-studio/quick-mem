package com.daominh.quickmem.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.MyViewPagerAdapter;
import com.daominh.quickmem.databinding.FragmentClassesBinding;
import com.daominh.quickmem.databinding.FragmentLibraryBinding;
import com.google.android.material.tabs.TabLayout;


public class LibraryFragment extends Fragment {
    private FragmentLibraryBinding binding;
    private TabLayout mtabLayout;
    private ViewPager mviewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtabLayout = binding.tabLayout;
        mviewPager = binding.viewPager;

        myViewPagerAdapter = new MyViewPagerAdapter(getFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mviewPager.setAdapter(myViewPagerAdapter);

        mtabLayout.setupWithViewPager(mviewPager);
        
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Add", Toast.LENGTH_SHORT).show();
            }
        });
    }
}