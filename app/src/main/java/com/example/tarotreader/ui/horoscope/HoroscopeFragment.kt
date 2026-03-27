package com.example.tarotreader.ui.horoscope

import android.os.Bundle
import android.content.Context
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
            R.layout.item_spinner_selected,
            HoroscopeData.signs
        ).apply {
            setDropDownViewResource(R.layout.item_spinner_dropdown)
        }
        spinner.adapter = signAdapter
        val preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedSign = preferences.getString(KEY_LAST_SIGN, HoroscopeData.signs.first())
        val initialSelection = HoroscopeData.signs.indexOf(savedSign).takeIf { it >= 0 } ?: 0

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
                val selectedSign = HoroscopeData.signs[position]
                preferences.edit().putString(KEY_LAST_SIGN, selectedSign).apply()
                (activity as? com.example.tarotreader.MainActivity)?.refreshBottomNavIcons()
                renderReading(view, selectedSign)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        spinner.setSelection(initialSelection, false)
        renderReading(view, HoroscopeData.signs[initialSelection])
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

    companion object {
        const val PREFS_NAME = "horoscope_preferences"
        const val KEY_LAST_SIGN = "last_selected_sign"
    }
}
