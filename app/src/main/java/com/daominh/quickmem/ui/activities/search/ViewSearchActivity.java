package com.daominh.quickmem.ui.activities.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.daominh.quickmem.adapter.flashcard.SetAllAdapter;
import com.daominh.quickmem.adapter.group.ClassesAllAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.databinding.ActivityViewSearchBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;

import java.util.ArrayList;

public class ViewSearchActivity extends AppCompatActivity {
    private ActivityViewSearchBinding binding;
    private UserSharePreferences userSharePreferences;
    private ArrayList<FlashCard> flashCards;
    private ArrayList<Group> groups;
    private FlashCardDAO flashCardDAO;
    private GroupDAO groupDAO;
    private SetAllAdapter setAllAdapter;
    private ClassesAllAdapter classesAllAdapter;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBackButton();
        setupData();
        setupSets();
        setupClasses();
        setupSearchView();
    }

    private void setupBackButton() {
        binding.backIv.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupData() {
        flashCardDAO = new FlashCardDAO(this);
        userSharePreferences = new UserSharePreferences(this);
        idUser = userSharePreferences.getId();
        groupDAO = new GroupDAO(this);
    }

    private void setupSets() {
        flashCards = flashCardDAO.getAllFlashCard();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewSearchActivity.this, RecyclerView.VERTICAL, false);
        binding.setsRv.setLayoutManager(linearLayoutManager);
        setAllAdapter = new SetAllAdapter(ViewSearchActivity.this, flashCards);
        binding.setsRv.setAdapter(setAllAdapter);
        setAllAdapter.notifyDataSetChanged();

        if (flashCards.isEmpty()) {
            binding.setsCl.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.VISIBLE);
        }
    }

    private void setupClasses() {
        groups = groupDAO.getAllClasses();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(ViewSearchActivity.this, RecyclerView.VERTICAL, false);
        binding.classesRv.setLayoutManager(linearLayoutManager1);
        classesAllAdapter = new ClassesAllAdapter(ViewSearchActivity.this, groups);
        binding.classesRv.setAdapter(classesAllAdapter);
        classesAllAdapter.notifyDataSetChanged();

        if (groups.isEmpty()) {
            binding.classCl.setVisibility(View.GONE);
        } else {
            binding.classCl.setVisibility(View.VISIBLE);
        }
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
        ArrayList<FlashCard> flashCards = new ArrayList<>();
        ArrayList<Group> groups = new ArrayList<>();

        for (FlashCard flashCard : ViewSearchActivity.this.flashCards) {
            if (flashCard.getName().toLowerCase().contains(newText.toLowerCase())) {
                flashCards.add(flashCard);
            }
        }
        for (Group group : ViewSearchActivity.this.groups) {
            if (group.getName().toLowerCase().contains(newText.toLowerCase())) {
                groups.add(group);
            }
        }

        updateAdapters(flashCards, groups);
        updateVisibility(newText, flashCards, groups);
    }

    private void updateAdapters(ArrayList<FlashCard> flashCards, ArrayList<Group> groups) {
        setAllAdapter = new SetAllAdapter(ViewSearchActivity.this, flashCards);
        binding.setsRv.setAdapter(setAllAdapter);
        setAllAdapter.notifyDataSetChanged();

        classesAllAdapter = new ClassesAllAdapter(ViewSearchActivity.this, groups);
        binding.classesRv.setAdapter(classesAllAdapter);
        classesAllAdapter.notifyDataSetChanged();
    }

    private void updateVisibility(String newText, ArrayList<FlashCard> flashCards, ArrayList<Group> groups) {
        if (newText.isEmpty()) {
            binding.setsCl.setVisibility(View.GONE);
            binding.classCl.setVisibility(View.GONE);
            binding.noResultTv.setVisibility(View.GONE);
            binding.enterTopicTv.setVisibility(View.VISIBLE);
        } else {
            binding.enterTopicTv.setVisibility(View.GONE);
            if (flashCards.isEmpty()) {
                binding.setsCl.setVisibility(View.GONE);
            } else {
                binding.setsCl.setVisibility(View.VISIBLE);
            }

            if (groups.isEmpty()) {
                binding.classCl.setVisibility(View.GONE);
            } else {
                binding.classCl.setVisibility(View.VISIBLE);
            }

            if (flashCards.isEmpty() && groups.isEmpty()) {
                binding.noResultTv.setVisibility(View.VISIBLE);
            } else {
                binding.noResultTv.setVisibility(View.GONE);
            }
        }
    }
}