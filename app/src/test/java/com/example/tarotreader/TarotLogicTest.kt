package com.example.tarotreader

import com.example.tarotreader.data.TarotCard
import com.example.tarotreader.data.TarotSpread
import com.example.tarotreader.data.TarotSpreadCard
import com.example.tarotreader.utils.TarotDeck
import com.example.tarotreader.utils.TarotSpreadEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TarotLogicTest {

    @Test
    fun fullDeck_containsAll78UniqueCards() {
        val cards = TarotDeck.getAllCards()

        assertEquals(78, cards.size)
        assertEquals(78, cards.map { it.name }.toSet().size)
    }

    @Test
    fun fullDeck_includesMajorArcanaAndCourtCards() {
        val cards = TarotDeck.getAllCards()
        val names = cards.map { it.name }.toSet()

        assertTrue("The Fool should exist", "The Fool" in names)
        assertTrue("The World should exist", "The World" in names)
        assertTrue("King of Cups should exist", "King of Cups" in names)
        assertTrue("Page of Pentacles should exist", "Page of Pentacles" in names)
    }

    @Test
    fun allCards_haveUprightAndReversedMeanings() {
        val cards = TarotDeck.getAllCards()

        assertTrue(cards.all { it.meaning.isNotBlank() })
        assertTrue(cards.all { !it.reversedMeaning.isNullOrBlank() })
    }

    @Test
    fun drawReading_matchesSpreadCardCountAndPositions() {
        TarotSpread.entries.forEach { spread ->
            val reading = TarotSpreadEngine.drawReading(spread)

            assertEquals(spread.cardCount, reading.cards.size)
            assertEquals(spread.positions, reading.cards.map { it.position })
            assertTrue(reading.headline.isNotBlank())
            assertTrue(reading.determination.isNotBlank())
        }
    }

    @Test
    fun buildPositionMeaning_includesPositionAndOrientation() {
        val card = TarotCard(
            name = "Test Card",
            suit = "Wands",
            imageRes = 123,
            meaning = "Upright meaning",
            reversedMeaning = "Reversed meaning"
        )
        val spreadCard = TarotSpreadCard(
            position = "Future",
            card = card,
            isReversed = true
        )

        val result = TarotSpreadEngine.buildPositionMeaning(spreadCard)

        assertTrue(result.contains("Future"))
        assertTrue(result.contains("Reversed"))
        assertTrue(result.contains("Reversed meaning"))
    }

    @Test
    fun titleFor_marksReversedCards() {
        val baseCard = TarotCard(
            name = "The Star",
            suit = "Major Arcana",
            imageRes = 1,
            meaning = "Hope",
            reversedMeaning = "Doubt"
        )

        val upright = TarotSpreadEngine.titleFor(
            TarotSpreadCard("Guidance", baseCard, false)
        )
        val reversed = TarotSpreadEngine.titleFor(
            TarotSpreadCard("Guidance", baseCard, true)
        )

        assertEquals("The Star", upright)
        assertEquals("The Star (Reversed)", reversed)
    }

    @Test
    fun generatedReading_usesCardsFromDeck() {
        val deckNames = TarotDeck.getAllCards().map { it.name }.toSet()
        val reading = TarotSpreadEngine.drawReading(TarotSpread.CELTIC_CROSS)

        assertEquals(10, reading.cards.size)
        assertTrue(reading.cards.all { it.card.name in deckNames })
    }
}
