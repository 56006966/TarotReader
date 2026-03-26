package com.example.tarotreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tarotreader.R
import com.example.tarotreader.data.TarotCard
import com.example.tarotreader.data.TarotSpreadCard

class TarotCardAdapter(
    private val cards: List<TarotSpreadCard>,
    private val onClick: (TarotCard) -> Unit
) : RecyclerView.Adapter<TarotCardAdapter.CardViewHolder>() {

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val positionView: TextView = view.findViewById(R.id.cardPosition)
        val imageView: ImageView = view.findViewById(R.id.cardImage)
        val nameView: TextView = view.findViewById(R.id.cardName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarot_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val spreadCard = cards[position]
        holder.positionView.text = spreadCard.position
        holder.imageView.setImageResource(spreadCard.card.imageRes)
        holder.imageView.rotation = if (spreadCard.isReversed) 180f else 0f
        holder.nameView.text = if (spreadCard.isReversed) {
            "${spreadCard.card.name} (Reversed)"
        } else {
            spreadCard.card.name
        }
        holder.itemView.setOnClickListener { onClick(spreadCard.card) }
    }

    override fun getItemCount() = cards.size
}
