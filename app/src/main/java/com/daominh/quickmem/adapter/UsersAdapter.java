package com.daominh.quickmem.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.daominh.quickmem.R;
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
            if (user.getStatus() == 0) {
                holder.binding.userCl.setBackgroundResource(R.color.red);
                holder.binding.userNameTv.setPaintFlags(holder.binding.userNameTv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.binding.emailTv.setPaintFlags(holder.binding.emailTv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.binding.roleTv.setPaintFlags(holder.binding.roleTv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.binding.blockTv.setText("Blocked");
            } else {
                holder.binding.userCl.setBackgroundResource(R.color.white_gray);
                holder.binding.userNameTv.setPaintFlags(holder.binding.userNameTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.binding.emailTv.setPaintFlags(holder.binding.emailTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.binding.roleTv.setPaintFlags(holder.binding.roleTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.binding.blockTv.setText("Block");
            }
            holder.binding.userCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        userDAO.updateStatusUser(user.getId(), 0);
                    } else {
                        userDAO.updateStatusUser(user.getId(), 1);
                    }
                }
            });
            // Load avatar using Picasso
            Glide.with(context)
                    .load(user.getAvatar())
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(holder.binding.avatarIv);
            holder.binding.userNameTv.setText(user.getUsername());
            holder.binding.emailTv.setText("Email: " + user.getEmail());
            if (role == 1) {
                holder.binding.roleTv.setText("Role: Giáo viên");
            } else if (role == 2) {
                holder.binding.roleTv.setText("Role: Học sinh");
            }
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