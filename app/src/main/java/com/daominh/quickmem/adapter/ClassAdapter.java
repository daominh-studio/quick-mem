package com.daominh.quickmem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.daominh.quickmem.data.dao.GroupDAO;
import com.daominh.quickmem.data.model.Group;
import com.daominh.quickmem.databinding.ItemClassBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    GroupDAO groupDAO;
    private final Context context;
    private final ArrayList<Group> classes;

    public ClassAdapter(Context context, ArrayList<Group> classes) {
        this.context = context;
        this.classes = classes;
    }

    @NonNull
    @NotNull
    @Override
    public ClassAdapter.ClassViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemClassBinding binding = ItemClassBinding.inflate(inflater, parent, false);
        return new ClassViewHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ClassAdapter.ClassViewHolder holder, int position) {
        Group group = classes.get(position);
        holder.binding.classNameTv.setText(group.getName());
        groupDAO = new GroupDAO(context);
        int numberMember = groupDAO.getNumberMemberInClass(group.getId()) + 1;
        int numberSet = groupDAO.getNumberFlashCardInClass(group.getId());
        holder.binding.numberUserTv.setText(numberMember + " members");
        holder.binding.numberSetTv.setText(numberSet + " sets");

    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        private final ItemClassBinding binding;

        public ClassViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemClassBinding.bind(itemView);
        }
    }
}
