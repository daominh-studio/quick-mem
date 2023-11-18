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
    private boolean isFlipped = false;

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
        holder.binding.termTv.setText(card.getFront());
        holder.binding.definitionTv.setText(card.getBack());

        holder.binding.cardView.setOnClickListener(v -> {
            if (!isFlipped) {
                // If not flipped, perform a 180-degree rotation animation
                holder.binding.cardView.animate()
                        .rotationXBy(180)
                        .setDuration(350)
                        .withEndAction(() -> {
                            // When the animation is complete, display the back content
                            holder.binding.termTv.setVisibility(View.GONE);
                            holder.binding.definitionTv.setVisibility(View.VISIBLE);
                            holder.binding.definitionTv.setRotationX(-180); // Reverse the rotation of the text
                            isFlipped = true; // Update the flipped state to true
                        })
                        .start();
            } else {
                // If already flipped, perform a rotation animation to return to the original position
                holder.binding.cardView.animate()
                        .rotationXBy(-180)
                        .setDuration(350)
                        .withEndAction(() -> {
                            // When the animation is complete, display the front content
                            holder.binding.termTv.setVisibility(View.VISIBLE);
                            holder.binding.definitionTv.setVisibility(View.GONE);
                            holder.binding.termTv.setRotationX(0); // Reset the rotation of the text
                            isFlipped = false; // Update the flipped state to false
                        })
                        .start();
            }
        });

        holder.binding.termTv.setVisibility(View.VISIBLE);
        holder.binding.definitionTv.setVisibility(View.GONE);
        holder.binding.termTv.setRotationX(0); // Reset the rotation of the text
        holder.binding.definitionTv.setRotationX(-180); // Reverse the rotation of the text
        isFlipped = false;
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