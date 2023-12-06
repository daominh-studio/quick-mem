package com.daominh.quickmem.adapter.flashcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daominh.quickmem.data.dao.CardDAO;
import com.daominh.quickmem.data.dao.UserDAO;
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.data.model.User;
import com.daominh.quickmem.databinding.ItemSetAllBinding;
import com.daominh.quickmem.ui.activities.set.ViewSetActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SetAllAdapter extends RecyclerView.Adapter<SetAllAdapter.SetsViewHolder> {

    private final Context context;
    private final ArrayList<FlashCard> sets;

    public SetAllAdapter(Context context, ArrayList<FlashCard> sets) {
        this.context = context;
        this.sets = sets;
    }

    @NonNull
    @NotNull
    @Override
    public SetAllAdapter.SetsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemSetAllBinding binding = ItemSetAllBinding.inflate(inflater, parent, false);
        return new SetsViewHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull SetAllAdapter.SetsViewHolder holder, int position) {

        FlashCard set = sets.get(position);
        CardDAO cardDAO = new CardDAO(context);
        int count = cardDAO.countCardByFlashCardId(set.getId());
        UserDAO userDAO = new UserDAO(context);
        User user = userDAO.getUserById(set.getUser_id());

        holder.binding.setNameTv.setText(set.getName());
        holder.binding.termCountTv.setText(count + " terms");
        holder.binding.userNameTv.setText(user.getUsername());
        Picasso.get().load(user.getAvatar()).into(holder.binding.avatarIv);

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
        private final ItemSetAllBinding binding;

        public SetsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemSetAllBinding.bind(itemView);
        }
    }

}
