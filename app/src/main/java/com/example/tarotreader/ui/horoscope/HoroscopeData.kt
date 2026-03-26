package com.example.tarotreader.ui.horoscope

data class HoroscopeReading(
    val overview: String,
    val love: String,
    val career: String,
    val energy: String,
    val luckyVibe: String
)

object HoroscopeData {

    val signs = listOf(
        "Aries",
        "Taurus",
        "Gemini",
        "Cancer",
        "Leo",
        "Virgo",
        "Libra",
        "Scorpio",
        "Sagittarius",
        "Capricorn",
        "Aquarius",
        "Pisces"
    )

    private val overviewOpeners = listOf(
        "A clear signal is coming through today.",
        "Your instincts are sharper than usual.",
        "A softer approach gets better results right now.",
        "Momentum builds once you commit to one priority."
    )

    private val loveMessages = listOf(
        "Say the honest thing kindly and let the conversation breathe.",
        "Warmth matters more than perfection today.",
        "Pay attention to who feels easy to be yourself around.",
        "A small gesture can reset the tone of a relationship."
    )

    private val careerMessages = listOf(
        "Finish one meaningful task before scattering your focus.",
        "A practical adjustment will make the rest of the week easier.",
        "Trust the skill you have already built.",
        "A direct message or follow-up could open the next door."
    )

    private val energyMessages = listOf(
        "Protect your pace and leave space between commitments.",
        "Movement helps clear mental static today.",
        "Recharge before you reach for more stimulation.",
        "Your energy improves when your environment is less cluttered."
    )

    private val luckyVibes = listOf(
        "gold tones and a bold first step",
        "fresh air and an uncluttered plan",
        "music, color, and a playful attitude",
        "quiet focus and one meaningful text"
    )

    fun buildReading(sign: String, daySeed: Int): HoroscopeReading {
        val signSeed = signs.indexOf(sign).coerceAtLeast(0)
        return HoroscopeReading(
            overview = "${overviewOpeners[(daySeed + signSeed) % overviewOpeners.size]} $sign, stay present and let your next move be deliberate.",
            love = loveMessages[(daySeed + signSeed * 2) % loveMessages.size],
            career = careerMessages[(daySeed + signSeed * 3) % careerMessages.size],
            energy = energyMessages[(daySeed + signSeed * 4) % energyMessages.size],
            luckyVibe = luckyVibes[(daySeed + signSeed * 5) % luckyVibes.size]
        )
    }
}
