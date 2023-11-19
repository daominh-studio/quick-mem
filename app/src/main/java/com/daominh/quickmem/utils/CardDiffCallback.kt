package com.daominh.quickmem.utils

import androidx.recyclerview.widget.DiffUtil
import com.daominh.quickmem.data.model.Card

class CardDiffCallback(
    private val oldList: List<Card>,
    private val newList: List<Card>
) : DiffUtil.Callback(
) {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}