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

import com.daominh.quickmem.adapter.flashcard.SetsAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.databinding.FragmentStudySetsBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;

import java.util.ArrayList;

public class StudySetsFragment extends Fragment {
    private FragmentStudySetsBinding binding;
    private UserSharePreferences userSharePreferences;
    private ArrayList<FlashCard> flashCards;
    private FlashCardDAO flashCardDAO;
    private SetsAdapter setsAdapter;
    private String idUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flashCardDAO = new FlashCardDAO(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudySetsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
        binding.createSetBtn.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), CreateSetActivity.class)));
        flashCards = flashCardDAO.getAllFlashCardByUserId(idUser);

        if (flashCards.isEmpty()) {
            binding.setsCl.setVisibility(View.VISIBLE);
            binding.setsRv.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.GONE);
            binding.setsRv.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
        binding.setsRv.setLayoutManager(linearLayoutManager);
        setsAdapter = new SetsAdapter(requireActivity(), flashCards, true);
        binding.setsRv.setAdapter(setsAdapter);
        setsAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        flashCards.clear();
        flashCards.addAll(flashCardDAO.getAllFlashCardByUserId(idUser));
        setsAdapter.notifyDataSetChanged();

        if (flashCards.isEmpty()) {
            binding.setsCl.setVisibility(View.VISIBLE);
            binding.setsRv.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.GONE);
            binding.setsRv.setVisibility(View.VISIBLE);
        }
    }
}