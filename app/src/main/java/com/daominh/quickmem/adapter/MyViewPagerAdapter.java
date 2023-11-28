package com.daominh.quickmem.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.daominh.quickmem.ui.fragments.library.FoldersFragment;
import com.daominh.quickmem.ui.fragments.library.MyClassesFragment;
import com.daominh.quickmem.ui.fragments.library.StudySetsFragment;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new StudySetsFragment();
            case 1:
                return new FoldersFragment();
            case 2:
                return new MyClassesFragment();
            default:
                return new StudySetsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Study sets";
                break;
            case 1:
                title = "Folders";
                break;
            case 2:
                title = "Classes";
                break;
        }
        return title;
    }
}