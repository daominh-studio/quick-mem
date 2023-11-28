package com.daominh.quickmem.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.ClassesAdapter;
import com.daominh.quickmem.adapter.UsersAdapter;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.FragmentClassesBinding;
import com.daominh.quickmem.databinding.FragmentUsersBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.auth.signin.SignInActivity;

import java.util.ArrayList;
import java.util.List;

public class ClassesFragment extends Fragment {
    private FragmentClassesBinding binding;
    private UserSharePreferences userSharePreferences;
    private List<Group> listClasses;
    private ClassesAdapter classesAdapter;
    private GroupDAO groupDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupDAO = new GroupDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClassesBinding.inflate(inflater, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listClasses = new ArrayList<>();
        for (Group group: groupDAO.getAllClasses()){
            listClasses.add(group);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
        binding.classesRv.setLayoutManager(linearLayoutManager);
        classesAdapter = new ClassesAdapter(requireActivity(), listClasses);
        binding.classesRv.setAdapter(classesAdapter);
        classesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting_admin, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Đăng xuất");
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userSharePreferences = new UserSharePreferences(getActivity());
                    userSharePreferences.clear();
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                }
            });
            builder.setNegativeButton("Hủy", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}