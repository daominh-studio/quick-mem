package com.daominh.quickmem.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.FolderDAO;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.databinding.DialogCreateFolderBinding;
import com.daominh.quickmem.databinding.FragmentCreateBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateClassActivity;
import com.daominh.quickmem.ui.activities.create.CreateFolderActivity;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreateFragment extends BottomSheetDialogFragment {

    private FragmentCreateBinding binding;
    UserSharePreferences userSharePreferences;
    FolderDAO folderDAO;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the enter transition animation
        setEnterTransition(new CustomEnterTransition().setDuration(500));
        // Set the exit transition animation
        setExitTransition(new CustomExitTransition().setDuration(500));
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userSharePreferences = new UserSharePreferences(requireActivity());

        binding.llCreateClass.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Hello class", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), CreateClassActivity.class));
            requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
            //call ondismiss to close the bottom sheet
            dismiss();
        });

        binding.llCreateFolder.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            final DialogCreateFolderBinding dialogBinding = DialogCreateFolderBinding.inflate(getLayoutInflater());
            builder.setView(dialogBinding.getRoot());
            AlertDialog dialog = builder.create();
            dialogBinding.folderEt.requestFocus();
            dialogBinding.cancelTv.setOnClickListener(v1 -> {
                dialog.dismiss();
                dismiss();
            });
            dialogBinding.okTv.setOnClickListener(v1 -> {
                final String folderName = dialogBinding.folderEt.getText().toString().trim();
                final String folderDescription = dialogBinding.descriptionEt.getText().toString().trim();

                if (folderName.isEmpty()) {
                    dialogBinding.folderTil.setError("");
                    dialogBinding.folderTil.setHelperText("Folder name cannot be empty");
                    dialogBinding.folderEt.requestFocus();
                    return;
                } else {
                    final String folderId = genUUID();
                    final String userId = getUser_id();
                    final String createdAt = getCurrentDate();
                    final String updatedAt = getCurrentDate();

                    Folder folder = new Folder(folderId, folderName, folderDescription, userId, createdAt, updatedAt);
                    folderDAO = new FolderDAO(requireContext());
                    if (folderDAO.insertFolder(folder) > 0) {
                        Toast.makeText(requireContext(), "Folder created", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        dismiss();
                    } else {
                        Toast.makeText(requireContext(), "Folder not created", Toast.LENGTH_SHORT).show();
                    }

                }

            });
            dialog.show();
        });

        binding.llCreateSet.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Hello set", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), CreateFolderActivity.class));
            requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
            dismiss();
        });
    }

    // Define your custom enter transition class
    private static class CustomEnterTransition extends androidx.transition.Slide {
        public CustomEnterTransition() {
            setSlideEdge(android.view.Gravity.BOTTOM);
        }
    }

    // Define your custom exit transition class
    private static class CustomExitTransition extends androidx.transition.Slide {
        public CustomExitTransition() {
            setSlideEdge(android.view.Gravity.BOTTOM);
        }
    }

    private String getCurrentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return currentDate.format(formatter);
        } else {
            // Handle case for Android versions less than Oreo
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

        return userSharePreferences.getId();
    }

}
