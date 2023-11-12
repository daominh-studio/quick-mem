package com.daominh.quickmem.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.daominh.quickmem.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateFragment extends BottomSheetDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the enter transition animation
        setEnterTransition(new CustomEnterTransition().setDuration(500));
        // Set the exit transition animation
        setExitTransition(new CustomExitTransition().setDuration(500));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false);
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
