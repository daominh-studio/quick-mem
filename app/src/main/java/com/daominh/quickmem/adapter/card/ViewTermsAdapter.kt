package com.daominh.quickmem.adapter.card

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ItemViewTermsBinding
import java.util.*
import kotlin.collections.ArrayList

class ViewTermsAdapter(
    private val cards: ArrayList<Card>
) : RecyclerView.Adapter<ViewTermsAdapter.ViewHolder>() {
    var textToSpeech: TextToSpeech? = null
    class ViewHolder(val binding: ItemViewTermsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewTermsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewTermsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewTermsAdapter.ViewHolder, position: Int) {
        val card = cards[position]
        holder.binding.termsTv.text = card.front
        holder.binding.definitionTv.text = card.back

        holder.binding.soundIv.setOnClickListener{
            textToSpeech = TextToSpeech(holder.itemView.context, TextToSpeech.OnInitListener { status ->
                if (status != TextToSpeech.ERROR) {
                    textToSpeech!!.language = Locale.US
                }else{
                    textToSpeech!!.language = Locale.US
                }
            })


        }

    }

    override fun getItemCount(): Int {
        return cards.size
    }
}
