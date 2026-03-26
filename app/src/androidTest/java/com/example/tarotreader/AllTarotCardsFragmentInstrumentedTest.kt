package com.example.tarotreader

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tarotreader.fragments.AllTarotCardsFragment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AllTarotCardsFragmentInstrumentedTest {

    @Test
    fun allCardsScreen_showsLoadedDeckCount() {
        val scenario = launchFragmentInContainer<AllTarotCardsFragment>(
            themeResId = R.style.Theme_TarotReader
        )

        scenario.onFragment { fragment ->
            val countView = fragment.requireView().findViewById<TextView>(R.id.allCardsCount)
            assertTrue(countView.text.toString().contains("78"))
        }
    }

    @Test
    fun tappingGalleryCard_flipsToBack() {
        val scenario = launchFragmentInContainer<AllTarotCardsFragment>(
            themeResId = R.style.Theme_TarotReader
        )

        scenario.onFragment { fragment ->
            val recyclerView = fragment.requireView().findViewById<RecyclerView>(R.id.allCardsRecyclerView)
            val first = recyclerView.requireHolder(0)
            val imageView = first.findViewById<ImageView>(R.id.allCardImage)
            assertEquals(0f, imageView.rotation, 0.01f)
            imageView.performClick()
        }

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        SystemClock.sleep(500)

        scenario.onFragment { fragment ->
            val recyclerView = fragment.requireView().findViewById<RecyclerView>(R.id.allCardsRecyclerView)
            val first = recyclerView.requireHolder(0)
            val imageView = first.findViewById<ImageView>(R.id.allCardImage)
            val shownRes = imageView.tag as Int
            assertEquals("card_back", fragment.resources.getResourceEntryName(shownRes))
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
