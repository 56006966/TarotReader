package com.example.tarotreader.utils

class TarotSpreadRevealState private constructor(
    private val revealed: BooleanArray
) {

    constructor(size: Int) : this(BooleanArray(size))

    fun isRevealed(index: Int): Boolean = revealed[index]

    fun reveal(index: Int): Boolean {
        if (revealed[index]) return false
        revealed[index] = true
        return true
    }

    fun allRevealed(): Boolean = revealed.all { it }

    fun toBooleanArray(): BooleanArray = revealed.copyOf()

    companion object {
        fun fromBooleanArray(values: BooleanArray): TarotSpreadRevealState {
            return TarotSpreadRevealState(values.copyOf())
        }
    }
}
