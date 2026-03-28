package com.example.tarotreader.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tarotreader.R
import androidx.navigation.fragment.findNavController
import com.example.tarotreader.data.TarotSpread
import com.example.tarotreader.network.BackendClient
import com.example.tarotreader.utils.TarotSpreadEngine

class TarotGameFragment : Fragment(R.layout.fragment_tarot_game) {

    private lateinit var spreadSpinner: Spinner
    private lateinit var spreadDescription: TextView
    private lateinit var sourceLabel: TextView
    private lateinit var drawButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.menu_tarot)
        spreadSpinner = view.findViewById(R.id.spreadSpinner)
        spreadDescription = view.findViewById(R.id.spreadDescription)
        sourceLabel = view.findViewById(R.id.sourceLabel)

        spreadSpinner.adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner_selected,
            TarotSpread.entries.map { it.displayName }
        ).apply {
            setDropDownViewResource(R.layout.item_spinner_dropdown)
        }
        spreadSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                selectedView: View?,
                position: Int,
                id: Long
            ) {
                updateSpreadPreview(TarotSpread.entries[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        view.findViewById<TextView>(R.id.tarotHeading).text = getString(R.string.tarot_heading)
        view.findViewById<TextView>(R.id.tarotSubheading).text = getString(R.string.tarot_subheading)
        drawButton = view.findViewById(R.id.btnDrawCard)
        drawButton.setOnClickListener {
            openSpreadReading()
        }
        view.findViewById<Button>(R.id.btnAllCards).setOnClickListener {
            findNavController().navigate(R.id.action_tarot_to_all_cards)
        }
        updateSpreadPreview(TarotSpread.entries.first())
    }

    private fun updateSpreadPreview(spread: TarotSpread) {
        spreadDescription.text = spread.prompt
        sourceLabel.visibility = View.GONE
    }

    private fun openSpreadReading() {
        val spread = TarotSpread.entries[spreadSpinner.selectedItemPosition]
        drawButton.isEnabled = false
        Thread {
            val remoteReading = BackendClient.drawTarotReading(spread)
            view?.post {
                if (!isAdded) return@post
                drawButton.isEnabled = true
                if (remoteReading != null) {
                    val action = TarotGameFragmentDirections.actionTarotToSpread(
                        spreadTitle = remoteReading.spreadTitle,
                        spreadHeadline = remoteReading.spreadHeadline,
                        spreadDetermination = remoteReading.spreadDetermination,
                        spreadSource = remoteReading.spreadSource,
                        cardPositions = remoteReading.cardPositions,
                        cardTitles = remoteReading.cardTitles,
                        cardMeanings = remoteReading.cardMeanings,
                        cardImages = remoteReading.cardImages
                    )
                    findNavController().navigate(action)
                } else {
                    openLocalSpreadReading(spread)
                }
            }
        }.start()
    }

    private fun openLocalSpreadReading(spread: TarotSpread) {
        val reading = TarotSpreadEngine.drawReading(spread)
        val action = TarotGameFragmentDirections.actionTarotToSpread(
            spreadTitle = reading.spread.displayName,
            spreadHeadline = reading.headline,
            spreadDetermination = reading.determination,
            spreadSource = reading.source,
            cardPositions = reading.cards.map { it.position }.toTypedArray(),
            cardTitles = reading.cards.map { TarotSpreadEngine.titleFor(it) }.toTypedArray(),
            cardMeanings = reading.cards.map { TarotSpreadEngine.meaningFor(it) }.toTypedArray(),
            cardImages = reading.cards.map { it.card.imageRes }.toIntArray()
        )
        findNavController().navigate(action)
    }
}
