package com.daominh.quickmem.adapter.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.daominh.quickmem.ui.activities.classes.ViewMembersFragment;
import com.daominh.quickmem.ui.activities.classes.ViewSetsFragment;
import com.daominh.quickmem.ui.fragments.library.FoldersFragment;
import com.daominh.quickmem.ui.fragments.library.MyClassesFragment;
import com.daominh.quickmem.ui.fragments.library.StudySetsFragment;

public class MyViewClassAdapter extends FragmentStatePagerAdapter {

    public MyViewClassAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return switch (position) {
            case 1 -> new ViewMembersFragment();
            default -> new ViewSetsFragment();
        };
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return switch (position) {
            case 0 -> "SETS";
            case 1 -> "MEMBERS";
            default -> "";
        };
    }
}
