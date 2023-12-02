package com.daominh.quickmem.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.daominh.quickmem.adapter.group.ClassAdapter;
import com.daominh.quickmem.adapter.folder.FolderAdapter;
import com.daominh.quickmem.adapter.flashcard.SetsAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.dao.FolderDAO;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.databinding.FragmentHomeBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.search.ViewSearchActivity;


import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private UserSharePreferences userSharePreferences;
    private UserDAO userDAO;
    private SetsAdapter setsAdapter;
    private FolderAdapter folderAdapter;
    private ClassAdapter classAdapter;
    private ArrayList<FlashCard> flashCards;
    private ArrayList<Folder> folders;
    private ArrayList<Group> classes;
    private FlashCardDAO flashCardDAO;
    private FolderDAO folderDAO;
    private GroupDAO groupDAO;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String idUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
        userDAO = new UserDAO(requireActivity());
        flashCardDAO = new FlashCardDAO(requireActivity());
        folderDAO = new FolderDAO(requireActivity());
        groupDAO = new GroupDAO(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();


        flashCards = flashCardDAO.getAllFlashCardByUserId(idUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.setsRv.setLayoutManager(linearLayoutManager);
        setsAdapter = new SetsAdapter(requireActivity(), flashCards, false);
        binding.setsRv.setAdapter(setsAdapter);
        setsAdapter.notifyDataSetChanged();

        // Assuming folderDAO has a method getAllFoldersByUserId() to get all folders for a user
        folders = folderDAO.getAllFolderByUserId(idUser);

        folderAdapter = new FolderAdapter(requireActivity(), folders);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.foldersRv.setLayoutManager(linearLayoutManager1);
        binding.foldersRv.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();

        classes = groupDAO.getClassesOwnedByUser(idUser);
        classes.addAll(groupDAO.getClassesUserIsMemberOf(idUser));

        classAdapter = new ClassAdapter(requireActivity(), classes);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.classesRv.setLayoutManager(linearLayoutManager2);
        binding.classesRv.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        if (flashCards.isEmpty()) {
            binding.setsCl.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.VISIBLE);
        }
        if (folders.isEmpty()) {
            binding.folderCl.setVisibility(View.GONE);
        } else {
            binding.folderCl.setVisibility(View.VISIBLE);
        }
        if (classes.isEmpty()) {
            binding.classCl.setVisibility(View.GONE);
        } else {
            binding.classCl.setVisibility(View.VISIBLE);
        }
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        binding.searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ViewSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refreshData() {
        flashCards = flashCardDAO.getAllFlashCardByUserId(idUser);
        setsAdapter = new SetsAdapter(requireActivity(), flashCards, false);
        binding.setsRv.setAdapter(setsAdapter);
        setsAdapter.notifyDataSetChanged();

        // Assuming folderDAO has a method getAllFoldersByUserId() to get all folders for a user
        folders = folderDAO.getAllFolderByUserId(idUser);

        folderAdapter = new FolderAdapter(requireActivity(), folders);
        binding.foldersRv.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();

        classes = groupDAO.getClassesOwnedByUser(idUser);
        classes.addAll(groupDAO.getClassesUserIsMemberOf(idUser));
        Log.d("classesz", classes.size() + " " + classes.toString());

        classAdapter = new ClassAdapter(requireActivity(), classes);
        binding.classesRv.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();

        if (flashCards.isEmpty()) {
            binding.setsCl.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.VISIBLE);
        }
        if (folders.isEmpty()) {
            binding.folderCl.setVisibility(View.GONE);
        } else {
            binding.folderCl.setVisibility(View.VISIBLE);
        }
        if (classes.isEmpty()) {
            binding.classCl.setVisibility(View.GONE);
        } else {
            binding.classCl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (flashCards.isEmpty()) {
            binding.setsCl.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.VISIBLE);
        }
        if (folders.isEmpty()) {
            binding.folderCl.setVisibility(View.GONE);
        } else {
            binding.folderCl.setVisibility(View.VISIBLE);
        }
        if (classes.isEmpty()) {
            binding.classCl.setVisibility(View.GONE);
        } else {
            binding.classCl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
