package com.daominh.quickmem.adapter.folder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.dao.FolderDAO
import com.daominh.quickmem.data.model.Folder
import com.daominh.quickmem.databinding.ItemFolderSelectBinding

class FolderSelectAdapter(
    private val folderList: ArrayList<Folder>,
    private val flashcardId: String
) : RecyclerView.Adapter<FolderSelectAdapter.FolderSelectViewHolder>() {
    class FolderSelectViewHolder(
        val binding: ItemFolderSelectBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderSelectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFolderSelectBinding.inflate(layoutInflater, parent, false)
        return FolderSelectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderSelectViewHolder, position: Int) {
        val folder = folderList[position]
        val folderDAO = FolderDAO(holder.itemView.context)
        holder.binding.folderNameTv.text = folder.name
        updateBackground(holder, folder, folderDAO)
        holder.binding.folderCv.setOnClickListener {
            if (folderDAO.isFlashCardInFolder(folder.id, flashcardId)) {
                folderDAO.removeFlashCardFromFolder(folder.id, flashcardId)
                Toast.makeText(
                    holder.itemView.context,
                    "Removed from ${folder.name}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                folderDAO.addFlashCardToFolder(folder.id, flashcardId)
                Toast.makeText(
                    holder.itemView.context,
                    "Added to ${folder.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            updateBackground(holder, folder, folderDAO)
        }
    }

    private fun updateBackground(holder: FolderSelectViewHolder, folder: Folder, folderDAO: FolderDAO) {
        if (folderDAO.isFlashCardInFolder(folder.id, flashcardId)) {
            holder.binding.folderCv.background =
                AppCompatResources.getDrawable(
                    holder.itemView.context,
                    com.daominh.quickmem.R.drawable.background_select
                )
        } else {
            holder.binding.folderCv.background =
                AppCompatResources.getDrawable(
                    holder.itemView.context,
                    com.daominh.quickmem.R.drawable.background_unselect
                )
        }
    }

    override fun getItemCount(): Int {
        return folderList.size
    }
}