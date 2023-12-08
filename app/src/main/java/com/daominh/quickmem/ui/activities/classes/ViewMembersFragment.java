package com.daominh.quickmem.ui.activities.classes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.daominh.quickmem.adapter.user.UserClassAdapter;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.FragmentViewMembersBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ViewMembersFragment extends Fragment {
    private FragmentViewMembersBinding binding;
    private UserSharePreferences userSharePreferences;
    private UserDAO userDAO;
    private GroupDAO groupDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());
        userDAO = new UserDAO(requireContext());
        groupDAO = new GroupDAO(requireContext());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewMembersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Group group = groupDAO.getGroupById(userSharePreferences.getClassId());
        ArrayList<User> users = new ArrayList<>();
        users.add(userDAO.getUserByIdClass(group.getUser_id()));
        users.addAll(userDAO.getListUserByIdClass(userSharePreferences.getClassId()));

        UserClassAdapter userClassAdapter = new UserClassAdapter(users, false, group.getId());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.membersRv.setLayoutManager(linearLayoutManager);
        binding.membersRv.setAdapter(userClassAdapter);
        binding.membersRv.setHasFixedSize(true);
        userClassAdapter.notifyDataSetChanged();

    }
}