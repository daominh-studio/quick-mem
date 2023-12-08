package com.daominh.quickmem.ui.activities.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.daominh.quickmem.adapter.flashcard.SetAllAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.databinding.ActivityViewSearchBinding;
import java.util.ArrayList;

public class ViewSearchActivity extends AppCompatActivity {
    private ActivityViewSearchBinding binding;
    private ArrayList<FlashCard> flashCards;
    private FlashCardDAO flashCardDAO;
    private SetAllAdapter setAllAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBackButton();
        setupData();
        setupSets();
        setupSearchView();
    }

    private void setupBackButton() {
        binding.backIv.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupData() {
        flashCardDAO = new FlashCardDAO(this);
    }

    private void setupSets() {
        flashCards = flashCardDAO.getAllFlashCardPublic();
        setAllAdapter = new SetAllAdapter(this, flashCards);
        binding.setsRv.setLayoutManager(new LinearLayoutManager(this));
        binding.setsRv.setAdapter(setAllAdapter);
        binding.setsCl.setVisibility(flashCards.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearchQuery(newText);
                return true;
            }
        });
    }

    private void handleSearchQuery(String newText) {
        ArrayList<FlashCard> filteredFlashCards = new ArrayList<>();
        for (FlashCard flashCard : flashCards) {
            if (flashCard.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredFlashCards.add(flashCard);
            }
        }
        updateAdapters(filteredFlashCards);
        updateVisibility(newText, filteredFlashCards);
    }

    private void updateAdapters(ArrayList<FlashCard> flashCards) {
        setAllAdapter = new SetAllAdapter(this, flashCards);
        binding.setsRv.setAdapter(setAllAdapter);
    }

    private void updateVisibility(String newText, ArrayList<FlashCard> flashCards) {
        boolean isSearchEmpty = newText.isEmpty();
        boolean isFlashCardsEmpty = flashCards.isEmpty();

        binding.setsCl.setVisibility(isSearchEmpty || isFlashCardsEmpty ? View.GONE : View.VISIBLE);
        binding.enterTopicTv.setVisibility(isSearchEmpty ? View.VISIBLE : View.GONE);
        binding.noResultTv.setVisibility(isSearchEmpty || !isFlashCardsEmpty ? View.GONE : View.VISIBLE);
    }
}