package com.daominh.quickmem.ui.activities.create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.CardAdapter;
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
    private int selectedPosition = -1;
    ActivityCreateSetBinding binding;
    CardDAO cardDAO;
    FlashCardDAO flashCardDAO;

    UserSharePreferences userSharePreferences;


    final String id = genUUID();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSetBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());


        if (binding.subjectEt.getText().toString().isEmpty()) {
            binding.subjectEt.requestFocus();
        }


        binding.descriptionTv.setOnClickListener(v -> binding.descriptionTil.setVisibility(View.VISIBLE));

        //create list two set
        cards = new ArrayList<>();
        cards.add(new Card());
        cards.add(new Card());

        cardAdapter = new CardAdapter(this, cards);
        binding.cardsLv.setAdapter(cardAdapter);
        binding.cardsLv.setLayoutManager(new LinearLayoutManager(this));
        binding.cardsLv.setHasFixedSize(true);
        cardAdapter.notifyDataSetChanged();


        cardAdapter.setOnItemClickListener(position -> {
            selectedPosition = position;
            updateToolbarTitle();
        });


        binding.addFab.setOnClickListener(v -> {
            cards.add(new Card());
            selectedPosition = cards.size() - 1;
            updateToolbarTitle();
            //scroll to last item
            binding.cardsLv.smoothScrollToPosition(cards.size() - 1);

            //notify adapter
            cardAdapter.notifyItemInserted(cards.size() - 1);

            binding.cardsLv.post(() -> {
                RecyclerView.ViewHolder viewHolder = binding.cardsLv.findViewHolderForAdapterPosition(cards.size() - 1);
                if (viewHolder != null) {
                    viewHolder.itemView.requestFocus();
                }
            });
        });


        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Backup of removed item for undo purpose
                Card deletedItem = cards.get(position);

                // Removing item from recycler view
                cards.remove(position);
                cardAdapter.notifyItemRemoved(position);

                // Showing Snack bar with Undo option
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {

                    // Check if the position is valid before adding the item back
                    if (position >= 0 && position <= cards.size()) {
                        cards.add(position, deletedItem);
                        cardAdapter.notifyItemInserted(position);
                    } else {
                        // If the position isn't valid, show a message or handle the error appropriately
                        Toast.makeText(getApplicationContext(), "Error restoring item", Toast.LENGTH_LONG).show();
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }

            @Override
            public void onChildDraw(@NotNull Canvas c, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

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
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.cardsLv);
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
        if (item.getItemId() == R.id.custom_set) {
            final String user_id = getUser_id();
            Toast.makeText(this, user_id, Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.done) {
            saveChanges();
            return true;
        } else {
            return super.onOptionsItemSelected(item);

        }

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

        //save all cards
        for (Card card : cards) {
            String front = card.getFront();
            String back = card.getBack();

            if (front.isEmpty()) {
                binding.cardsLv.requestFocus();
                Toast.makeText(this, "Please enter front", Toast.LENGTH_SHORT).show();
                return;
            }

            if (back.isEmpty()) {
                binding.cardsLv.requestFocus();
                Toast.makeText(this, "Please enter back", Toast.LENGTH_SHORT).show();
                return;
            }

            cardDAO = new CardDAO(this);
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
                return;
            }
        }

        //save flashcard
        flashCardDAO = new FlashCardDAO(this);
        FlashCard flashCard = new FlashCard();
        flashCard.setName(subject);
        flashCard.setDescription(description);
        userSharePreferences = new UserSharePreferences(this);
        flashCard.setUser_id(userSharePreferences.getId());
        flashCard.setCreated_at(getCurrentDate());
        flashCard.setUpdated_at(getCurrentDate());
        flashCard.setId(id);

        if (flashCardDAO.insertFlashCard(flashCard) > 0) {
            Intent intent = new Intent(this, ViewSetActivity.class);
            intent.putExtra("id", flashCard.getId());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Insert flashcard failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateToolbarTitle() {
        if (selectedPosition >= 0 && selectedPosition < cards.size()) {
            int positionToShow = selectedPosition + 1;
            int totalItems = cards.size();
            String title = positionToShow + "/" + totalItems;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    private String getCurrentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return currentDate.format(formatter);
        } else {
            // Handle a case for Android versions less than Oreo
            // Here we're using SimpleDateFormat which is available on all Android versions
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(new Date());
        }
    }

    private String genUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    private String getUser_id() {
        userSharePreferences = new UserSharePreferences(this);
        return userSharePreferences.getId();
    }

}