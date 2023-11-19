package com.daominh.quickmem.ui.activities.set;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.ViewSetAdapter;
import com.daominh.quickmem.data.dao.CardDAO;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.model.Card;
import com.daominh.quickmem.databinding.ActivityViewSetBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.learn.LearnActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewSetActivity extends AppCompatActivity {
    private ActivityViewSetBinding binding;
    CardDAO cardDAO;
    ArrayList<Card> cards;
    ViewSetAdapter viewSetAdapter;
    LinearLayoutManager linearLayoutManager;
    private static final String LIST_POSITION = "list_position";
    int listPosition = 0;
    UserSharePreferences userSharePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        setupRecyclerView(savedInstanceState);
        setupCardData();
        setupNavigationListener();
        setupScrollListeners();

        binding.recyclerViewSet.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int centerPosition = linearLayoutManager.findFirstVisibleItemPosition() + 1;
                binding.centerTv.setText(String.valueOf(centerPosition));
                binding.previousTv.setText(centerPosition > 1 ? String.valueOf(centerPosition - 1) : "");
                binding.nextTv.setText(centerPosition < cards.size() ? String.valueOf(centerPosition + 1) : "");
            }
        });

        userSharePreferences = new UserSharePreferences(this);
        Picasso.get().load(userSharePreferences.getAvatar()).into(binding.avatarIv);
        binding.userNameTv.setText(userSharePreferences.getUserName());
        cardDAO = new CardDAO(this);
        binding.termCountTv.setText(String.valueOf(cardDAO.countCardByFlashCardId(getIntent().getStringExtra("id"))) + " " + getString(R.string.term));


        binding.reviewCl.setOnClickListener(v -> {
            Intent intent = new Intent(this, LearnActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
        });

    }

    private void setupRecyclerView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            listPosition = savedInstanceState.getInt(LIST_POSITION);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerViewSet.setLayoutManager(linearLayoutManager);
        binding.recyclerViewSet.scrollToPosition(listPosition);
    }

    private void setupCardData() {
        String id = getIntent().getStringExtra("id");
        cardDAO = new CardDAO(this);
        cards = cardDAO.getCardsByFlashCardId(id);
        viewSetAdapter = new ViewSetAdapter(this, cards);
        binding.recyclerViewSet.setAdapter(viewSetAdapter);
        viewSetAdapter.notifyDataSetChanged();
    }

    private void setupNavigationListener() {
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupScrollListeners() {
        binding.previousIv.setOnClickListener(v -> {
            int currentPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (currentPosition > 0) {
                binding.recyclerViewSet.scrollToPosition(currentPosition - 1);
            }
        });

        binding.nextIv.setOnClickListener(v -> {
            int currentPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (currentPosition < cards.size() - 1) {
                binding.recyclerViewSet.scrollToPosition(currentPosition + 1);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LIST_POSITION, linearLayoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}