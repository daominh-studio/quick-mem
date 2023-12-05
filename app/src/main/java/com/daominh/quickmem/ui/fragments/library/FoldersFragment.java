package com.daominh.quickmem.ui.fragments.library;

import android.annotation.SuppressLint;
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

import com.daominh.quickmem.adapter.folder.FolderCopyAdapter;
import com.daominh.quickmem.data.dao.FolderDAO;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.databinding.FragmentFoldersBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateFolderActivity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoldersFragment extends Fragment {
    private FragmentFoldersBinding binding;
    private UserSharePreferences userSharePreferences;
    private ArrayList<Folder> folders;
    private FolderCopyAdapter folderAdapter;
    private FolderDAO folderDAO;
    private String idUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
        folderDAO = new FolderDAO(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoldersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUserPreferences();
        setupCreateButton();
        setupFolders();
        setupRecyclerView();
    }

    private void setupUserPreferences() {
        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
    }

    private void setupCreateButton() {
        binding.createSetBtn.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), CreateFolderActivity.class)));
    }

    private void setupFolders() {
        folders = folderDAO.getAllFolderByUserId(idUser);
        if (folders.isEmpty()) {
            binding.folderCl.setVisibility(View.VISIBLE);
            binding.foldersRv.setVisibility(View.GONE);
        } else {
            binding.folderCl.setVisibility(View.GONE);
            binding.foldersRv.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerView() {
        folderAdapter = new FolderCopyAdapter(requireActivity(), folders);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
        binding.foldersRv.setLayoutManager(linearLayoutManager1);
        binding.foldersRv.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData() {
        folders = folderDAO.getAllFolderByUserId(idUser);

        folderAdapter = new FolderCopyAdapter(requireActivity(), folders);
        binding.foldersRv.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();

        if (folders.isEmpty()) {
            binding.folderCl.setVisibility(View.VISIBLE);
            binding.foldersRv.setVisibility(View.GONE);
        } else {
            binding.folderCl.setVisibility(View.GONE);
            binding.foldersRv.setVisibility(View.VISIBLE);
        }
    }
}