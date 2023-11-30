package com.daominh.quickmem.adapter.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.data.model.Card;
import com.daominh.quickmem.databinding.ItemCardAddBinding;
import com.daominh.quickmem.utils.OnItemClickListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.BiConsumer;

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

        holder.binding.termEt.addTextChangedListener(createTextWatcher(card, holder.binding.termEt, Card::setFront));
        holder.binding.definitionEt.addTextChangedListener(createTextWatcher(card, holder.binding.definitionEt, Card::setBack));
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
    private TextWatcher createTextWatcher(Card card, EditText editText, BiConsumer<Card, String> setter) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                updateCard(card, editText, setter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCard(card, editText, setter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCard(card, editText, setter);
            }
        };
    }

    private void updateCard(Card card, EditText editText, BiConsumer<Card, String> setter) {
        setter.accept(card, editText.getText().toString());
        int position = cards.indexOf(card);
        if (position != -1) {
            cards.set(position, card);
        } else {
            Toast.makeText(context, "wrong", Toast.LENGTH_SHORT).show();
        }
    }


}
