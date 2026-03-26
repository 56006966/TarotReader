package com.example.tarotreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tarotreader.R
import com.example.tarotreader.data.TarotCard
import com.example.tarotreader.utils.TarotCardFlipAnimator

class AllTarotCardsAdapter(
    private val cards: List<TarotCard>
) : RecyclerView.Adapter<AllTarotCardsAdapter.AllCardsViewHolder>() {

    private val faceStates = MutableList(cards.size) { true }

    inner class AllCardsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.allCardImage)
        val titleView: TextView = view.findViewById(R.id.allCardTitle)
        val suitView: TextView = view.findViewById(R.id.allCardSuit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCardsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_all_tarot_card, parent, false)
        return AllCardsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllCardsViewHolder, position: Int) {
        val card = cards[position]
        holder.titleView.text = card.name
        holder.suitView.text = card.suit ?: "Tarot"
        TarotCardFlipAnimator.bindCardFace(
            holder.imageView,
            faceStates[position],
            card.imageRes,
            false
        )
        holder.imageView.setOnClickListener {
            faceStates[position] = !faceStates[position]
            TarotCardFlipAnimator.flip(
                holder.imageView,
                faceStates[position],
                card.imageRes,
                false
            )
        }
    }

    override fun getItemCount(): Int = cards.size
}
