package com.example.tarotreader.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tarotreader.R

class TarotDetailsFragment : Fragment(R.layout.fragment_tarot_card_details) {

    private val args: TarotDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardImage: ImageView = view.findViewById(R.id.cardImage)
        val cardName: TextView = view.findViewById(R.id.cardName)
        val cardMeaning: TextView = view.findViewById(R.id.cardMeaning)

        cardImage.setImageResource(args.cardImageRes)
        cardName.text = args.cardName
        cardMeaning.text = args.cardMeaning
        requireActivity().title = args.cardName
    }
}
