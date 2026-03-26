package com.example.tarotreader.data

data class TarotSpreadCard(
    val position: String,
    val card: TarotCard,
    val isReversed: Boolean
)

data class TarotSpreadReading(
    val spread: TarotSpread,
    val source: String,
    val headline: String,
    val determination: String,
    val cards: List<TarotSpreadCard>
)
