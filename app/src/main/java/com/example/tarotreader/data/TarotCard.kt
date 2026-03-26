package com.example.tarotreader.data

data class TarotCard(
    val name: String,
    val suit: String?,
    val imageRes: Int,
    val meaning: String,
    val reversedMeaning: String? = null
)
