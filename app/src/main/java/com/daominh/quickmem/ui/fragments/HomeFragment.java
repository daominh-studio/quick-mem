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

import androidx.recyclerview.widget.LinearLayoutManager;
import com.daominh.quickmem.adapter.SetsAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.databinding.FragmentHomeBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.auth.signup.SignUpActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Set;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private UserSharePreferences userSharePreferences;
    private UserDAO userDAO;

    SetsAdapter setsAdapter;
    ArrayList<FlashCard> flashCards;
    FlashCardDAO flashCardDAO;

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

        //get all sets by idUser
        flashCardDAO = new FlashCardDAO(requireActivity());
        flashCards = flashCardDAO.getAllFlashCardByUserId(idUser);


        // Initialize adapter before setting it to RecyclerView
        setsAdapter = new SetsAdapter(requireActivity(), flashCards);

        binding.setsRv.setAdapter(setsAdapter);
        binding.setsRv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.setsRv.setLayoutManager(linearLayoutManager);

        setsAdapter.notifyDataSetChanged();


    }

    @Override
    public void onStart() {
        super.onStart();
        setsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
