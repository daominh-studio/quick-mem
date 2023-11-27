package com.daominh.quickmem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.data.model.Card;
import com.daominh.quickmem.databinding.ItemCardAddBinding;
import com.daominh.quickmem.utils.OnItemClickListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    Context context;
    ArrayList<Card> cards;

    private OnItemClickListener onItemClickListener;

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

        if (position > 1) {
            holder.binding.termEt.requestFocus();
        }

        holder.binding.termEt.setText(card.getFront());
        holder.binding.definitionEt.setText(card.getBack());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
        holder.binding.termEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });

        holder.binding.definitionEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });

        holder.binding.termEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                card.setFront(s.toString());
                card.setBack(holder.binding.definitionEt.getText().toString());
                if (position < cards.size()) {
                    cards.set(position, card);
                } else {
                    Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                card.setFront(s.toString());
                card.setBack(holder.binding.definitionEt.getText().toString());

                if (position < cards.size()) {
                    cards.set(position, card);
                } else {
                    Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                card.setFront(s.toString());
                card.setBack(holder.binding.definitionEt.getText().toString());

                if (position < cards.size()) {
                    cards.set(position, card);
                } else {
                    Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.binding.definitionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                card.setFront(holder.binding.termEt.getText().toString());
                card.setBack(s.toString());

                if (position < cards.size()) {
                    cards.set(position, card);
                } else {
                    Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                card.setFront(holder.binding.termEt.getText().toString());
                card.setBack(s.toString());

                if (position < cards.size()) {
                    cards.set(position, card);
                } else {
                    Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                card.setFront(holder.binding.termEt.getText().toString());
                card.setBack(s.toString());

                if (position < cards.size()) {
                    cards.set(position, card);
                } else {
                    Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private final ItemCardAddBinding binding;

        public CardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.binding = ItemCardAddBinding.bind(itemView);
        }
    }


}
