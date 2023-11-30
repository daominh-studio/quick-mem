package com.daominh.quickmem.adapter.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.ItemUsersAdminBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private final Context context;
    private final List<User> users;
    private final UserDAO userDAO;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);
        Picasso.get().load(user.getAvatar()).placeholder(R.drawable.ic_user).into(holder.binding.avatarIv);
        int role = user.getRole();
        if (role != 0) {
            holder.binding.getRoot().setVisibility(View.VISIBLE);
            holder.binding.userCl.setBackgroundResource(user.getStatus() == 0 ? R.color.gray : R.color.white_gray);
            holder.binding.blockTv.setText(user.getStatus() == 0 ? "Blocked" : "Block");
            holder.binding.userCb.setChecked(user.getStatus() == 0);
            int paintFlags = holder.binding.emailTv.getPaintFlags();
            paintFlags = user.getStatus() == 0 ? (paintFlags | Paint.STRIKE_THRU_TEXT_FLAG) : (paintFlags & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.binding.emailTv.setPaintFlags(paintFlags);
            holder.binding.userNameTv.setPaintFlags(paintFlags);
            holder.binding.roleTv.setPaintFlags(paintFlags);

            int finalPaintFlags = paintFlags;
            holder.binding.userCb.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                userDAO.updateStatusUser(user.getId(), isChecked ? 0 : 1);
                holder.binding.userCl.setBackgroundResource(isChecked ? R.color.gray : R.color.white_gray);
                holder.binding.blockTv.setText(isChecked ? "Blocked" : "Block");
                int flags = isChecked ? (finalPaintFlags | Paint.STRIKE_THRU_TEXT_FLAG) : (finalPaintFlags & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.binding.emailTv.setPaintFlags(flags);
                holder.binding.userNameTv.setPaintFlags(flags);
                holder.binding.roleTv.setPaintFlags(flags);
            });

            holder.binding.userNameTv.setText(user.getUsername());
            holder.binding.emailTv.setText("Email: " + user.getEmail());
            holder.binding.roleTv.setText("Role: " + (role == 1 ? "Teacher" : "Student"));
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