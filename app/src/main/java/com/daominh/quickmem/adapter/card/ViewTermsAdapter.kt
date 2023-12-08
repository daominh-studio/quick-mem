package com.daominh.quickmem.adapter.card

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ItemViewTermsBinding
import kotlin.collections.ArrayList

class ViewTermsAdapter(
    private val cards: ArrayList<Card>
) : RecyclerView.Adapter<ViewTermsAdapter.ViewHolder>() {
    private var textToSpeech: TextToSpeech? = null

    class ViewHolder(val binding: ItemViewTermsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewTermsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]

        val status = card.status
        // change color of card
        if (status == 1) {
            holder.binding.cardView.setCardBackgroundColor(holder.itemView.context.getColor(android.R.color.holo_green_light))
        } else if (status == 2) {
            holder.binding.cardView.setCardBackgroundColor(holder.itemView.context.getColor(android.R.color.holo_orange_light))
        } else {
            holder.binding.cardView.setCardBackgroundColor(holder.itemView.context.getColor(android.R.color.white))
        }

        holder.binding.termsTv.text = card.front
        holder.binding.definitionTv.text = card.back

        holder.binding.soundIv.setOnClickListener {
            if (holder.binding.termsTv.text.toString().isNotEmpty()) {
                textToSpeech = TextToSpeech(holder.itemView.context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        val result = textToSpeech?.setLanguage(textToSpeech?.voice?.locale)
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Toast.makeText(holder.itemView.context, "Language not supported", Toast.LENGTH_SHORT).show()
                        } else {
                            textToSpeech?.speak(
                                holder.binding.termsTv.text.toString(),
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                            )
                        }
                    } else {
                        Toast.makeText(holder.itemView.context, "Initialization failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {

        return cards.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        if (textToSpeech != null) {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }
}