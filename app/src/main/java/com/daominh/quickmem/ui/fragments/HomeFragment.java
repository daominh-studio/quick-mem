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
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.adapter.ClassAdapter;
import com.daominh.quickmem.adapter.FolderAdapter;
import com.daominh.quickmem.adapter.SetsAdapter;
import com.daominh.quickmem.data.dao.FlashCardDAO;
import com.daominh.quickmem.data.dao.FolderDAO;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.data.model.Group;
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

    private SetsAdapter setsAdapter;
    private FolderAdapter folderAdapter;
    private ClassAdapter classAdapter;
    private ArrayList<FlashCard> flashCards;
    private ArrayList<Folder> folders;
    private ArrayList<Group> classes;
    private FlashCardDAO flashCardDAO;
    private FolderDAO folderDAO;
    private GroupDAO groupDAO;

    private String idUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();
        userDAO = new UserDAO(requireActivity());
        flashCardDAO = new FlashCardDAO(requireActivity());
        folderDAO = new FolderDAO(requireActivity());
        groupDAO = new GroupDAO(requireActivity());
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

        userSharePreferences = new UserSharePreferences(requireActivity());
        idUser = userSharePreferences.getId();

        flashCards = flashCardDAO.getAllFlashCardByUserId(idUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        binding.setsRv.setLayoutManager(linearLayoutManager);
        setsAdapter = new SetsAdapter(requireActivity(), flashCards);
        binding.setsRv.setAdapter(setsAdapter);
        setsAdapter.notifyDataSetChanged();

        folderAdapter = new FolderAdapter(requireActivity(), folders);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
