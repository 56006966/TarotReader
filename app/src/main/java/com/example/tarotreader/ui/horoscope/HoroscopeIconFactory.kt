package com.example.tarotreader.ui.horoscope

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.example.tarotreader.R
import kotlin.math.roundToInt

object HoroscopeIconFactory {

    fun createTarotIcon(context: Context): Drawable {
        val source = BitmapFactory.decodeResource(context.resources, R.drawable.tarot_icon)
        val sized = Bitmap.createScaledBitmap(
            source,
            iconSizePx(context),
            iconSizePx(context),
            true
        )
        return BitmapDrawable(context.resources, sized)
    }

    fun createHoroscopeIcon(context: Context, sign: String): Drawable {
        val sheet = BitmapFactory.decodeResource(context.resources, R.drawable.astrology_icon_sheet)
        val cols = 3
        val rows = 4
        val cellWidth = sheet.width / cols
        val cellHeight = sheet.height / rows
        val (col, row) = zodiacCellFor(sign)
        val cropped = Bitmap.createBitmap(
            sheet,
            col * cellWidth,
            row * cellHeight,
            cellWidth,
            cellHeight
        )
        val padded = Bitmap.createBitmap(cellWidth, cellHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(padded)
        val inset = (cellWidth * 0.08f).roundToInt()
        canvas.drawBitmap(cropped, inset.toFloat(), inset.toFloat(), null)
        val sized = Bitmap.createScaledBitmap(
            padded,
            iconSizePx(context),
            iconSizePx(context),
            true
        )
        return BitmapDrawable(context.resources, sized)
    }

    private fun zodiacCellFor(sign: String): Pair<Int, Int> {
        return when (sign) {
            "Cancer" -> 0 to 0
            "Leo" -> 1 to 0
            "Aries" -> 2 to 0
            "Libra" -> 0 to 1
            "Virgo" -> 1 to 1
            "Scorpio" -> 2 to 1
            "Taurus" -> 0 to 2
            "Aquarius" -> 1 to 2
            "Gemini" -> 2 to 2
            "Capricorn" -> 0 to 3
            "Sagittarius" -> 1 to 3
            "Pisces" -> 2 to 3
            else -> 1 to 2
        }
    }

    private fun iconSizePx(context: Context): Int {
        return (24f * context.resources.displayMetrics.density).roundToInt().coerceAtLeast(24)
    }
}
