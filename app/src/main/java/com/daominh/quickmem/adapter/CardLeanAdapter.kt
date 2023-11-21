package com.daominh.quickmem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ItemLearnSetBinding

class CardLeanAdapter(
    private var cardList: List<Card>
) : RecyclerView.Adapter<CardLeanAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardLeanAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLearnSetBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardLeanAdapter.ViewHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun getCount(): Int {
        return cardList.size
    }

    fun setCards(cards: List<Card>) {
        this.cardList = cards
    }

    fun getCards(): List<Card> {
        return cardList
    }

    class ViewHolder(private val binding: ItemLearnSetBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(card: Card) {
            binding.itemFont.text = card.front
            binding.itemBack.text = card.back
        }

    }
}