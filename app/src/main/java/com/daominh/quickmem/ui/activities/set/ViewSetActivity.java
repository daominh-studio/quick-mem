package com.daominh.quickmem.ui.activities.set;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
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

import java.util.ArrayList;

public class ViewSetActivity extends AppCompatActivity {
    private ActivityViewSetBinding binding;
    CardDAO cardDAO;
    FlashCardDAO flashCardDAO;
    ArrayList<Card> cards;
    ViewSetAdapter viewSetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewSetBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        binding.toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        CardDAO cardDAO = new CardDAO(this);
        cards = cardDAO.getCardsByFlashCardId(id);
        Toast.makeText(this, cards.size() + "", Toast.LENGTH_SHORT).show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerViewSet.setLayoutManager(linearLayoutManager);

        viewSetAdapter = new ViewSetAdapter(this, cards);
        binding.recyclerViewSet.setAdapter(viewSetAdapter);
        viewSetAdapter.notifyDataSetChanged();
    }
}