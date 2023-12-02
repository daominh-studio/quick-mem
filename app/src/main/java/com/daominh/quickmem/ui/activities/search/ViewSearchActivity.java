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

        binding.backIv.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        //get data from intent
        flashCardDAO = new FlashCardDAO(this);
        userSharePreferences = new UserSharePreferences(this);
        idUser = userSharePreferences.getId();
        groupDAO = new GroupDAO(this);


        //set data for sets
        flashCards = flashCardDAO.getAllFlashCard();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewSearchActivity.this, RecyclerView.VERTICAL, false);
        binding.setsRv.setLayoutManager(linearLayoutManager);
        setAllAdapter = new SetAllAdapter(ViewSearchActivity.this, flashCards);
        binding.setsRv.setAdapter(setAllAdapter);
        setAllAdapter.notifyDataSetChanged();

        // if list flashcards is empty, gone sets_cl
        if (flashCards.size() == 0) {
            binding.setsCl.setVisibility(View.GONE);
        } else {
            binding.setsCl.setVisibility(View.VISIBLE);
        }


        //set data for classes
        groups = groupDAO.getAllClasses();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(ViewSearchActivity.this, RecyclerView.VERTICAL, false);
        binding.classesRv.setLayoutManager(linearLayoutManager1);
        classesAllAdapter = new ClassesAllAdapter(ViewSearchActivity.this, groups);
        binding.classesRv.setAdapter(classesAllAdapter);
        classesAllAdapter.notifyDataSetChanged();

        // if list classes is empty, gone class_cl
        if (groups.size() == 0) {
            binding.classCl.setVisibility(View.GONE);
        } else {
            binding.classCl.setVisibility(View.VISIBLE);
        }


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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

                //update adapter with new data
                setAllAdapter = new SetAllAdapter(ViewSearchActivity.this, flashCards);
                binding.setsRv.setAdapter(setAllAdapter);
                setAllAdapter.notifyDataSetChanged();

                classesAllAdapter = new ClassesAllAdapter(ViewSearchActivity.this, groups);
                binding.classesRv.setAdapter(classesAllAdapter);
                classesAllAdapter.notifyDataSetChanged();

                // if newtext is empty, enable enter_topic_tv, gone setcl and classcl
                if (newText.isEmpty()) {
                    binding.setsCl.setVisibility(View.GONE);
                    binding.classCl.setVisibility(View.GONE);
                    binding.noResultTv.setVisibility(View.GONE);
                    binding.enterTopicTv.setVisibility(View.VISIBLE);
                } else {
                    binding.enterTopicTv.setVisibility(View.GONE);
                    // if flashcards no have data, gone setcl
                    if (flashCards.size() == 0) {
                        binding.setsCl.setVisibility(View.GONE);
                    } else {
                        binding.setsCl.setVisibility(View.VISIBLE);
                    }

                    //if classes no have data, gone classcl
                    if (groups.size() == 0) {
                        binding.classCl.setVisibility(View.GONE);
                    } else {
                        binding.classCl.setVisibility(View.VISIBLE);
                    }

                    //if flashcards and classes no have data, show no_result_tv
                    if (flashCards.size() == 0 && groups.size() == 0) {
                        binding.noResultTv.setVisibility(View.VISIBLE);
                    } else {
                        binding.noResultTv.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });
    }
}