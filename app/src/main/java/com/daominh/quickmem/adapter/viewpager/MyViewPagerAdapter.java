package com.daominh.quickmem.adapter.viewpager;

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
        return switch (position) {
            case 1 -> new FoldersFragment();
            case 2 -> new MyClassesFragment();
            default -> new StudySetsFragment();
        };
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            case 0 -> "Study sets";
            case 1 -> "Folders";
            case 2 -> "Classes";
            default -> "";
        };
    }
}