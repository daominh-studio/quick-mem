package com.daominh.quickmem.adapter.card;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daominh.quickmem.data.model.Card;
import com.daominh.quickmem.databinding.ItemViewSetBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewSetAdapter extends RecyclerView.Adapter<ViewSetAdapter.ViewSetViewHolder> {
    private final Context context;
    private final ArrayList<Card> cards;
    private TextToSpeech textToSpeech;

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
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }

        });
        holder.binding.soundIv.setOnClickListener(v -> {
            if (!holder.binding.backTv.getText().toString().isEmpty()){
                textToSpeech = new TextToSpeech(context, status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = textToSpeech.setLanguage(textToSpeech.getVoice().getLocale());
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Toast.makeText(context, "Language not supported", Toast.LENGTH_SHORT).show();
                        } else {
                            Bundle params = new Bundle();
                            params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f);
                            textToSpeech.speak(holder.binding.backTv.getText().toString(), TextToSpeech.QUEUE_FLUSH, params, "UniqueID");
                        }
                    } else {
                        Toast.makeText(context, "Initialization failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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

    @Override
    public void onDetachedFromRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}