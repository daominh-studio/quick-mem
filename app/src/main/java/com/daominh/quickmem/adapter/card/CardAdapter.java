package com.daominh.quickmem.adapter.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.data.model.Card;
import com.daominh.quickmem.databinding.ItemCardAddBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private final Context context;
    private final ArrayList<Card> cards;

    public CardAdapter(Context context, ArrayList<Card> cards) {
        this.context = context;
        this.cards = cards;
    }

    @NonNull
    @NotNull
    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCardAddBinding binding = ItemCardAddBinding.inflate(inflater, parent, false);
        return new CardViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardAdapter.CardViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Card card = cards.get(position);
        holder.removeTextWatchers();

        if (position > 1) {
            holder.binding.termEt.requestFocus();
        }

        holder.binding.termEt.setText(card.getFront());
        holder.binding.definitionEt.setText(card.getBack());


        TextWatcher frontWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                card.setFront(s.toString().trim());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                card.setFront(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                card.setFront(s.toString().trim());
            }
        };

        TextWatcher backWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                card.setBack(s.toString().trim());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                card.setBack(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                card.setBack(s.toString().trim());
            }
        };

        holder.setTextWatchers(frontWatcher, backWatcher);

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private final ItemCardAddBinding binding;
        private TextWatcher frontWatcher;
        private TextWatcher backWatcher;

        public CardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.binding = ItemCardAddBinding.bind(itemView);
        }

        public void removeTextWatchers() {
            if (frontWatcher != null) {
                binding.termEt.removeTextChangedListener(frontWatcher);
            }
            if (backWatcher != null) {
                binding.definitionEt.removeTextChangedListener(backWatcher);
            }
        }

        public void setTextWatchers(TextWatcher frontWatcher, TextWatcher backWatcher) {
            this.frontWatcher = frontWatcher;
            this.backWatcher = backWatcher;
            binding.termEt.addTextChangedListener(frontWatcher);
            binding.definitionEt.addTextChangedListener(backWatcher);
        }
    }
}
