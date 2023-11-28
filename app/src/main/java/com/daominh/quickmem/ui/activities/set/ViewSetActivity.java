package com.daominh.quickmem.ui.activities.set;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
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
import com.daominh.quickmem.ui.activities.MainActivity;
import com.daominh.quickmem.ui.activities.folder.AddToFolderActivity;
import com.daominh.quickmem.ui.activities.group.AddToClassActivity;
import com.daominh.quickmem.ui.activities.learn.LearnActivity;
import com.daominh.quickmem.ui.activities.learn.QuizActivity;
import com.daominh.quickmem.ui.activities.learn.SelectDefineActivity;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment;
import com.kennyc.bottomsheet.menu.BottomSheetMenu;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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

        userSharePreferences = new UserSharePreferences(this);
        Picasso.get().load(userSharePreferences.getAvatar()).into(binding.avatarIv);
        binding.userNameTv.setText(userSharePreferences.getUserName());
        cardDAO = new CardDAO(this);
        binding.termCountTv.setText(cardDAO.countCardByFlashCardId(getIntent().getStringExtra("id")) + " " + getString(R.string.term));
        flashCardDAO = new FlashCardDAO(this);
        binding.setNameTv.setText(flashCardDAO.getFlashCardById(getIntent().getStringExtra("id")).getName());


        binding.reviewCl.setOnClickListener(v -> {
            Intent intent = new Intent(this, LearnActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
        });
        binding.learnCl.setOnClickListener(v -> {
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

                            if (menuItem.getItemId() == R.id.edit) {
                                Toast.makeText(ViewSetActivity.this, "edit", Toast.LENGTH_SHORT).show();
                            } else if (menuItem.getItemId() == R.id.delete_set) {
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
                            } else if (menuItem.getItemId() == R.id.add_to_folder) {
                                Intent intent = new Intent(ViewSetActivity.this, AddToFolderActivity.class);
                                intent.putExtra("flashcard_id", getIntent().getStringExtra("id"));
                                startActivity(intent);
                            } else if (menuItem.getItemId() == R.id.add_to_class) {
                                Intent intent = new Intent(ViewSetActivity.this, AddToClassActivity.class);
                                intent.putExtra("flashcard_id", getIntent().getStringExtra("id"));
                                startActivity(intent);
                            } else if (menuItem.getItemId() == R.id.save_and_edit) {

                            } else if (menuItem.getItemId() == R.id.edit) {
//                                Intent intent = new Intent(ViewSetActivity.this, EditSetActivity.class);
//                                intent.putExtra("flashcard_id", getIntent().getStringExtra("id"));
//                                startActivity(intent);
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

}
