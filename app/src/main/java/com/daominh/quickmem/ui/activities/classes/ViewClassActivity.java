package com.daominh.quickmem.ui.activities.classes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daominh.quickmem.ui.activities.set.AddFlashCardToClassActivity;
import com.daominh.quickmem.ui.activities.group.AddMemberActivity;
import com.daominh.quickmem.ui.activities.group.EditClassActivity;
import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.group.MyViewClassAdapter;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.databinding.ActivityViewClassBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.google.android.material.tabs.TabLayout;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ViewClassActivity extends AppCompatActivity {
    private ActivityViewClassBinding binding;
    private GroupDAO groupDAO;
    int currentTabPosition = 0;
    private String id;
    private UserSharePreferences userSharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewClassBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        MyViewClassAdapter myViewClassAdapter = new MyViewClassAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(myViewClassAdapter);

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            myViewClassAdapter.notifyDataSetChanged();
        });

    }

    @SuppressLint("SetTextI18n")
    private void setUpData() {
        id = getIntent().getStringExtra("id");
        groupDAO = new GroupDAO(this);
        Group group = groupDAO.getGroupById(id);
        binding.classNameTv.setText(group.getName());
        binding.termCountTv.setText(groupDAO.getNumberFlashCardInClass(id) + " sets");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_set, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            new BottomSheetMenuDialogFragment.Builder(this)
                    .setSheet(R.menu.menu_view_class)
                    .setTitle("Class")
                    .setListener(new BottomSheetListener() {
                        @Override
                        public void onSheetShown(@NotNull BottomSheetMenuDialogFragment bottomSheetMenuDialogFragment, @Nullable Object o) {

                        }

                        @Override
                        public void onSheetItemSelected(@NotNull BottomSheetMenuDialogFragment bottomSheetMenuDialogFragment, @NotNull MenuItem menuItem, @Nullable Object o) {
                            if (menuItem.getItemId() == R.id.add_member) {
                                if (isOwner()) {
                                    holderAddMember();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .setStyle(Styles.FAILED)
                                            .setHeading("Error!")
                                            .setDescription("You are not the owner of this class!")
                                            .setPositiveButtonText("OK")
                                            .setCancelable(true)
                                            .showDialog(new OnDialogButtonClickListener() {
                                                @Override
                                                public void onDismissClicked(Dialog dialog) {
                                                    super.onDismissClicked(dialog);
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            } else if (menuItem.getItemId() == R.id.add_sets) {
                                if (isOwner()) {
                                    handleAddSets();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .setStyle(Styles.FAILED)
                                            .setHeading("Error!")
                                            .setDescription("You are not the owner of this class!")
                                            .setPositiveButtonText("OK")
                                            .setCancelable(true)
                                            .showDialog(new OnDialogButtonClickListener() {
                                                @Override
                                                public void onDismissClicked(Dialog dialog) {
                                                    super.onDismissClicked(dialog);
                                                    dialog.dismiss();
                                                }
                                            });
                                }

                            } else if (menuItem.getItemId() == R.id.edit_class) {
                                if (isOwner()) {
                                    handleEditClass();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .setStyle(Styles.FAILED)
                                            .setHeading("Error!")
                                            .setDescription("You are not the owner of this class!")
                                            .setPositiveButtonText("OK")
                                            .setCancelable(true)
                                            .showDialog(new OnDialogButtonClickListener() {
                                                @Override
                                                public void onDismissClicked(Dialog dialog) {
                                                    super.onDismissClicked(dialog);
                                                    dialog.dismiss();
                                                }
                                            });
                                }

                            } else if (menuItem.getItemId() == R.id.delete_class) {
                                if (isOwner()) {
                                    handleDeleteClass();
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .setStyle(Styles.FAILED)
                                            .setHeading("Error!")
                                            .setDescription("You are not the owner of this class!")
                                            .setPositiveButtonText("OK")
                                            .setCancelable(true)
                                            .showDialog(new OnDialogButtonClickListener() {
                                                @Override
                                                public void onDismissClicked(Dialog dialog) {
                                                    super.onDismissClicked(dialog);
                                                    dialog.dismiss();
                                                }
                                            });
                                }

                            } else if (menuItem.getItemId() == R.id.leave_class) {
                                if (!isOwner()) {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .setStyle(Styles.STANDARD)
                                            .setHeading("Are you sure?")
                                            .setDescription("You will loss access to this class!")
                                            .setPositiveButtonText("Yes")
                                            .setPopupDialogIcon(R.drawable.baseline_logout_24)
                                            .setNegativeButtonText("Cancel")
                                            .setCancelable(true)
                                            .showDialog(new OnDialogButtonClickListener() {
                                                @Override
                                                public void onPositiveClicked(Dialog dialog) {
                                                    super.onPositiveClicked(dialog);
                                                    UserDAO userDAO = new UserDAO(ViewClassActivity.this);
                                                    if (userDAO.removeUserFromClass(userSharePreference.getId(), userSharePreference.getClassId()) > 0L) {
                                                        PopupDialog.getInstance(ViewClassActivity.this)
                                                                .setStyle(Styles.SUCCESS)
                                                                .setHeading("Leave!")
                                                                .setDescription("Your class has been leave!.")
                                                                .setDismissButtonText("OK")
                                                                .setCancelable(true)
                                                                .showDialog(new OnDialogButtonClickListener() {
                                                                    @Override
                                                                    public void onDismissClicked(Dialog dialog) {
                                                                        super.onDismissClicked(dialog);
                                                                        dialog.dismiss();
                                                                        finish();
                                                                    }
                                                                });
                                                    } else {
                                                        PopupDialog.getInstance(ViewClassActivity.this)
                                                                .setStyle(Styles.FAILED)
                                                                .setHeading("Error!")
                                                                .setDescription("Something went wrong!")
                                                                .setPositiveButtonText("OK")
                                                                .setCancelable(true)
                                                                .showDialog(new OnDialogButtonClickListener() {
                                                                    @Override
                                                                    public void onDismissClicked(Dialog dialog) {
                                                                        super.onDismissClicked(dialog);
                                                                        dialog.dismiss();
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
                                } else {
                                    PopupDialog.getInstance(ViewClassActivity.this)
                                            .setStyle(Styles.FAILED)
                                            .setHeading("Error!")
                                            .setDescription("You are the owner of this class!")
                                            .setPositiveButtonText("OK")
                                            .setCancelable(true)
                                            .showDialog(new OnDialogButtonClickListener() {
                                                @Override
                                                public void onDismissClicked(Dialog dialog) {
                                                    super.onDismissClicked(dialog);
                                                    dialog.dismiss();
                                                }
                                            });
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleAddSets() {
        Intent intent = new Intent(this, AddFlashCardToClassActivity.class);
        intent.putExtra("flashcard_id", id);
        startActivity(intent);
        finish();
    }

    private void handleEditClass() {
        Intent intent = new Intent(this, EditClassActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void handleDeleteClass() {
        PopupDialog.getInstance(this)
                .setStyle(Styles.STANDARD)
                .setHeading("Are you sure?")
                .setDescription("You will not be able to recover this class!")
                .setPositiveButtonText("Yes")
                .setPopupDialogIcon(R.drawable.ic_delete)
                .setNegativeButtonText("Cancel")
                .setCancelable(true)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                        if (groupDAO.deleteClass(id) > 0L) {
                            PopupDialog.getInstance(ViewClassActivity.this)
                                    .setStyle(Styles.SUCCESS)
                                    .setHeading("Deleted!")
                                    .setDescription("Your class has been deleted.")
                                    .setDismissButtonText("OK")
                                    .setCancelable(true)
                                    .showDialog(new OnDialogButtonClickListener() {
                                        @Override
                                        public void onDismissClicked(Dialog dialog) {
                                            super.onDismissClicked(dialog);
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                        } else {
                            PopupDialog.getInstance(ViewClassActivity.this)
                                    .setStyle(Styles.FAILED)
                                    .setHeading("Error!")
                                    .setDescription("Something went wrong!")
                                    .setPositiveButtonText("OK")
                                    .setCancelable(true)
                                    .showDialog(new OnDialogButtonClickListener() {
                                        @Override
                                        public void onDismissClicked(Dialog dialog) {
                                            super.onDismissClicked(dialog);
                                            dialog.dismiss();
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
    }

    private void holderAddMember() {
        Intent intent = new Intent(this, AddMemberActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

    private boolean isOwner() {
        userSharePreference = new UserSharePreferences(this);
        String currentUserId = userSharePreference.getId();
        String ownerId = groupDAO.getGroupById(id).getUser_id();
        return currentUserId.equals(ownerId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
    }
}