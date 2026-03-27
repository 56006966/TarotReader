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
    private val determination: String,
    private val onAllCardsRevealed: (() -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CARD = 0
        private const val VIEW_TYPE_RESULT = 1
    }

    inner class SpreadMeaningViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val positionView: TextView = view.findViewById(R.id.spreadItemPosition)
        val titleView: TextView = view.findViewById(R.id.spreadItemTitle)
        val imageView: ImageView = view.findViewById(R.id.spreadItemImage)
        val meaningView: TextView = view.findViewById(R.id.spreadItemMeaning)
    }

    inner class SpreadResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resultBody: TextView = view.findViewById(R.id.spreadResultBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_RESULT) {
            SpreadResultViewHolder(
                inflater.inflate(R.layout.item_tarot_spread_result, parent, false)
            )
        } else {
            SpreadMeaningViewHolder(
                inflater.inflate(R.layout.item_tarot_spread_meaning, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SpreadResultViewHolder) {
            bindResult(holder)
            return
        }
        holder as SpreadMeaningViewHolder
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
                    notifyItemChanged(items.size)
                    onAllCardsRevealed?.invoke()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) VIEW_TYPE_RESULT else VIEW_TYPE_CARD
    }

    override fun getItemCount(): Int = items.size + 1

    private fun bindResult(holder: SpreadResultViewHolder) {
        holder.resultBody.text = determination
        holder.itemView.alpha = if (revealState.allRevealed()) 1f else 0f
        holder.itemView.visibility = if (revealState.allRevealed()) View.VISIBLE else View.GONE
    }
}
