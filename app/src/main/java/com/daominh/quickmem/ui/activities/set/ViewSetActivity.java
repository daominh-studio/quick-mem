package com.daominh.quickmem.ui.activities.set;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.EditFlashCardActivity;
import com.daominh.quickmem.R;
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
    CardDAO cardDAO;
    FlashCardDAO flashCardDAO;
    ArrayList<Card> cards;
    ViewSetAdapter viewSetAdapter;
    LinearLayoutManager linearLayoutManager;
    private static final String LIST_POSITION = "list_position";
    int listPosition = 0;
    UserSharePreferences userSharePreferences;
    String idCard;


    @SuppressLint("SetTextI18n")
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

        String id = getIntent().getStringExtra("id");
        UserDAO userDAO = new UserDAO(this);
        flashCardDAO = new FlashCardDAO(this);
        User user = userDAO.getUserById(flashCardDAO.getFlashCardById(id).getUser_id());

        Picasso.get().load(user.getAvatar()).into(binding.avatarIv);
        binding.userNameTv.setText(user.getUsername());
        cardDAO = new CardDAO(this);
        binding.termCountTv.setText(cardDAO.countCardByFlashCardId(getIntent().getStringExtra("id")) + " " + getString(R.string.term));
        flashCardDAO = new FlashCardDAO(this);
        binding.setNameTv.setText(flashCardDAO.getFlashCardById(getIntent().getStringExtra("id")).getName());

        userSharePreferences = new UserSharePreferences(this);

        binding.reviewCl.setOnClickListener(v -> {
            if (!userSharePreferences.getId().equals(flashCardDAO.getFlashCardById(getIntent().getStringExtra("id")).getUser_id())) {
                PopupDialog.getInstance(this)
                        .setStyle(Styles.STANDARD)
                        .setHeading(getString(R.string.error))
                        .setDescription(getString(R.string.review_error))
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
                                                Intent intent = new Intent(ViewSetActivity.this, ViewSetActivity.class);
                                                intent.putExtra("id", idCard);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        });
                return;
            }
        });
        binding.learnCl.setOnClickListener(v -> {
            cardDAO = new CardDAO(this);
            if (cardDAO.countCardByFlashCardId(getIntent().getStringExtra("id")) < 4) {
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
                return;
            }
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);

        });

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
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupCardData() {
        String id = getIntent().getStringExtra("id");
        cardDAO = new CardDAO(this);
        cards = cardDAO.getCardsByFlashCardId(id);
        viewSetAdapter = new ViewSetAdapter(this, cards);
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
        if (isUserOwner()){
            MenuItem editItem = menu.findItem(R.id.edit);
            MenuItem deleteItem = menu.findItem(R.id.delete_set);
            MenuItem resetItem = menu.findItem(R.id.reset);

            if (editItem != null) {
                editItem.setVisible(false);
            }
            if (deleteItem != null) {
                deleteItem.setVisible(false);
            }
            if (resetItem != null) {
                resetItem.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.edit);
        MenuItem deleteItem = menu.findItem(R.id.delete_set);
        MenuItem resetItem = menu.findItem(R.id.reset);

        if (isUserOwner()) {
            if (editItem != null) {
                editItem.setVisible(false);
            }
            if (deleteItem != null) {
                deleteItem.setVisible(false);
            }
            if (resetItem != null) {
                resetItem.setVisible(false);
            }
        } else {
            if (editItem != null) {
                editItem.setVisible(true);
            }
            if (deleteItem != null) {
                deleteItem.setVisible(true);
            }
            if (resetItem != null) {
                resetItem.setVisible(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
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


                            if (menuItem.getItemId() == R.id.edit) {
                               if (isUserOwner()){
                                   Intent intent = new Intent(ViewSetActivity.this, EditFlashCardActivity.class);
                                   intent.putExtra("id", id);
                                   startActivity(intent);
                                 }else {
                                   Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
                               }
                            } else if (menuItem.getItemId() == R.id.delete_set) {
                              if (isUserOwner()){
                                  //dialog are you sure?
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
                                                  FlashCardDAO flashCardDAO = new FlashCardDAO(ViewSetActivity.this);
                                                  if (flashCardDAO.deleteFlashcardAndCards(getIntent().getStringExtra("id"))) {
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

                                              @Override
                                              public void onNegativeClicked(Dialog dialog) {
                                                  super.onNegativeClicked(dialog);
                                                  dialog.dismiss();
                                              }
                                          });
                              }else {
                                  Toast.makeText(ViewSetActivity.this, getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
                              }
                            } else if (menuItem.getItemId() == R.id.add_to_folder) {
                                Intent intent = new Intent(ViewSetActivity.this, AddToFolderActivity.class);
                                intent.putExtra("flashcard_id", id);
                                startActivity(intent);
                            } else if (menuItem.getItemId() == R.id.add_to_class) {
                                Intent intent = new Intent(ViewSetActivity.this, AddToClassActivity.class);
                                intent.putExtra("flashcard_id", id);
                                startActivity(intent);
                            } else if (menuItem.getItemId() == R.id.reset) {
                                if (isUserOwner()){
                                    cardDAO = new CardDAO(ViewSetActivity.this);
                                    cardDAO.resetIsLearnedAndStatusCardByFlashCardId(id);
                                    Toast.makeText(ViewSetActivity.this, getString(R.string.reset_success), Toast.LENGTH_SHORT).show();
                                }else {
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

    private void copyFlashCard(){
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
            if (cardDAO.insertCard(card) > 0L){
                Toast.makeText(this, getString(R.string.review_success), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, getString(R.string.review_error), Toast.LENGTH_SHORT).show();
            }
        }
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
        return userSharePreferences.getId().equals(flashCardDAO.getFlashCardById(getIntent().getStringExtra("id")).getUser_id());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCardData();
    }
}
