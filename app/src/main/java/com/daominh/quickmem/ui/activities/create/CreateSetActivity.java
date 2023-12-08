package com.daominh.quickmem.ui.activities.create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.card.CardAdapter;
import com.daominh.quickmem.data.dao.CardDAO;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.model.Card;

import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.databinding.ActivityCreateSetBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.set.ViewSetActivity;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class CreateSetActivity extends AppCompatActivity {
    private CardAdapter cardAdapter;
    private ArrayList<Card> cards;
    private ActivityCreateSetBinding binding;
    private final String id = genUUID();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSetBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setupToolbar();
        setupSubjectEditText();
        setupDescriptionTextView();
        setupCardsList();
        setupCardAdapter();
        setupAddFab();
        setupItemTouchHelper();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupSubjectEditText() {
        if (binding.subjectEt.getText().toString().isEmpty()) {
            binding.subjectEt.requestFocus();
        }
    }

    private void setupDescriptionTextView() {
        binding.descriptionTv.setOnClickListener(v -> {
            if (binding.descriptionTil.getVisibility() == View.GONE) {
                binding.descriptionTil.setVisibility(View.VISIBLE);
            } else {
                binding.descriptionTil.setVisibility(View.GONE);
            }
        });
    }

    private void setupCardsList() {
        //create list two set
        cards = new ArrayList<>();
        cards.add(new Card());
        cards.add(new Card());
        updateTotalCards();
    }

    private void updateTotalCards() {
        binding.totalCardsTv.setText(String.format("Total Cards: %s", cards.size()));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupCardAdapter() {
        cardAdapter = new CardAdapter(this, cards);
        binding.cardsLv.setAdapter(cardAdapter);
        binding.cardsLv.setLayoutManager(new LinearLayoutManager(this));
        binding.cardsLv.setHasFixedSize(true);
        cardAdapter.notifyDataSetChanged();

    }

    private void setupAddFab() {
        binding.addFab.setOnClickListener(v -> {
            if (!checkTwoCardsEmpty()) {

                Card newCard = new Card();
                cards.add(newCard);
                //scroll to last item
                binding.cardsLv.smoothScrollToPosition(cards.size() - 1);
                //notify adapter
                cardAdapter.notifyItemInserted(cards.size() - 1);
                updateTotalCards();

            } else {
                Toast.makeText(this, "Please enter front and back", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback callback = createItemTouchHelperCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.cardsLv);
    }

    private ItemTouchHelper.SimpleCallback createItemTouchHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                handleOnSwiped(viewHolder);
            }

            @Override
            public void onChildDraw(@NotNull Canvas c, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                handleOnChildDraw(c, viewHolder, dX);
            }
        };
    }

    private void handleOnSwiped(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getBindingAdapterPosition();

        // Backup of removed item for undo purpose
        Card deletedItem = cards.get(position);

        // Removing item from recycler view
        cards.remove(position);
        updateTotalCards();
        cardAdapter.notifyItemRemoved(position);

        // Showing Snack bar with an Undo option
        Snackbar snackbar = Snackbar.make(binding.getRoot(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", view -> {

            // Check if the position is valid before adding the item back
            if (position >= 0 && position <= cards.size()) {
                cards.add(position, deletedItem);
                cardAdapter.notifyItemInserted(position);
                updateTotalCards();
            } else {
                // If the position isn't valid, show a message or handle the error appropriately
                Toast.makeText(getApplicationContext(), "Error restoring item", Toast.LENGTH_LONG).show();
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void handleOnChildDraw(@NotNull Canvas c, @NotNull RecyclerView.ViewHolder viewHolder, float dX) {
        Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete);
        View itemView = viewHolder.itemView;
        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            final ColorDrawable background = new ColorDrawable(Color.WHITE);
            background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);
        } else { // No swipe
            icon.setBounds(0, 0, 0, 0);
        }

        icon.draw(c);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_set, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            saveChanges();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void saveChanges() {
        String subject = binding.subjectEt.getText().toString();
        String description = binding.descriptionEt.getText().toString();

        if (subject.isEmpty()) {
            binding.subjectTil.setError("Please enter subject");
            binding.subjectEt.requestFocus();
            return;
        } else {
            binding.subjectTil.setError(null);
        }

        if (!saveAllCards()) {
            return;
        }

        if (!saveFlashCard(subject, description)) {
            Toast.makeText(this, "Insert flashcard failed", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ViewSetActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

    private boolean saveAllCards() {
        for (Card card : cards) {
            if (!saveCard(card)) {
                return false;
            }
        }
        return true;
    }

    private boolean saveCard(Card card) {
        String front = card.getFront();
        String back = card.getBack();

        if (front == null || front.isEmpty()) {
            binding.cardsLv.requestFocus();
            Toast.makeText(this, "Please enter front", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (back == null || back.isEmpty()) {
            binding.cardsLv.requestFocus();
            Toast.makeText(this, "Please enter back", Toast.LENGTH_SHORT).show();
            return false;
        }

        CardDAO cardDAO = new CardDAO(this);
        card.setId(genUUID());
        card.setFront(front);
        card.setBack(back);
        card.setStatus(0);
        card.setIsLearned(0);
        card.setFlashcard_id(id);
        card.setCreated_at(getCurrentDate());
        card.setUpdated_at(getCurrentDate());
        if (cardDAO.insertCard(card) <= 0) {
            Toast.makeText(this, "Insert card failed" + id, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean saveFlashCard(String subject, String description) {
        FlashCardDAO flashCardDAO = new FlashCardDAO(this);
        FlashCard flashCard = new FlashCard();
        flashCard.setName(subject);
        flashCard.setDescription(description);
        UserSharePreferences userSharePreferences = new UserSharePreferences(this);
        flashCard.setUser_id(userSharePreferences.getId());
        flashCard.setCreated_at(getCurrentDate());
        flashCard.setUpdated_at(getCurrentDate());
        flashCard.setId(id);
        binding.privateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "Public", Toast.LENGTH_SHORT).show();
                flashCard.setIs_public(1);
            } else {
                Toast.makeText(this, "Private", Toast.LENGTH_SHORT).show();
                flashCard.setIs_public(0);
            }
        });

        return flashCardDAO.insertFlashCard(flashCard) > 0;
    }

    private String getCurrentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            return getCurrentDateNewApi();
        } else {
            return getCurrentDateOldApi();
        }
    }

    public boolean checkTwoCardsEmpty() {
        // check if 2 cards are empty return true
        int emptyCount = 0;
        for (Card card : cards) {
            if (card.getFront() == null || card.getFront().isEmpty() || card.getBack() == null || card.getBack().isEmpty()) {
                emptyCount++;
                if (emptyCount == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private String genUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentDateNewApi() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(formatter);
    }

    private String getCurrentDateOldApi() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }
}