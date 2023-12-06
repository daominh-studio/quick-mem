package com.daominh.quickmem.adapter.group

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.adapter.folder.FolderSelectAdapter
import com.daominh.quickmem.data.dao.FolderDAO
import com.daominh.quickmem.data.dao.GroupDAO
import com.daominh.quickmem.data.model.Folder
import com.daominh.quickmem.data.model.Group
import com.daominh.quickmem.databinding.ItemSelectClassBinding

class ClassSelectAdapter(
    private val classList: ArrayList<Group>,
    private val flashCardId: String
) : RecyclerView.Adapter<ClassSelectAdapter.ClassSelectViewHolder>() {
    class ClassSelectViewHolder(
        val binding: ItemSelectClassBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassSelectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectClassBinding.inflate(layoutInflater, parent, false)
        return ClassSelectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassSelectViewHolder, position: Int) {
        val group = classList[position]
        val groupDAO = GroupDAO(holder.itemView.context)
        holder.binding.classNameTv.text = group.name
        updateBackground(holder, group, groupDAO)

        holder.binding.cardView.setOnClickListener {
            if (groupDAO.isFlashCardInClass(group.id, flashCardId)) {
                groupDAO.removeFlashCardFromClass(group.id, flashCardId)
            } else {
                groupDAO.addFlashCardToClass(group.id, flashCardId)
            }
            updateBackground(holder, group, groupDAO)
        }


    }

    override fun getItemCount(): Int {
        return classList.size
    }

    private fun updateBackground(holder: ClassSelectViewHolder, group: Group, groupDAO: GroupDAO) {
        if (groupDAO.isFlashCardInClass(group.id, flashCardId)) {
            holder.binding.cardView.background =
                AppCompatResources.getDrawable(
                    holder.itemView.context,
                    com.daominh.quickmem.R.drawable.background_select
                )
        } else {
            holder.binding.cardView.background =
                AppCompatResources.getDrawable(
                    holder.itemView.context,
                    com.daominh.quickmem.R.drawable.background_unselect
                )
        }
    }
}