package com.daominh.quickmem.ui.fragments.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.adapter.folder.FolderAdapter;
import com.daominh.quickmem.adapter.flashcard.SetsAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.dao.FolderDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.databinding.FragmentHomeBinding;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;
import com.daominh.quickmem.ui.activities.search.ViewSearchActivity;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ArrayList<FlashCard> flashCards;
    private ArrayList<Folder> folders;
    private FlashCardDAO flashCardDAO;
    private FolderDAO folderDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity();
        flashCardDAO = new FlashCardDAO(requireActivity());
        folderDAO = new FolderDAO(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity();

        setupFlashCards();
        setupFolders();
        setupVisibility();
        setupSwipeRefreshLayout();
        setupSearchBar();
        setupCreateSetsButton();

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            binding.swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(requireActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupFlashCards() {
        flashCards = flashCardDAO.getAllFlashCard();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.setsRv.setLayoutManager(linearLayoutManager);
        SetsAdapter setsAdapter = new SetsAdapter(requireActivity(), flashCards, false);
        binding.setsRv.setAdapter(setsAdapter);
        setsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupFolders() {
        folders = folderDAO.getAllFolders();
        FolderAdapter folderAdapter = new FolderAdapter(requireActivity(), folders);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.foldersRv.setLayoutManager(linearLayoutManager1);
        binding.foldersRv.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();
    }


    private void setupVisibility() {
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
    }

    private void setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupSearchBar() {
        binding.searchBar.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ViewSearchActivity.class);
            startActivity(intent);
        });
    }

    private void setupCreateSetsButton() {
        binding.createSetsCl.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreateSetActivity.class)));
    }

    private void refreshData() {
        setupFlashCards();
        setupFolders();
        setupVisibility();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
