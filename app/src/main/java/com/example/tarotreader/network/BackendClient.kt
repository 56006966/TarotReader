package com.example.tarotreader.network

import com.example.tarotreader.BuildConfig
import com.example.tarotreader.R
import com.example.tarotreader.data.TarotSpread
import com.example.tarotreader.ui.horoscope.HoroscopeReading
import com.example.tarotreader.utils.TarotDeck
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class BackendTarotReading(
    val spreadTitle: String,
    val spreadHeadline: String,
    val spreadDetermination: String,
    val spreadSource: String,
    val cardPositions: Array<String>,
    val cardTitles: Array<String>,
    val cardMeanings: Array<String>,
    val cardImages: IntArray
)

object BackendClient {

    private const val CONNECT_TIMEOUT_MS = 2500
    private const val READ_TIMEOUT_MS = 3500
    private val baseUrl = BuildConfig.BACKEND_BASE_URL.trim().trimEnd('/')

    fun fetchHoroscope(sign: String): HoroscopeReading? {
        val encodedSign = URLEncoder.encode(sign, StandardCharsets.UTF_8.name())
        val json = request(
            method = "GET",
            path = "/tarotreader/horoscope?sign=$encodedSign"
        ) ?: return null

        return HoroscopeReading(
            overview = json.optString("overview"),
            love = json.optString("love"),
            career = json.optString("career"),
            energy = json.optString("energy"),
            luckyVibe = json.optString("luckyVibe")
        )
    }

    fun drawTarotReading(spread: TarotSpread): BackendTarotReading? {
        val response = request(
            method = "POST",
            path = "/tarotreader/tarot/spread",
            body = JSONObject().put("spread", spread.name).toString()
        ) ?: return null

        val cards = response.optJSONArray("cards") ?: JSONArray()
        val positions = Array(cards.length()) { "" }
        val titles = Array(cards.length()) { "" }
        val meanings = Array(cards.length()) { "" }
        val images = IntArray(cards.length()) { R.drawable.card_back }

        for (index in 0 until cards.length()) {
            val card = cards.optJSONObject(index) ?: continue
            val cardName = card.optString("name")
            val isReversed = card.optBoolean("isReversed", false)
            positions[index] = card.optString("position")
            titles[index] = if (isReversed) "$cardName (Reversed)" else cardName
            meanings[index] = card.optString("meaning")
            images[index] = TarotDeck.findCardByName(cardName)?.imageRes ?: R.drawable.card_back
        }

        return BackendTarotReading(
            spreadTitle = response.optString("displayName", spread.displayName),
            spreadHeadline = response.optString("headline", spread.prompt),
            spreadDetermination = response.optString("determination", spread.prompt),
            spreadSource = response.optString("source", "tarotreader.phunkypixels.com"),
            cardPositions = positions,
            cardTitles = titles,
            cardMeanings = meanings,
            cardImages = images
        )
    }

    private fun request(
        method: String,
        path: String,
        body: String? = null
    ): JSONObject? {
        if (!baseUrl.startsWith("http")) return null

        val connection = (URL("$baseUrl$path").openConnection() as? HttpURLConnection) ?: return null
        return try {
            connection.requestMethod = method
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connection.doInput = true

            if (body != null) {
                connection.doOutput = true
                OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8).use { writer ->
                    writer.write(body)
                }
            }

            val stream = if (connection.responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            } ?: return null

            val payload = BufferedReader(stream.reader(StandardCharsets.UTF_8)).use { it.readText() }
            if (connection.responseCode !in 200..299 || payload.isBlank()) return null
            JSONObject(payload)
        } catch (_: Exception) {
            null
        } finally {
            connection.disconnect()
        }
    }
}
