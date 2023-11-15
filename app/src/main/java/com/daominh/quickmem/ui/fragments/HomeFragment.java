package com.daominh.quickmem.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
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

        // Get user avatar Bitmap using UserDAO
        Bitmap userAvatar = userDAO.getAvatarUser(idUser);

        // Load the user avatar into the ImageView using Picasso
        Picasso.get().load(userAvatar.toString()).into(binding.notificationIv);
    }
}
