package com.daominh.quickmem.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.databinding.FragmentHomeBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.auth.signup.SignUpActivity;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private UserSharePreferences userSharePreferences;
    private UserDAO userDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get user ID from SharedPreferences
        userSharePreferences = new UserSharePreferences(requireActivity());
        String idUser = userSharePreferences.getId();
        userDAO = new UserDAO(requireActivity());
        String avatar = userDAO.getAvatarUser(idUser);
        Picasso.get().load(avatar).into(binding.notificationIv);
        binding.searchCl.setOnClickListener(v -> {
            userSharePreferences = new UserSharePreferences(requireActivity());
            userSharePreferences.setLogin(false);

            userSharePreferences.clear();
            startActivity(new Intent(requireActivity(), SignUpActivity.class));
            requireActivity().finish();
        });


    }
}
