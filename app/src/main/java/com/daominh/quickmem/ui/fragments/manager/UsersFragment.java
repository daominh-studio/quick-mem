package com.daominh.quickmem.ui.fragments.manager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.R;
import com.daominh.quickmem.adapter.user.UsersAdapter;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.FragmentUsersBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.auth.signin.SignInActivity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class UsersFragment extends Fragment {
    private FragmentUsersBinding binding;
    private UserDAO userDAO;
    private UsersAdapter usersAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDAO = new UserDAO(requireActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        assert activity != null;
        activity.setSupportActionBar(binding.toolbar);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<User> listUsers = userDAO.getAllUser().stream().filter(user -> user.getRole() != 0).collect(Collectors.toList());
        binding.usersRv.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
        usersAdapter = new UsersAdapter(requireActivity(), listUsers);
        binding.usersRv.setAdapter(usersAdapter);
        usersAdapter.notifyDataSetChanged();


    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_setting_admin, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search users");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String newText) {
                List<User> listUsers = userDAO.getAllUser().stream().filter(user -> user.getRole() != 0).collect(Collectors.toList());
                List<User> listUsersFilter = listUsers.stream().filter(user -> user.getUsername().toLowerCase().contains(newText.toLowerCase())).collect(Collectors.toList());
                usersAdapter = new UsersAdapter(requireActivity(), listUsersFilter);
                binding.usersRv.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Sign out")
                    .setMessage("Are you sure?")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        new UserSharePreferences(requireContext()).clear();
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                        UserSharePreferences userSharePreferences = new UserSharePreferences(requireActivity());
                        userSharePreferences.clear();
                        requireActivity().finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
       return true;
    }
}