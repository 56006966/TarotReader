package com.example.tarotreader.ui.horoscope

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tarotreader.R
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

class HoroscopeFragment : Fragment(R.layout.fragment_horoscope) {

    private val today: Calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.menu_horoscope)

        val spinner = view.findViewById<Spinner>(R.id.signSpinner)
        val signAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            HoroscopeData.signs
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.adapter = signAdapter

        val formattedDate = DateFormat.getDateInstance(
            DateFormat.FULL,
            Locale.getDefault()
        ).format(today.time)
        view.findViewById<TextView>(R.id.horoscopeDate).text =
            getString(R.string.horoscope_for_date, formattedDate)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                selectedView: View?,
                position: Int,
                id: Long
            ) {
                renderReading(view, HoroscopeData.signs[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        renderReading(view, HoroscopeData.signs.first())
    }

    private fun renderReading(root: View, sign: String) {
        val reading = HoroscopeData.buildReading(sign, today.get(Calendar.DAY_OF_YEAR))
        root.findViewById<TextView>(R.id.overviewBody).text = reading.overview
        root.findViewById<TextView>(R.id.loveBody).text = reading.love
        root.findViewById<TextView>(R.id.careerBody).text = reading.career
        root.findViewById<TextView>(R.id.energyBody).text = reading.energy
        root.findViewById<TextView>(R.id.luckyBody).text =
            reading.luckyVibe.replaceFirstChar { it.titlecase() }
    }
}
