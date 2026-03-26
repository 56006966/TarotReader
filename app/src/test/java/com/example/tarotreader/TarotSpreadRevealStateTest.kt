package com.example.tarotreader

import com.example.tarotreader.utils.TarotSpreadRevealState
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TarotSpreadRevealStateTest {

    @Test
    fun revealState_startsHiddenAndTracksReveal() {
        val state = TarotSpreadRevealState(3)

        assertFalse(state.isRevealed(0))
        assertFalse(state.allRevealed())

        assertTrue(state.reveal(0))
        assertTrue(state.isRevealed(0))
        assertFalse(state.allRevealed())
        assertFalse(state.reveal(0))
    }

    @Test
    fun revealState_restoresFromSavedArray() {
        val restored = TarotSpreadRevealState.fromBooleanArray(
            booleanArrayOf(true, false, true)
        )

        assertTrue(restored.isRevealed(0))
        assertFalse(restored.isRevealed(1))
        assertTrue(restored.isRevealed(2))
        assertArrayEquals(
            booleanArrayOf(true, false, true),
            restored.toBooleanArray()
        )
    }

    @Test
    fun revealState_reportsWhenEverythingIsRevealed() {
        val state = TarotSpreadRevealState(2)

        state.reveal(0)
        assertFalse(state.allRevealed())

        state.reveal(1)
        assertTrue(state.allRevealed())
    }
}
