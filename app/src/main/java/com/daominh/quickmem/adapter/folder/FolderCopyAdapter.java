package com.daominh.quickmem.adapter.folder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.data.model.Folder;
import com.daominh.quickmem.databinding.ItemFolderBinding;
import com.daominh.quickmem.databinding.ItemFolderCopyBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.folder.ViewFolderActivity;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FolderCopyAdapter extends RecyclerView.Adapter<FolderCopyAdapter.FolderViewHolder>{
    private final Context context;
    private final ArrayList<Folder> folders;

    public FolderCopyAdapter(Context context, ArrayList<Folder> folders) {
        this.context = context;
        this.folders = folders;
    }

    @NonNull
    @NotNull
    @Override
    public FolderCopyAdapter.FolderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemFolderCopyBinding binding = ItemFolderCopyBinding.inflate(inflater, parent, false);
        return new FolderViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FolderCopyAdapter.FolderViewHolder holder, int position) {
        Folder folder = folders.get(position);
        UserSharePreferences userSharePreferences = new UserSharePreferences(context);
        String avatar = userSharePreferences.getAvatar();
        String username = userSharePreferences.getUserName();

        holder.binding.folderNameTv.setText(folder.getName());
        Picasso.get().load(avatar).into(holder.binding.avatarIv);
        holder.binding.userNameTv.setText(username);
        holder.binding.folderCl.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewFolderActivity.class);
            intent.putExtra("id", folder.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        private final ItemFolderCopyBinding binding;

        public FolderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemFolderCopyBinding.bind(itemView);
        }
    }
}
