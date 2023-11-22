package com.daominh.quickmem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daominh.quickmem.data.model.Card;
import com.daominh.quickmem.databinding.ItemViewSetBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewSetAdapter extends RecyclerView.Adapter<ViewSetAdapter.ViewSetViewHolder> {
    private final Context context;
    private final ArrayList<Card> cards;

    public ViewSetAdapter(Context context, ArrayList<Card> cards) {
        this.context = context;
        this.cards = cards;
    }

    @NonNull
    @NotNull
    @Override
    public ViewSetAdapter.ViewSetViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemViewSetBinding binding = ItemViewSetBinding.inflate(inflater, parent, false);
        return new ViewSetViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewSetAdapter.ViewSetViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.binding.backTv.setText(card.getFront());
        holder.binding.frontTv.setText(card.getBack());
        holder.binding.cardViewFlip.setFlipDuration(450);
        holder.binding.cardViewFlip.setFlipEnabled(true);
        holder.binding.cardViewFlip.setOnClickListener(v -> {
            holder.binding.cardViewFlip.flipTheView();
        });

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewSetViewHolder extends RecyclerView.ViewHolder {
        private final ItemViewSetBinding binding;

        public ViewSetViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemViewSetBinding.bind(itemView);
        }
    }

}