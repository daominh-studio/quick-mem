package com.daominh.quickmem.ui.fragments.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import com.daominh.quickmem.databinding.FragmentCreateBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.create.CreateClassActivity;
import com.daominh.quickmem.ui.activities.create.CreateFolderActivity;
import com.daominh.quickmem.ui.activities.create.CreateSetActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.jetbrains.annotations.NotNull;


public class CreateFragment extends BottomSheetDialogFragment {

    private FragmentCreateBinding binding;
    UserSharePreferences userSharePreferences;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the entered transition animation
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
        if (userSharePreferences.getRole() == 2) { // student
            binding.llCreateClass.setVisibility(View.GONE);
        }


        binding.llCreateClass.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), binding.llCreateClass, "transition");
            startActivity(new Intent(requireContext(), CreateClassActivity.class), options.toBundle());
            //call on dismissing to close the bottom sheet
            dismiss();
        });

        binding.llCreateFolder.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), binding.llCreateFolder, "transition");
            startActivity(new Intent(requireContext(), CreateFolderActivity.class), options.toBundle());
            dismiss();
        });

        binding.llCreateSet.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), binding.llCreateSet, "transition");
            startActivity(new Intent(requireContext(), CreateSetActivity.class), options.toBundle());
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
