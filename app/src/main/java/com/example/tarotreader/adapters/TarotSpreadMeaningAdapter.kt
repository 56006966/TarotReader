package com.example.tarotreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tarotreader.R
import com.example.tarotreader.utils.TarotCardFlipAnimator
import com.example.tarotreader.utils.TarotSpreadRevealState

data class TarotSpreadMeaningItem(
    val position: String,
    val title: String,
    val meaning: String,
    val imageRes: Int,
    val isReversed: Boolean
)

class TarotSpreadMeaningAdapter(
    private val items: List<TarotSpreadMeaningItem>,
    private val revealState: TarotSpreadRevealState,
    private val onAllCardsRevealed: (() -> Unit)? = null
) : RecyclerView.Adapter<TarotSpreadMeaningAdapter.SpreadMeaningViewHolder>() {

    inner class SpreadMeaningViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val positionView: TextView = view.findViewById(R.id.spreadItemPosition)
        val titleView: TextView = view.findViewById(R.id.spreadItemTitle)
        val imageView: ImageView = view.findViewById(R.id.spreadItemImage)
        val meaningView: TextView = view.findViewById(R.id.spreadItemMeaning)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpreadMeaningViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarot_spread_meaning, parent, false)
        return SpreadMeaningViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpreadMeaningViewHolder, position: Int) {
        val item = items[position]
        holder.positionView.text = item.position
        holder.titleView.text = item.title
        holder.meaningView.text = item.meaning

        val isRevealed = revealState.isRevealed(position)
        holder.positionView.alpha = if (isRevealed) 1f else 0f
        holder.titleView.alpha = if (isRevealed) 1f else 0f
        holder.meaningView.alpha = if (isRevealed) 1f else 0f
        TarotCardFlipAnimator.bindCardFace(
            holder.imageView,
            isRevealed,
            item.imageRes,
            item.isReversed
        )
        holder.imageView.setOnClickListener {
            if (!revealState.reveal(position)) return@setOnClickListener
            TarotCardFlipAnimator.reveal(
                holder.imageView,
                item.imageRes,
                item.isReversed
            ) {
                holder.positionView.animate().alpha(1f).setDuration(220).start()
                holder.titleView.animate().alpha(1f).setDuration(220).start()
                holder.meaningView.animate().alpha(1f).setDuration(260).start()
                if (revealState.allRevealed()) {
                    onAllCardsRevealed?.invoke()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
