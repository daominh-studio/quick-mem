package com.daominh.quickmem.ui.fragments.home;

import android.annotation.SuppressLint;
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
import com.daominh.quickmem.adapter.group.ClassAdapter;
import com.daominh.quickmem.adapter.folder.FolderAdapter;
import com.daominh.quickmem.adapter.flashcard.SetsAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.dao.FolderDAO;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.databinding.FragmentHomeBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;
import com.daominh.quickmem.ui.activities.search.ViewSearchActivity;
import com.daominh.quickmem.utils.FlashcardTable;
import com.daominh.quickmem.utils.Table;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private UserSharePreferences userSharePreferences;
    private SetsAdapter setsAdapter;
    private FolderAdapter folderAdapter;
    private ClassAdapter classAdapter;
    private ArrayList<FlashCard> flashCards;
    private ArrayList<Folder> folders;
    private ArrayList<Group> classes;
    private FlashCardDAO flashCardDAO;
    private FolderDAO folderDAO;
    private GroupDAO groupDAO;
    private String idUser;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
        flashCardDAO = new FlashCardDAO(requireActivity());
        folderDAO = new FolderDAO(requireActivity());
        groupDAO = new GroupDAO(requireActivity());
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

        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();

        setupFlashCards();
        setupFolders();
        setupClasses();

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
        //get all flashcards by user id from firebase
        firebaseFirestore.collection(Table.FLASHCARD.toString()).whereEqualTo(FlashcardTable.USER_ID.toString(), idUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                flashCards = new ArrayList<>();
                for (int i = 0; i < task.getResult().size(); i++) {
                    FlashCard flashCard = new FlashCard();
                    flashCard.setId(task.getResult().getDocuments().get(i).getId());
                    flashCard.setUser_id(task.getResult().getDocuments().get(i).getString(FlashcardTable.USER_ID.toString()));
                    flashCard.setName(task.getResult().getDocuments().get(i).getString(FlashcardTable.NAME.toString()));
                    flashCard.setIs_public(((Long) task.getResult().getDocuments().get(i).get(FlashcardTable.IS_PUBLIC.toString())).intValue());
                    flashCard.setDescription(task.getResult().getDocuments().get(i).getString(FlashcardTable.DESCRIPTION.toString()));
                    flashCard.setCreated_at(task.getResult().getDocuments().get(i).getString(FlashcardTable.CREATED_AT.toString()));
                    flashCard.setUpdated_at(task.getResult().getDocuments().get(i).getString(FlashcardTable.UPDATED_AT.toString()));
                    flashCards.add(flashCard);
                }
                setsAdapter = new SetsAdapter(requireActivity(), flashCards, false);
                Log.d("TAGGG", "setupFlashCards: " + flashCards.size());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                binding.setsRv.setLayoutManager(linearLayoutManager);
                binding.setsRv.setAdapter(setsAdapter);
                setsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireActivity(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });


//        flashCards = flashCardDAO.getAllFlashCardByUserId(idUser);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
//        binding.setsRv.setLayoutManager(linearLayoutManager);
//        setsAdapter = new SetsAdapter(requireActivity(), flashCards, false);
//        binding.setsRv.setAdapter(setsAdapter);
//        setsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupFolders() {
        folders = folderDAO.getAllFolderByUserId(idUser);
        folderAdapter = new FolderAdapter(requireActivity(), folders);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.foldersRv.setLayoutManager(linearLayoutManager1);
        binding.foldersRv.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupClasses() {
        classes = groupDAO.getClassesOwnedByUser(idUser);
        classes.addAll(groupDAO.getClassesUserIsMemberOf(idUser));
        classAdapter = new ClassAdapter(requireActivity(), classes);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.classesRv.setLayoutManager(linearLayoutManager2);
        binding.classesRv.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();
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
        if (classes.isEmpty()) {
            binding.classCl.setVisibility(View.GONE);
        } else {
            binding.classCl.setVisibility(View.VISIBLE);
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
        refreshFlashCards();
        refreshFolders();
        refreshClasses();
        updateVisibility();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshFlashCards() {
        flashCards = flashCardDAO.getAllFlashCardByUserId(idUser);
        setsAdapter = new SetsAdapter(requireActivity(), flashCards, false);
        binding.setsRv.setAdapter(setsAdapter);
        setsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshFolders() {
        folders = folderDAO.getAllFolderByUserId(idUser);
        folderAdapter = new FolderAdapter(requireActivity(), folders);
        binding.foldersRv.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshClasses() {
        classes = groupDAO.getClassesOwnedByUser(idUser);
        classes.addAll(groupDAO.getClassesUserIsMemberOf(idUser));
        classAdapter = new ClassAdapter(requireActivity(), classes);
        binding.classesRv.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();
    }

    private void updateVisibility() {
        if (flashCards == null || flashCards.isEmpty()) {
            binding.setsCl.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.VISIBLE);
        }
        if (folders == null || folders.isEmpty()) {
            binding.folderCl.setVisibility(View.GONE);
        } else {
            binding.folderCl.setVisibility(View.VISIBLE);
        }
        if (classes == null || classes.isEmpty()) {
            binding.classCl.setVisibility(View.GONE);
        } else {
            binding.classCl.setVisibility(View.VISIBLE);
        }

        if ((flashCards == null || flashCards.isEmpty()) &&
                (folders == null || folders.isEmpty()) &&
                (classes == null || classes.isEmpty())) {
            binding.emptyCl.setVisibility(View.VISIBLE);
        } else {
            binding.emptyCl.setVisibility(View.GONE);
        }
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
