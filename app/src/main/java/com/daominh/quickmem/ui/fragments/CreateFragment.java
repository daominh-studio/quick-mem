package com.daominh.quickmem.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.daominh.quickmem.R;
import com.daominh.quickmem.databinding.FragmentCreateBinding;
import com.daominh.quickmem.ui.activities.create.CreateClassActivity;
import com.daominh.quickmem.ui.activities.create.CreateFolderActivity;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.jetbrains.annotations.NotNull;

public class CreateFragment extends BottomSheetDialogFragment {

    private FragmentCreateBinding binding;


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

        binding.llCreateClass.setOnClickListener(v ->{
            Toast.makeText(requireContext(), "Hello class", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), CreateClassActivity.class));
            requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
            //call ondismiss to close the bottom sheet
            dismiss();
        });

        binding.llCreateFolder.setOnClickListener(v ->{
            Toast.makeText(requireContext(), "Hello folder", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), CreateFolderActivity.class));
            requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
            dismiss();
        });

        binding.llCreateSet.setOnClickListener(v ->{
            Toast.makeText(requireContext(), "Hello set", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), CreateSetActivity.class));
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

}
