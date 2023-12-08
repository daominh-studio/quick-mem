package com.daominh.quickmem.ui.activities.classes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daominh.quickmem.ui.activities.set.AddFlashCardToClassActivity;
import com.daominh.quickmem.adapter.flashcard.SetCopyAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.databinding.FragmentViewSetsBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ViewSetsFragment extends Fragment {
    private FragmentViewSetsBinding binding;
    private UserSharePreferences userSharePreferences;
    private final ArrayList<FlashCard> flashCards = new ArrayList<>();
    private FlashCardDAO flashCardDAO;
    private GroupDAO groupDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flashCardDAO = new FlashCardDAO(requireActivity());
        groupDAO = new GroupDAO(requireActivity());
        userSharePreferences = new UserSharePreferences(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewSetsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());
        binding.addSetsBtn.setOnClickListener(view1 -> {

        });

        fetchFlashCards();
        setupVisibility();
        setupRecyclerView();

        binding.addSetsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AddFlashCardToClassActivity.class);
            intent.putExtra("flashcard_id", userSharePreferences.getClassId());
            startActivity(intent);
        });
    }

    private void fetchFlashCards() {
        ArrayList<String> listId = groupDAO.getAllFlashCardInClass(userSharePreferences.getClassId());
        for (String id : listId) {
            flashCards.add(flashCardDAO.getFlashCardById(id));
        }
    }

    private void setupVisibility() {
        if (flashCards.isEmpty()) {
            binding.setsLl.setVisibility(View.VISIBLE);
            binding.setsRv.setVisibility(View.GONE);
        } else {
            binding.setsLl.setVisibility(View.GONE);
            binding.setsRv.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerView() {
        SetCopyAdapter setsAdapter = new SetCopyAdapter(requireActivity(), flashCards);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.setsRv.setLayoutManager(linearLayoutManager);
        binding.setsRv.setAdapter(setsAdapter);
        binding.setsRv.setHasFixedSize(true);
        setsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    @Override
    public void onPause() {
        super.onPause();
        setupRecyclerView();
    }
}