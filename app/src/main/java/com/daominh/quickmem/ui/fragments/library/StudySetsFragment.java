package com.daominh.quickmem.ui.fragments.library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.adapter.flashcard.SetCopyAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.databinding.FragmentStudySetsBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudySetsFragment extends Fragment {
    private FragmentStudySetsBinding binding;
    private ArrayList<FlashCard> flashCards;
    private FlashCardDAO flashCardDAO;
    private SetCopyAdapter setsAdapter;
    private String idUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flashCardDAO = new FlashCardDAO(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudySetsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    private void setupView() {
        UserSharePreferences userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
        binding.createSetBtn.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), CreateSetActivity.class)));
        flashCards = flashCardDAO.getAllFlashCardByUserId(idUser);
        updateVisibility();
        setupRecyclerView();
    }

    private void updateVisibility() {
        if (flashCards.isEmpty()) {
            binding.setsCl.setVisibility(View.VISIBLE);
            binding.setsRv.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.GONE);
            binding.setsRv.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
        binding.setsRv.setLayoutManager(linearLayoutManager);
        setsAdapter = new SetCopyAdapter(requireActivity(), flashCards);
        binding.setsRv.setAdapter(setsAdapter);
        setsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshData() {
        flashCards.clear();
        flashCards.addAll(flashCardDAO.getAllFlashCardByUserId(idUser));
        setsAdapter.notifyDataSetChanged();
        updateVisibility();
    }
}