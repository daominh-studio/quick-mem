package com.daominh.quickmem.adapter.card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ItemLearnSetBinding

class CardLeanAdapter(
    private var cardList: List<Card>
) : RecyclerView.Adapter<CardLeanAdapter.ViewHolder>() {
    private var flippedStates = MutableList(cardList.size) { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLearnSetBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun setCards(cards: List<Card>) {
        this.cardList = cards
        flippedStates = MutableList(cards.size) { false }
    }

    fun getCards(): List<Card> {
        return cardList
    }

    fun getCount(): Int {
        return cardList.size
    }

    inner class ViewHolder(private val binding: ItemLearnSetBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(card: Card) {
            binding.backTv.text = card.front
            binding.frontTv.text = card.back
            binding.cardViewFlip.setFlipTypeFromRight()
            binding.cardViewFlip.setFlipDuration(500)
            binding.cardViewFlip.setToHorizontalType()
            binding.cardViewFlip.setOnClickListener {
                binding.cardViewFlip.flipTheView()
            }
        }
    }
}