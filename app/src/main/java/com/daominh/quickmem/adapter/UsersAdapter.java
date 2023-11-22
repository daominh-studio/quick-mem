package com.daominh.quickmem.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.ItemUsersAdminBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private final Context context;
    private final List<User> users;
    private UserDAO userDAO;

    public UsersAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.userDAO = new UserDAO(context);
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemUsersAdminBinding binding = ItemUsersAdminBinding.inflate(inflater, parent, false);
        return new UsersViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);
        int role = user.getRole();
        if (role != 0) {
            holder.binding.getRoot().setVisibility(View.VISIBLE);
            holder.binding.userCb.setChecked(user.getStatus() == 0);
            holder.binding.userCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked){
                        userDAO.updateStatusUser(user.getId(),0);
                    } else {
                        userDAO.updateStatusUser(user.getId(),1);
                    }
                }
            });
            // Load avatar using Picasso
            Picasso.get().load(user.getAvatar()).into(holder.binding.avatarIv);

            holder.binding.userNameTv.setText(user.getUsername());
            Toast.makeText(context, "hi" + user.getUsername(), Toast.LENGTH_SHORT).show();
            holder.binding.emailTv.setText("Email: "+user.getEmail());
            if (role == 1) {
                holder.binding.roleTv.setText("Role: Giáo viên");
            } else if (role == 2) {
                holder.binding.roleTv.setText("Role: Học sinh");
            }
            Toast.makeText(context, "role" + user.getRole(), Toast.LENGTH_SHORT).show();
        } else {
            holder.binding.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (User user : users) {
            if (user.getRole() != 0) {
                count++;
            }
        }
        return count;
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        private final ItemUsersAdminBinding binding;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemUsersAdminBinding.bind(itemView);
        }
    }
}