package com.daominh.quickmem.adapter.flashcard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.model.FlashCard
import com.daominh.quickmem.databinding.ItemSetFolderBinding
import com.squareup.picasso.Picasso

class SetClassAdapter(
    private val flashCardList: ArrayList<FlashCard>,
    private val classId: String
) : RecyclerView.Adapter<SetClassAdapter.SetClassViewHolder>() {
    class SetClassViewHolder(
        val binding: ItemSetFolderBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetClassViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSetFolderBinding.inflate(layoutInflater, parent, false)
        return SetClassViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return flashCardList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SetClassViewHolder, position: Int) {
        val flashCard = flashCardList[position]
        val cardDAO = CardDAO(holder.itemView.context)
        holder.binding.setNameTv.text = flashCard.name
        holder.binding.termCountTv.text = cardDAO.countCardByFlashCardId(flashCard.id).toString() + " terms"

    }
}