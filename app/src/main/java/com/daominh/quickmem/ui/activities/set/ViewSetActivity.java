package com.daominh.quickmem.ui.activities.set;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.R;
import com.daominh.quickmem.ui.activities.learn.TrueFalseFlashCardsActivity;
import com.daominh.quickmem.adapter.card.ViewTermsAdapter;
import com.daominh.quickmem.adapter.card.ViewSetAdapter;
import com.daominh.quickmem.data.dao.CardDAO;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.Card;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.ActivityViewSetBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.folder.AddToFolderActivity;
import com.daominh.quickmem.ui.activities.group.AddToClassActivity;
import com.daominh.quickmem.ui.activities.learn.LearnActivity;
import com.daominh.quickmem.ui.activities.learn.QuizActivity;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ViewSetActivity extends AppCompatActivity {
    private ActivityViewSetBinding binding;
    private CardDAO cardDAO;
    private FlashCardDAO flashCardDAO;
    private ArrayList<Card> cards;
    private LinearLayoutManager linearLayoutManager;
    private static final String LIST_POSITION = "list_position";
    private int listPosition = 0;
    private UserSharePreferences userSharePreferences;
    private String idCard;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        userSharePreferences = new UserSharePreferences(this);
        flashCardDAO = new FlashCardDAO(this);

        setupRecyclerView(savedInstanceState);
        setupCardData();
        setupNavigationListener();
        setupScrollListeners();
        setupOnScrollListener();
        setupUserDetails();
        setupReviewClickListener();
        setupLearnClickListener();
        setTrueFalseClickListener();
        setupToolbarNavigation();
    }

    private void setTrueFalseClickListener() {
        binding.trueFalseCl.setOnClickListener(v -> {
            if (!isUserOwner()) {
                showLearnErrorDialog();
            } else {
                Intent intent = new Intent(this, TrueFalseFlashCardsActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });
    }

    private void setupOnScrollListener() {
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
    }

    @SuppressLint("SetTextI18n")
    private void setupUserDetails() {
        String id = getIntent().getStringExtra("id");
        UserDAO userDAO = new UserDAO(this);
        flashCardDAO = new FlashCardDAO(this);
        User user = userDAO.getUserById(flashCardDAO.getFlashCardById(id).getUser_id());

        Picasso.get().load(user.getAvatar()).into(binding.avatarIv);
        binding.userNameTv.setText(user.getUsername());
        binding.descriptionTv.setText(flashCardDAO.getFlashCardById(id).getDescription());
        cardDAO = new CardDAO(this);
        binding.termCountTv.setText(cardDAO.countCardByFlashCardId(getIntent().getStringExtra("id")) + " " + getString(R.string.term));
        flashCardDAO = new FlashCardDAO(this);
        binding.setNameTv.setText(flashCardDAO.getFlashCardById(getIntent().getStringExtra("id")).getName());

        userSharePreferences = new UserSharePreferences(this);
    }

    private void setupReviewClickListener() {
        binding.reviewCl.setOnClickListener(v -> {
            if (!isUserOwner()) {
                showLearnErrorDialog();
            } else {
                Intent intent = new Intent(this, LearnActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });
    }

    private void setupLearnClickListener() {
        binding.learnCl.setOnClickListener(v -> {
            cardDAO = new CardDAO(this);

            if (!isUserOwner()) {
                showLearnErrorDialog();
                return;
            }

            if (cardDAO.countCardByFlashCardId(getIntent().getStringExtra("id")) < 4) {
                showReviewErrorDialog();
            } else {
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });
    }

    private void showReviewErrorDialog() {
        PopupDialog.getInstance(this)
                .setStyle(Styles.FAILED)
                .setHeading(getString(R.string.error))
                .setDescription(getString(R.string.learn_error))
                .setDismissButtonText(getString(R.string.ok))
                .setCancelable(true)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                        dialog.dismiss();
                    }
                });
    }

    private void showLearnErrorDialog() {
        PopupDialog.getInstance(this)
                .setStyle(Styles.STANDARD)
                .setHeading(getString(R.string.error))
                .setDescription(getString(R.string.review_error))
                .setPopupDialogIcon(R.drawable.baseline_error_24)
                .setDismissButtonText(getString(R.string.ok))
                .setNegativeButtonText(getString(R.string.cancel))
                .setPositiveButtonText(getString(R.string.ok))
                .setCancelable(true)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                    }

                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                        copyFlashCard();
                        PopupDialog.getInstance(ViewSetActivity.this)
                                .setStyle(Styles.SUCCESS)
                                .setHeading(getString(R.string.success))
                                .setDescription(getString(R.string.review_success))
                                .setCancelable(false)
                                .setDismissButtonText(getString(R.string.view))
                                .showDialog(new OnDialogButtonClickListener() {
                                    @Override
                                    public void onDismissClicked(Dialog dialog) {
                                        super.onDismissClicked(dialog);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

    }


    private void setupToolbarNavigation() {
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupRecyclerView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            listPosition = savedInstanceState.getInt(LIST_POSITION);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerViewSet.setLayoutManager(linearLayoutManager);
        binding.recyclerViewSet.scrollToPosition(listPosition);

        LinearLayoutManager linearLayoutManagerVertical = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );

        binding.recyclerViewTerms.setLayoutManager(linearLayoutManagerVertical);
        binding.recyclerViewTerms.setNestedScrollingEnabled(false);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupCardData() {
        String id = getIntent().getStringExtra("id");
        cardDAO = new CardDAO(this);
        cards = cardDAO.getCardsByFlashCardId(id);
        setUpProgress(cards);
        ViewSetAdapter viewSetAdapter = new ViewSetAdapter(this, cards);
        binding.recyclerViewSet.setAdapter(viewSetAdapter);
        viewSetAdapter.notifyDataSetChanged();

        ViewTermsAdapter viewTermsAdapter = new ViewTermsAdapter(cards);
        binding.recyclerViewTerms.setAdapter(viewTermsAdapter);
        viewTermsAdapter.notifyDataSetChanged();

    }

    private void setupNavigationListener() {
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher());
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
        if (item.getItemId() == R.id.menu) {
            new BottomSheetMenuDialogFragment.Builder(this)
                    .setSheet(R.menu.menu_bottom_view_set)
                    .setTitle(R.string.book)
                    .setListener(new BottomSheetListener() {
                        @Override
                        public void onSheetShown(@NotNull BottomSheetMenuDialogFragment bottomSheetMenuDialogFragment, @Nullable Object o) {
                        }

                        @Override
                        public void onSheetItemSelected(@NotNull BottomSheetMenuDialogFragment bottomSheetMenuDialogFragment, @NotNull MenuItem menuItem, @Nullable Object o) {
                            String id = getIntent().getStringExtra("id");

                            int itemId = menuItem.getItemId();
                            if (itemId == R.id.edit) {
                                if (isUserOwner()) {
                                    handleEditOption(id);
                                } else {
                                    Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
                                }
                            } else if (itemId == R.id.delete_set) {
                                if (isUserOwner()) {
                                    handleDeleteSetOption(id);
                                } else {
                                    Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
                                }
                            } else if (itemId == R.id.add_to_folder) {
                                if (isUserOwner()) {
                                    handleAddToFolderOption(id);
                                } else {
                                    Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
                                }
                            } else if (itemId == R.id.add_to_class) {
                                if (isUserOwner()) {
                                    handleAddToClassOption(id);
                                } else {
                                    Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
                                }
                            } else if (itemId == R.id.reset) {
                                if (isUserOwner()) {
                                    handleResetOption(id);
                                } else {
                                    Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onSheetDismissed(@NotNull BottomSheetMenuDialogFragment bottomSheetMenuDialogFragment, @Nullable Object o, int i) {
                        }
                    })
                    .setCloseTitle(getString(R.string.close))
                    .setAutoExpand(true)
                    .setCancelable(true)
                    .show(getSupportFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleEditOption(String id) {
        if (isUserOwner()) {
            Intent intent = new Intent(ViewSetActivity.this, EditFlashCardActivity.class);
            intent.putExtra("flashcard_id", id);
            startActivity(intent);
        } else {
            Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDeleteSetOption(String id) {
        if (isUserOwner()) {
            //dialog are you sure?
            showDeleteSetDialog(id);
        } else {
            Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleAddToFolderOption(String id) {
        Intent intent = new Intent(ViewSetActivity.this, AddToFolderActivity.class);
        intent.putExtra("flashcard_id", id);
        startActivity(intent);
    }

    private void handleAddToClassOption(String id) {
        Intent intent = new Intent(ViewSetActivity.this, AddToClassActivity.class);
        intent.putExtra("flashcard_id", id);
        startActivity(intent);
    }

    private void handleResetOption(String id) {
        if (isUserOwner()) {
            cardDAO = new CardDAO(ViewSetActivity.this);
            if (cardDAO.resetIsLearnedAndStatusCardByFlashCardId(id) > 0L) {
                Toast.makeText(ViewSetActivity.this, getString(R.string.reset_success), Toast.LENGTH_SHORT).show();
                setupCardData();
            } else {
                Toast.makeText(ViewSetActivity.this, getString(R.string.reset_error), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteSetDialog(String id) {
        PopupDialog.getInstance(ViewSetActivity.this)
                .setStyle(Styles.STANDARD)
                .setHeading(getString(R.string.delete_set))
                .setDescription(getString(R.string.delete_set_description))
                .setPopupDialogIcon(R.drawable.ic_delete)
                .setCancelable(true)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                        deleteSet(id);
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                        dialog.dismiss();
                    }
                });
    }

    private void deleteSet(String id) {
        FlashCardDAO flashCardDAO = new FlashCardDAO(ViewSetActivity.this);
        if (flashCardDAO.deleteFlashcardAndCards(id)) {
            PopupDialog.getInstance(ViewSetActivity.this)
                    .setStyle(Styles.SUCCESS)
                    .setHeading(getString(R.string.success))
                    .setDescription(getString(R.string.delete_set_success))
                    .setCancelable(false)
                    .setDismissButtonText(getString(R.string.ok))
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onDismissClicked(Dialog dialog) {
                            super.onDismissClicked(dialog);
                            finish();
                        }
                    });
        } else {
            PopupDialog.getInstance(ViewSetActivity.this)
                    .setStyle(Styles.FAILED)
                    .setHeading(getString(R.string.error))
                    .setDescription(getString(R.string.delete_set_error))
                    .setCancelable(true)
                    .showDialog(new OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveClicked(Dialog dialog) {
                            super.onPositiveClicked(dialog);
                        }
                    });
        }
    }

    private void copyFlashCard() {
        String id = getIntent().getStringExtra("id");
        userSharePreferences = new UserSharePreferences(this);
        flashCardDAO = new FlashCardDAO(this);
        FlashCard flashCard = flashCardDAO.getFlashCardById(id);
        idCard = genUUID();
        flashCard.setId(idCard);
        flashCard.setUser_id(getUser_id());
        flashCardDAO.insertFlashCard(flashCard);

        CardDAO cardDAO = new CardDAO(this);
        ArrayList<Card> cards = cardDAO.getCardsByFlashCardId(id);
        for (Card card : cards) {
            card.setId(genUUID());
            card.setFlashcard_id(flashCard.getId());
            card.setIsLearned(0);
            card.setStatus(0);
            card.setCreated_at(getCurrentDate());
            card.setUpdated_at(getCurrentDate());
            if (cardDAO.insertCard(card) > 0L) {
            } else {
                Toast.makeText(this, getString(R.string.review_error), Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(this, getString(R.string.review_success), Toast.LENGTH_SHORT).show();
    }


    private String getCurrentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            return getCurrentDateNewApi();
        } else {
            return getCurrentDateOldApi();
        }
    }

    private String genUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    private String getUser_id() {
        userSharePreferences = new UserSharePreferences(this);
        return userSharePreferences.getId();
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

    private boolean isUserOwner() {
        Log.d("isUserOwner", "isUserOwner: " + userSharePreferences.getId().equals(flashCardDAO.getFlashCardById(getIntent().getStringExtra("id")).getUser_id()));
        return userSharePreferences.getId().equals(flashCardDAO.getFlashCardById(getIntent().getStringExtra("id")).getUser_id());

    }

    @SuppressLint("SetTextI18n")
    private void setUpProgress(ArrayList<Card> cards) {
        int notLearned = 0;
        int learning = 0;
        int learned = 0;
        for (Card card : cards) {
            if (card.getStatus() == 0) {
                notLearned++;
            } else if (card.getStatus() == 1) {
                learned++;
            } else {
                learning++;
            }
        }

        if (isUserOwner()) {
            binding.notLearnTv.setText("Not learned: " + notLearned);
            binding.isLearningTv.setText("Learning: " + learning);
            binding.learnedTv.setText("Learned: " + learned);
        } else {
            binding.notLearnTv.setText("Not learned: " + cards.size());
            binding.isLearningTv.setText("Learning: " + 0);
            binding.learnedTv.setText("Learned: " + 0);
            binding.notLearnTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            binding.isLearningTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            binding.learnedTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCardData();
        setupUserDetails();
    }
}
