package com.daominh.quickmem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.ItemFolderBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    UserSharePreferences userSharePreferences;
    private final Context context;
    private final ArrayList<Folder> folders;

    public FolderAdapter(Context context, ArrayList<Folder> folders) {
        this.context = context;
        this.folders = folders;
    }

    @NonNull
    @NotNull
    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemFolderBinding binding = ItemFolderBinding.inflate(inflater, parent, false);
        return new FolderViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FolderAdapter.FolderViewHolder holder, int position) {
        Folder folder = folders.get(position);
        userSharePreferences = new UserSharePreferences(context);
        String avatar = userSharePreferences.getAvatar();
        String username = userSharePreferences.getUserName();

        holder.binding.folderNameTv.setText(folder.getName());
        Picasso.get().load(avatar).into(holder.binding.avatarIv);
        holder.binding.userNameTv.setText(username);
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        private final ItemFolderBinding binding;

        public FolderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemFolderBinding.bind(itemView);
        }
    }
}
