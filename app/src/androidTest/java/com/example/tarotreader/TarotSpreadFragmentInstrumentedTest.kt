package com.example.tarotreader

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tarotreader.fragments.TarotSpreadFragment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TarotSpreadFragmentInstrumentedTest {

    @Test
    fun partiallyRevealedSpread_persistsAcrossRecreation() {
        val scenario = launchFragmentInContainer<TarotSpreadFragment>(
            fragmentArgs = spreadArgs(cardCount = 3),
            themeResId = R.style.Theme_TarotReader
        )

        clickCardAt(scenario, 0)

        scenario.onFragment { fragment ->
            val recyclerView = fragment.requireView().findViewById<RecyclerView>(R.id.spreadRecyclerView)
            val first = recyclerView.requireHolder(0)
            val second = recyclerView.requireHolder(1)
            val determination = fragment.requireView().findViewById<TextView>(R.id.spreadDetermination)

            assertTrue(first.findViewById<TextView>(R.id.spreadItemTitle).alpha > 0.9f)
            assertEquals(0f, second.findViewById<TextView>(R.id.spreadItemTitle).alpha, 0.01f)
            assertEquals(View.INVISIBLE, determination.visibility)
        }

        scenario.recreate()

        scenario.onFragment { fragment ->
            val recyclerView = fragment.requireView().findViewById<RecyclerView>(R.id.spreadRecyclerView)
            val first = recyclerView.requireHolder(0)
            val second = recyclerView.requireHolder(1)
            val determination = fragment.requireView().findViewById<TextView>(R.id.spreadDetermination)

            assertTrue(first.findViewById<TextView>(R.id.spreadItemTitle).alpha > 0.9f)
            assertEquals(0f, second.findViewById<TextView>(R.id.spreadItemTitle).alpha, 0.01f)
            assertEquals(View.INVISIBLE, determination.visibility)
        }
    }

    @Test
    fun allCardsRevealed_showsDeterminationAndKeepsItAfterRecreation() {
        val scenario = launchFragmentInContainer<TarotSpreadFragment>(
            fragmentArgs = spreadArgs(cardCount = 1),
            themeResId = R.style.Theme_TarotReader
        )

        clickCardAt(scenario, 0)

        scenario.onFragment { fragment ->
            val determination = fragment.requireView().findViewById<TextView>(R.id.spreadDetermination)
            assertEquals(View.VISIBLE, determination.visibility)
            assertTrue(determination.alpha > 0.9f)
        }

        scenario.recreate()

        scenario.onFragment { fragment ->
            val determination = fragment.requireView().findViewById<TextView>(R.id.spreadDetermination)
            assertEquals(View.VISIBLE, determination.visibility)
            assertTrue(determination.alpha > 0.9f)
        }
    }

    @Test
    fun determination_staysHiddenUntilFinalCardIsRevealed() {
        val scenario = launchFragmentInContainer<TarotSpreadFragment>(
            fragmentArgs = spreadArgs(cardCount = 3),
            themeResId = R.style.Theme_TarotReader
        )

        clickCardAt(scenario, 0)
        clickCardAt(scenario, 1)

        scenario.onFragment { fragment ->
            val determination = fragment.requireView().findViewById<TextView>(R.id.spreadDetermination)
            assertEquals(View.INVISIBLE, determination.visibility)
            assertEquals(0f, determination.alpha, 0.01f)
        }

        clickCardAt(scenario, 2)

        scenario.onFragment { fragment ->
            val determination = fragment.requireView().findViewById<TextView>(R.id.spreadDetermination)
            assertEquals(View.VISIBLE, determination.visibility)
            assertTrue(determination.alpha > 0.9f)
        }
    }

    private fun clickCardAt(
        scenario: androidx.fragment.app.testing.FragmentScenario<TarotSpreadFragment>,
        position: Int
    ) {
        scenario.onFragment { fragment ->
            val recyclerView = fragment.requireView().findViewById<RecyclerView>(R.id.spreadRecyclerView)
            val holder = recyclerView.requireHolder(position)
            holder.findViewById<ImageView>(R.id.spreadItemImage).performClick()
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        SystemClock.sleep(700)
    }

    private fun spreadArgs(cardCount: Int): Bundle {
        val positions = Array(cardCount) { index -> "Position ${index + 1}" }
        val titles = Array(cardCount) { index ->
            if (index == 0) "Card ${index + 1} (Reversed)" else "Card ${index + 1}"
        }
        val meanings = Array(cardCount) { index -> "Meaning ${index + 1}" }
        val images = IntArray(cardCount) { R.drawable.card_back }

        return Bundle().apply {
            putString("spreadTitle", "Test Spread")
            putString("spreadHeadline", "Headline")
            putString("spreadDetermination", "Determination")
            putString("spreadSource", "Source")
            putStringArray("cardPositions", positions)
            putStringArray("cardTitles", titles)
            putStringArray("cardMeanings", meanings)
            putIntArray("cardImages", images)
        }
    }

    private fun RecyclerView.requireHolder(position: Int): View {
        findViewHolderForAdapterPosition(position)?.itemView?.let { return it }
        measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        layout(left, top, right, bottom)
        return requireNotNull(findViewHolderForAdapterPosition(position)?.itemView) {
            "No ViewHolder found for adapter position $position"
        }
    }
}
