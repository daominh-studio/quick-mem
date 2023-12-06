package com.daominh.quickmem.ui.activities.classes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }

    private void fetchFlashCards() {
        ArrayList<String> listId = groupDAO.getAllFlashCardInClass(userSharePreferences.getClassId());
        Toast.makeText(requireActivity(), listId.size() + "", Toast.LENGTH_SHORT).show();
        for (String id : listId) {
            flashCards.add(flashCardDAO.getFlashCardById(id));
            Toast.makeText(requireActivity(), id, Toast.LENGTH_SHORT).show();
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
    private void removeIdClass(){
        userSharePreferences = new UserSharePreferences(requireActivity());
        userSharePreferences.removeClassId();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeIdClass();
    }
}