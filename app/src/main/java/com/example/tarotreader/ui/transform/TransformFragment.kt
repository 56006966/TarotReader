package com.example.tarotreader.ui.transform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class TransformFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FrameLayout(requireContext()).apply {
            addView(TextView(context).apply {
                text = "Legacy template screen"
                textSize = 18f
                gravity = android.view.Gravity.CENTER
            })
        }
    }
}
