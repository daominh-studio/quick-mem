package com.daominh.quickmem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.R
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ItemSelectDefineBinding

class DefineListAdapter(
    private val cardList: List<Card>,
) : RecyclerView.Adapter<DefineListAdapter.DefineViewHolder>() {
    class DefineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSelectDefineBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_define, parent, false)
        return DefineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: DefineViewHolder, position: Int) {
        val card = cardList[position]
        holder.binding.wordTv.text = card.front
        // set other fields as needed
    }
}