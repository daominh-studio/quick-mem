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
import com.daominh.quickmem.data.model.FlashCard;
import com.daominh.quickmem.databinding.ItemSetBinding;
import com.daominh.quickmem.preferen.UserSharePreferences;
import com.daominh.quickmem.ui.activities.set.ViewSetActivity;
import com.daominh.quickmem.utils.CARDTable;
import com.daominh.quickmem.utils.Table;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.SetsViewHolder> {

    private final Context context;
    private final ArrayList<FlashCard> sets;
    private final Boolean isLibrary;

    public SetsAdapter(Context context, ArrayList<FlashCard> sets, Boolean isLibrary) {
        this.context = context;
        this.sets = sets;
        this.isLibrary = isLibrary;
    }

    @NonNull
    @NotNull
    @Override
    public SetsAdapter.SetsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemSetBinding binding = ItemSetBinding.inflate(inflater, parent, false);
        return new SetsViewHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull SetsAdapter.SetsViewHolder holder, int position) {
        if (isLibrary) {
            //set weight of card
            ViewGroup.LayoutParams params = holder.binding.setCv.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        }
        FlashCard set = sets.get(position);
        UserSharePreferences userSharePreferences = new UserSharePreferences(context);
//        CardDAO cardDAO = new CardDAO(context);
//        int count = cardDAO.countCardByFlashCardId(set.getId());
        String userNames = userSharePreferences.getUserName();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Table.CARD.toString()).whereEqualTo(CARDTable.FLASHCARD_ID.toString(), set.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = task.getResult().size();
                holder.binding.termCountTv.setText(count + " terms");
            }
        });

        holder.binding.setNameTv.setText(set.getName());
        holder.binding.userNameTv.setText(userNames);
        Picasso.get().load(userSharePreferences.getAvatar()).into(holder.binding.avatarIv);
        holder.binding.createdDateTv.setText(set.getCreated_at());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewSetActivity.class);
            intent.putExtra("id", set.getId());
            intent.putExtra("user_id", set.getUser_id());

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
