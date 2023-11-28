package com.daominh.quickmem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.dao.UserDAO
import com.daominh.quickmem.data.model.FlashCard
import com.daominh.quickmem.data.model.User
import com.daominh.quickmem.databinding.ItemSetFolderBinding
import com.squareup.picasso.Picasso

class SetFolderViewAdapter(
    private val flashcardList: ArrayList<FlashCard>,
    private val onDoneClickListener: OnDoneClickListener
) : RecyclerView.Adapter<SetFolderViewAdapter.SetFolderViewHolder>() {
    private val selectedItems = ArrayList<FlashCard>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetFolderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSetFolderBinding.inflate(layoutInflater, parent, false)
        return SetFolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetFolderViewAdapter.SetFolderViewHolder, position: Int) {
        val flashcard = flashcardList[position]
        val userDAO = UserDAO(holder.itemView.context)
        val user = userDAO.getUserById(flashcard.user_id)
        val cardDAO = CardDAO(holder.itemView.context)
        val count = cardDAO.countCardByFlashCardId(flashcard.id)

        Picasso.get().load(user.avatar).into(holder.binding.avatarIv)
        holder.binding.userNameTv.text = user.name
        holder.binding.setNameTv.text = flashcard.name
        holder.binding.termCountTv.text = count.toString()
        holder.binding.setFolderItem.setOnClickListener {
            if (selectedItems.contains(flashcard)) {
                selectedItems.remove(flashcard)
                holder.binding.setFolderItem.background =
                    holder.binding.setFolderItem.context.getDrawable(com.daominh.quickmem.R.drawable.background_unselect)
            } else {
                selectedItems.add(flashcard)
                holder.binding.setFolderItem.background =
                    holder.binding.setFolderItem.context.getDrawable(com.daominh.quickmem.R.drawable.background_select)
            }
        }
    }


    fun getItemSelect(): ArrayList<FlashCard> {
        return selectedItems
    }

    override fun getItemCount(): Int {
        return flashcardList.size
    }

    class SetFolderViewHolder(val binding: ItemSetFolderBinding) : RecyclerView.ViewHolder(binding.root) {


    }
    interface OnDoneClickListener {
        fun onDoneClick(): ArrayList<FlashCard>
    }
}