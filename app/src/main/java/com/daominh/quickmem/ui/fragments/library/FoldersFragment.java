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

import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.FolderAdapter;
import com.daominh.quickmem.data.dao.FolderDAO;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.databinding.FragmentFoldersBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateFolderActivity;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;

import java.util.ArrayList;

public class FoldersFragment extends Fragment {
    private FragmentFoldersBinding binding;
    private UserSharePreferences userSharePreferences;
    private ArrayList<Folder> folders;
    private FolderAdapter folderAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoldersBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
        binding.createSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateFolderActivity.class));
            }
        });

        folders = folderDAO.getAllFolderByUserId(idUser);

        if (folders.size() == 0) {
            binding.folderCl.setVisibility(View.VISIBLE);
            binding.foldersRv.setVisibility(View.GONE);
        } else {
            binding.folderCl.setVisibility(View.GONE);
            binding.foldersRv.setVisibility(View.VISIBLE);
        }

        folderAdapter = new FolderAdapter(requireActivity(), folders);
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

    private void refreshData() {
        folders = folderDAO.getAllFolderByUserId(idUser);

        folderAdapter = new FolderAdapter(requireActivity(), folders);
        binding.foldersRv.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();

        if (folders.size() == 0) {
            binding.folderCl.setVisibility(View.VISIBLE);
            binding.foldersRv.setVisibility(View.GONE);
        } else {
            binding.folderCl.setVisibility(View.GONE);
            binding.foldersRv.setVisibility(View.VISIBLE);
        }
    }
}