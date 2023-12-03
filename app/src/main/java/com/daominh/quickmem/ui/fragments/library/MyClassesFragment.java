package com.daominh.quickmem.ui.fragments.library;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daominh.quickmem.adapter.group.ClassAdapter;
import com.daominh.quickmem.adapter.group.ClassCopyAdapter;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.FragmentMyClassesBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateClassActivity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyClassesFragment extends Fragment {

    private FragmentMyClassesBinding binding;
    private UserSharePreferences userSharePreferences;
    private ArrayList<Group> classes;
    private GroupDAO groupDAO;
    private ClassCopyAdapter classAdapter;
    private String idUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
        groupDAO = new GroupDAO(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyClassesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();

        UserDAO userDAO = new UserDAO(getContext());
        User user = userDAO.getUserById(idUser);
        if (user.getRole() == 2) {
            binding.createSetBtn.setVisibility(View.GONE);
        }
        binding.createSetBtn.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), CreateClassActivity.class)));

        classes = groupDAO.getClassesOwnedByUser(idUser);
        classes.addAll(groupDAO.getClassesUserIsMemberOf(idUser));

        if (classes.isEmpty()) {
            binding.classesCl.setVisibility(View.VISIBLE);
            binding.classesRv.setVisibility(View.GONE);
        } else {
            binding.classesCl.setVisibility(View.GONE);
            binding.classesRv.setVisibility(View.VISIBLE);
        }

        classAdapter = new ClassCopyAdapter(requireActivity(), classes);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
        binding.classesRv.setLayoutManager(linearLayoutManager2);
        binding.classesRv.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        classes = groupDAO.getClassesOwnedByUser(idUser);
        classes.addAll(groupDAO.getClassesUserIsMemberOf(idUser));

        classAdapter = new ClassCopyAdapter(requireActivity(), classes);
        binding.classesRv.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        if (classes.isEmpty()) {
            binding.classesCl.setVisibility(View.VISIBLE);
            binding.classesRv.setVisibility(View.GONE);
        } else {
            binding.classesCl.setVisibility(View.GONE);
            binding.classesRv.setVisibility(View.VISIBLE);
        }
    }
}