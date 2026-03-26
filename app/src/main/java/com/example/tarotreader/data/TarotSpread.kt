package com.example.tarotreader.data

enum class TarotSpread(
    val displayName: String,
    val cardCount: Int,
    val positions: List<String>,
    val prompt: String
) {
    DAILY(
        displayName = "Daily Card",
        cardCount = 1,
        positions = listOf("Guidance"),
        prompt = "A simple one-card pull for the energy around you today."
    ),
    PAST_PRESENT_FUTURE(
        displayName = "Past, Present, Future",
        cardCount = 3,
        positions = listOf("Past", "Present", "Future"),
        prompt = "See what shaped this moment, where you stand now, and what is unfolding next."
    ),
    LOVE(
        displayName = "Love Spread",
        cardCount = 3,
        positions = listOf("Your Heart", "Their Energy", "Next Step"),
        prompt = "A relationship-focused spread for emotional clarity and gentle direction."
    ),
    CAREER(
        displayName = "Career Spread",
        cardCount = 3,
        positions = listOf("Current Path", "Challenge", "Opportunity"),
        prompt = "Use this spread to check your momentum, obstacles, and practical next move."
    ),
    CELTIC_CROSS_LITE(
        displayName = "Celtic Cross Lite",
        cardCount = 5,
        positions = listOf("Present", "Challenge", "Foundation", "Guidance", "Outcome"),
        prompt = "A deeper five-card spread for complex situations without the full ten-card intensity."
    ),
    CELTIC_CROSS(
        displayName = "Celtic Cross",
        cardCount = 10,
        positions = listOf(
            "Present",
            "Challenge",
            "Foundation",
            "Recent Past",
            "Conscious Goal",
            "Near Future",
            "Self",
            "Environment",
            "Hopes and Fears",
            "Outcome"
        ),
        prompt = "A full ten-card spread for layered situations, showing the core dynamic, surrounding influences, and likely direction."
    )
}
