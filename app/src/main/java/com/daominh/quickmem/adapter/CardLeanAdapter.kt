package com.daominh.quickmem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ItemLearnSetBinding

class CardLeanAdapter(
    private var cardList: List<Card>
) : RecyclerView.Adapter<CardLeanAdapter.ViewHolder>() {

    private var isAnimationRunning = false
    private var flippedStates = MutableList(cardList.size) { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLearnSetBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card, position)
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

        fun bind(card: Card, position: Int) {
            binding.itemFont.text = card.front
            binding.itemBack.text = card.back

            if (flippedStates[position]) {
                binding.itemFont.visibility = View.GONE
                binding.itemBack.visibility = View.VISIBLE
                binding.itemBack.rotationX = -180f
            } else {
                binding.itemFont.visibility = View.VISIBLE
                binding.itemBack.visibility = View.GONE
                binding.itemFont.rotationX = 0f
            }

            binding.cardView.setOnClickListener {
                if (!isAnimationRunning) {
                    flipCard(position, binding)
                }
            }
        }
    }

    private fun flipCard(position: Int, binding: ItemLearnSetBinding) {
        if (!flippedStates[position]) {
            binding.cardView.animate()
                .rotationYBy(90f)
                .setInterpolator(
                    LinearInterpolator()
                )
                .setDuration(350)
                .withEndAction {
                    flippedStates[position] = true
                    binding.itemFont.visibility = View.GONE
                    binding.itemBack.visibility = View.VISIBLE
                    binding.itemBack.rotationX = -180f
                    binding.cardView.animate()
                        .rotationYBy(90f)
                        .setDuration(350)
                        .withEndAction {
                            isAnimationRunning = false
                        }
                        .start()
                }
                .start()
        } else {
            binding.cardView.animate()
                .rotationYBy(-90f)
                .setDuration(350)
                .setInterpolator(
                    LinearInterpolator()
                )
                .withEndAction {
                    flippedStates[position] = false
                    binding.itemFont.visibility = View.VISIBLE
                    binding.itemBack.visibility = View.GONE
                    binding.itemFont.rotationX = 0f
                    binding.cardView.animate()
                        .rotationYBy(-90f)
                        .setDuration(350)
                        .withEndAction {
                            isAnimationRunning = false
                        }
                        .start()
                }
                .start()
        }
        isAnimationRunning = true
    }
}