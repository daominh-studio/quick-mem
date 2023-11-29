package com.daominh.quickmem.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daominh.quickmem.adapter.MyViewPagerAdapter;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.FragmentLibraryBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateClassActivity;
import com.daominh.quickmem.ui.activities.create.CreateFolderActivity;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;
import com.google.android.material.tabs.TabLayout;


public class LibraryFragment extends Fragment {
    private FragmentLibraryBinding binding;
    private UserSharePreferences userSharePreferences;
    private int currentTabPosition = 0;
    private String idUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
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
        TabLayout mtabLayout = binding.tabLayout;
        ViewPager mviewPager = binding.viewPager;

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mviewPager.setAdapter(myViewPagerAdapter);

        mtabLayout.setupWithViewPager(mviewPager);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                updateAddButtonVisibility();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();

        UserDAO userDAO = new UserDAO(getContext());
        User user = userDAO.getUserById(idUser);
        if (user.getRole() == 2) {
            updateAddButtonVisibility();
        }
        binding.addBtn.setOnClickListener(view1 -> {
            if (currentTabPosition == 0){
                startActivity(new Intent(getActivity(), CreateSetActivity.class));
            } else if (currentTabPosition == 1){
                startActivity(new Intent(getActivity(), CreateFolderActivity.class));
            } else if (currentTabPosition == 2){
                startActivity(new Intent(getActivity(), CreateClassActivity.class));
            }
        });
    }

    private void updateAddButtonVisibility() {
        if (userSharePreferences.getRole() == 2 && currentTabPosition == 2) {
            binding.addBtn.setVisibility(View.GONE);
        } else {
            binding.addBtn.setVisibility(View.VISIBLE);
        }
    }
}