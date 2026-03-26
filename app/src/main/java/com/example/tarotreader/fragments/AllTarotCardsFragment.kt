package com.example.tarotreader.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tarotreader.R
import com.example.tarotreader.adapters.AllTarotCardsAdapter
import com.example.tarotreader.utils.TarotDeck

class AllTarotCardsFragment : Fragment(R.layout.fragment_all_tarot_cards) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.all_cards_title)

        val cards = TarotDeck.getAllCards()
        view.findViewById<TextView>(R.id.allCardsCount).text =
            getString(R.string.all_cards_count, cards.size)

        val recyclerView = view.findViewById<RecyclerView>(R.id.allCardsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = AllTarotCardsAdapter(cards)
    }
}
