package com.daominh.quickmem.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daominh.quickmem.R;
import com.daominh.quickmem.data.dao.CardDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.databinding.ItemSetBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.set.ViewSetActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.SetsViewHolder> {

    UserSharePreferences userSharePreferences;
    private final Context context;
    private final ArrayList<FlashCard> sets;
    CardDAO cardDAO;

    public SetsAdapter(Context context, ArrayList<FlashCard> sets) {
        this.context = context;
        this.sets = sets;
    }

    @NonNull
    @NotNull
    @Override
    public SetsAdapter.SetsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemSetBinding binding = ItemSetBinding.inflate(inflater, parent, false);
        return new SetsViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SetsAdapter.SetsViewHolder holder, int position) {
        FlashCard set = sets.get(position);
        userSharePreferences = new UserSharePreferences(context);
        cardDAO = new CardDAO(context);
        int count = cardDAO.countCardByFlashCardId(set.getId());
        String avatar = userSharePreferences.getAvatar();
        String userNames = userSharePreferences.getUserName();

        holder.binding.setNameTv.setText(set.getName());
        holder.binding.termCountTv.setText(count + " terms");
        holder.binding.userNameTv.setText(userNames);
        Picasso.get().load(avatar).into(holder.binding.avatarIv);
        holder.binding.createdDateTv.setText(set.getCreated_at());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewSetActivity.class);
            intent.putExtra("id", set.getId());

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class SetsViewHolder extends RecyclerView.ViewHolder {
        private final ItemSetBinding binding;

        public SetsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemSetBinding.bind(itemView);
        }
    }

}
