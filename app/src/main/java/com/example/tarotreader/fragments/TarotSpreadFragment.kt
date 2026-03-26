package com.example.tarotreader.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tarotreader.R
import com.example.tarotreader.adapters.TarotSpreadMeaningAdapter
import com.example.tarotreader.adapters.TarotSpreadMeaningItem
import com.example.tarotreader.utils.TarotSpreadRevealState

class TarotSpreadFragment : Fragment(R.layout.fragment_tarot_spread) {

    private val args: TarotSpreadFragmentArgs by navArgs()
    private lateinit var revealState: TarotSpreadRevealState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = args.spreadTitle

        view.findViewById<TextView>(R.id.spreadTitle).text = args.spreadTitle
        view.findViewById<TextView>(R.id.spreadHeadline).text = args.spreadHeadline
        val determinationView = view.findViewById<TextView>(R.id.spreadDetermination)
        determinationView.text = args.spreadDetermination
        val restoredState = savedInstanceState?.getBooleanArray(KEY_REVEAL_STATE)
        revealState = if (restoredState != null) {
            TarotSpreadRevealState.fromBooleanArray(restoredState)
        } else {
            TarotSpreadRevealState(args.cardPositions.size)
        }
        determinationView.alpha = if (revealState.allRevealed()) 1f else 0f
        determinationView.visibility = if (revealState.allRevealed()) View.VISIBLE else View.INVISIBLE
        view.findViewById<TextView>(R.id.spreadSource).visibility = View.GONE

        val items = args.cardPositions.indices.map { index ->
            TarotSpreadMeaningItem(
                position = args.cardPositions[index],
                title = args.cardTitles[index],
                meaning = args.cardMeanings[index],
                imageRes = args.cardImages[index],
                isReversed = args.cardTitles[index].contains("(Reversed)")
            )
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.spreadRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TarotSpreadMeaningAdapter(
            items = items,
            revealState = revealState
        ) {
            if (!determinationView.isShown) {
                determinationView.visibility = View.VISIBLE
                determinationView.animate().alpha(1f).setDuration(320).start()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::revealState.isInitialized) {
            outState.putBooleanArray(KEY_REVEAL_STATE, revealState.toBooleanArray())
        }
    }

    companion object {
        private const val KEY_REVEAL_STATE = "tarot_spread_reveal_state"
    }
}
