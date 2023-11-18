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
    private final ArrayList<Boolean> flippedStates;
    private boolean isAnimationRunning;

    public ViewSetAdapter(Context context, ArrayList<Card> cards) {
        this.context = context;
        this.cards = cards;
        this.flippedStates = new ArrayList<>(cards.size());
        for (int i = 0; i < cards.size(); i++) {
            flippedStates.add(false);
        }
        this.isAnimationRunning = false;
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
        holder.binding.termTv.setText(card.getFront());
        holder.binding.definitionTv.setText(card.getBack());

        boolean isFlipped = flippedStates.get(position);

        if (isFlipped) {
            holder.binding.termTv.setVisibility(View.GONE);
            holder.binding.definitionTv.setVisibility(View.VISIBLE);
            holder.binding.definitionTv.setRotationX(-180);
        } else {
            holder.binding.termTv.setVisibility(View.VISIBLE);
            holder.binding.definitionTv.setVisibility(View.GONE);
            holder.binding.termTv.setRotationX(0);
        }

        holder.binding.cardView.setOnClickListener(v -> {
            if (!isAnimationRunning) { // Check if animation is not running
                boolean currentFlippedState = flippedStates.get(position);

                if (!currentFlippedState) {
                    isAnimationRunning = true; // Set animation state as true
                    holder.binding.cardView.animate()
                            .rotationXBy(180)
                            .setDuration(350)
                            .withEndAction(() -> {
                                flippedStates.set(position, true);
                                holder.binding.termTv.setVisibility(View.GONE);
                                holder.binding.definitionTv.setVisibility(View.VISIBLE);
                                holder.binding.definitionTv.setRotationX(-180);
                                isAnimationRunning = false; // Reset animation state to false
                            })
                            .start();
                } else {
                    isAnimationRunning = true; // Set animation state as true
                    holder.binding.cardView.animate()
                            .rotationXBy(-180)
                            .setDuration(350)
                            .withEndAction(() -> {
                                flippedStates.set(position, false);
                                holder.binding.termTv.setVisibility(View.VISIBLE);
                                holder.binding.definitionTv.setVisibility(View.GONE);
                                holder.binding.termTv.setRotationX(0);
                                isAnimationRunning = false; // Reset animation state to false
                            })
                            .start();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewSetViewHolder extends RecyclerView.ViewHolder {
        private final ItemViewSetBinding binding;

        public ViewSetViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemViewSetBinding.bind(itemView);
        }
    }
}