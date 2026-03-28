package com.example.tarotreader.utils

import com.example.tarotreader.R
import com.example.tarotreader.data.TarotCard
import java.util.Locale

object TarotDeck {

    private val orderedResourceNames = listOf(
        "the_fool",
        "the_magician",
        "the_high_priestess",
        "the_empress",
        "the_emperor",
        "the_hierophant",
        "the_lovers",
        "the_chariot",
        "strength",
        "the_hermit",
        "wheel_of_fortune",
        "justice",
        "the_hanged_man",
        "death",
        "temperance",
        "the_devil",
        "the_tower",
        "the_star",
        "the_moon",
        "the_sun",
        "judgement",
        "the_world",
        "ace_of_wands",
        "two_of_wands",
        "three_of_wands",
        "four_of_wands",
        "five_of_wands",
        "six_of_wands",
        "seven_of_wands",
        "eight_of_wands",
        "nine_of_wands",
        "ten_of_wands",
        "page_of_wands",
        "knight_of_wands",
        "queen_of_wands",
        "king_of_wands",
        "ace_of_cups",
        "two_of_cups",
        "three_of_cups",
        "four_of_cups",
        "five_of_cups",
        "six_of_cups",
        "seven_of_cups",
        "eight_of_cups",
        "nine_of_cups",
        "ten_of_cups",
        "page_of_cups",
        "knight_of_cups",
        "queen_of_cups",
        "king_of_cups",
        "ace_of_swords",
        "two_of_swords",
        "three_of_swords",
        "four_of_swords",
        "five_of_swords",
        "six_of_swords",
        "seven_of_swords",
        "eight_of_swords",
        "nine_of_swords",
        "ten_of_swords",
        "page_of_swords",
        "knight_of_swords",
        "queen_of_swords",
        "king_of_swords",
        "ace_of_pentacles",
        "two_of_pentacles",
        "three_of_pentacles",
        "four_of_pentacles",
        "five_of_pentacles",
        "six_of_pentacles",
        "seven_of_pentacles",
        "eight_of_pentacles",
        "nine_of_pentacles",
        "ten_of_pentacles",
        "page_of_pentacles",
        "knight_of_pentacles",
        "queen_of_pentacles",
        "king_of_pentacles"
    )

    private val majorMeanings = mapOf(
        "The Fool" to Pair(
            "A fresh start is opening up. Trust curiosity, stay light on your feet, and let a new chapter teach you as you go.",
            "Impulsiveness or naivety could scatter your energy. Slow down just enough to notice what you are overlooking."
        ),
        "The Magician" to Pair(
            "You already have the tools you need. Focus your intention and turn ideas into visible action.",
            "Your focus may be split or your confidence may be performative. Reconnect with substance before you try to persuade anyone."
        ),
        "The High Priestess" to Pair(
            "Slow down and listen inward. The right answer is emerging through intuition rather than force.",
            "Inner wisdom is being drowned out by noise, secrecy, or avoidance. Sit with the truth you already suspect."
        ),
        "The Empress" to Pair(
            "Nurture what is growing in your life. Creativity, comfort, and care are your strengths today.",
            "Overgiving, creative block, or neglect of your own needs is throwing things off balance. Care for yourself as intentionally as you care for others."
        ),
        "The Emperor" to Pair(
            "Create structure around what matters. Clear boundaries and a steady plan will help things move.",
            "Control, rigidity, or fear of vulnerability may be limiting progress. Lead with steadiness, not domination."
        ),
        "The Hierophant" to Pair(
            "Tradition, mentorship, and trusted wisdom can steady this moment. Learn from what has endured.",
            "Outdated rules or pressure to conform may be muting your own judgment. Keep what is wise and release what is rigid."
        ),
        "The Lovers" to Pair(
            "Lead with honesty in relationships and choices. Alignment matters more than instant comfort.",
            "Misalignment or avoidance is clouding an important choice. Come back to your values before you commit."
        ),
        "The Chariot" to Pair(
            "Momentum is on your side. Commit to one direction and move with confidence.",
            "You may be pushing too hard or losing control of competing priorities. Regain direction before forcing another step."
        ),
        "Strength" to Pair(
            "Gentle persistence wins here. Meet stress with patience rather than pressure.",
            "Self-doubt or pent-up frustration needs softer handling. Courage right now looks like emotional steadiness."
        ),
        "The Hermit" to Pair(
            "A little solitude can bring clarity. Give yourself room to think before reacting.",
            "Isolation or overthinking may be turning insight into distance. Seek wisdom, but do not disappear from life."
        ),
        "Wheel of Fortune" to Pair(
            "A shift is underway. Stay adaptable and look for the opportunity inside change.",
            "Resistance to change or bad timing is making things feel stuck. Focus on what you can influence while the cycle turns."
        ),
        "Justice" to Pair(
            "Truth wants to be seen. Be fair, direct, and willing to own your part.",
            "Imbalance, denial, or consequences delayed are in play. Get honest now so the correction is cleaner later."
        ),
        "The Hanged Man" to Pair(
            "Pause before pushing ahead. A new perspective will save you wasted effort.",
            "Stagnation may be replacing surrender. If the pause has stopped teaching you, it may be time to reengage."
        ),
        "Death" to Pair(
            "Something is ending so something healthier can begin. Release what has run its course.",
            "Clinging to the old is stretching out a necessary ending. Letting go is the only way this transforms."
        ),
        "Temperance" to Pair(
            "Balance is the message. Blend rest, action, feeling, and logic instead of overdoing one side.",
            "Excess, impatience, or emotional swings are disrupting harmony. Return to moderation and integration."
        ),
        "The Devil" to Pair(
            "Notice habits that drain your power. Awareness is the first step toward freedom.",
            "You are starting to see the grip of fear, attachment, or self-sabotage. Name it clearly so it loses strength."
        ),
        "The Tower" to Pair(
            "Unexpected change can clear false foundations. Let the truth reorganize your next move.",
            "Tension is building because something unstable has not yet been addressed. A conscious reset may prevent a harsher break."
        ),
        "The Star" to Pair(
            "Hope is returning. Stay open to healing, inspiration, and gentler possibilities.",
            "Discouragement or disconnection may be dimming your sense of possibility. Rebuild trust in small, steady ways."
        ),
        "The Moon" to Pair(
            "Not everything is visible yet. Move carefully and trust your instincts while the fog lifts.",
            "Fear, confusion, or projection could be distorting what you see. Check your assumptions before reacting."
        ),
        "The Sun" to Pair(
            "Joy, confidence, and momentum are available. Let yourself be seen in a fuller way.",
            "Temporary doubt or burnout may be dimming your clarity. Light returns faster when you stop hiding what is true."
        ),
        "Judgement" to Pair(
            "You are being called to rise into a clearer version of yourself. Answer honestly.",
            "Avoiding accountability or the next calling can keep you in an outdated identity. Respond instead of postponing."
        ),
        "The World" to Pair(
            "A cycle is completing. Acknowledge how far you have come and step into what is next.",
            "Closure is available, but unfinished emotional threads may still need attention. Complete the lesson before rushing onward."
        )
    )

    private val deck: List<TarotCard> = orderedResourceNames.map { resourceName ->
        buildCard(resourceName)
    }

    fun getAllCards(): List<TarotCard> = deck

    fun getDeck(): List<TarotCard> = deck.shuffled()

    fun findCardByName(name: String): TarotCard? =
        deck.firstOrNull { it.name.equals(name, ignoreCase = true) }

    private fun buildCard(resourceName: String): TarotCard {
        val title = displayName(resourceName)
        val suit = suitFor(resourceName)
        val meanings = if (suit == "Major Arcana") {
            majorMeanings.getValue(title)
        } else {
            minorMeanings(resourceName, suit)
        }

        return TarotCard(
            name = title,
            suit = suit,
            imageRes = drawableId(resourceName),
            meaning = meanings.first,
            reversedMeaning = meanings.second
        )
    }

    private fun drawableId(resourceName: String): Int {
        return R.drawable::class.java.getField(resourceName).getInt(null)
    }

    private fun displayName(resourceName: String): String {
        return resourceName
            .split("_")
            .mapIndexed { index, token ->
                when {
                    token == "of" -> "of"
                    index > 0 && token == "and" -> "and"
                    else -> token.replaceFirstChar { ch ->
                        if (ch.isLowerCase()) ch.titlecase(Locale.US) else ch.toString()
                    }
                }
            }
            .joinToString(" ")
    }

    private fun suitFor(resourceName: String): String {
        return when {
            resourceName.endsWith("_wands") -> "Wands"
            resourceName.endsWith("_cups") -> "Cups"
            resourceName.endsWith("_swords") -> "Swords"
            resourceName.endsWith("_pentacles") -> "Pentacles"
            else -> "Major Arcana"
        }
    }

    private fun minorMeanings(resourceName: String, suit: String): Pair<String, String> {
        val rank = resourceName.substringBefore("_of_").replaceFirstChar { it.titlecase(Locale.US) }
        val suitTheme = when (suit) {
            "Wands" -> "energy, desire, and momentum"
            "Cups" -> "emotion, intuition, and connection"
            "Swords" -> "clarity, tension, and truth"
            else -> "work, resources, and steady growth"
        }
        val uprightLead = when (rank) {
            "Ace" -> "A new opening appears in $suitTheme."
            "Two" -> "A balancing act is taking shape in $suitTheme."
            "Three" -> "Growth, exchange, or momentum is building in $suitTheme."
            "Four" -> "Stability and pause matter more than speed in $suitTheme."
            "Five" -> "Conflict or strain is exposing what needs adjustment in $suitTheme."
            "Six" -> "Movement, support, or harmony is returning to $suitTheme."
            "Seven" -> "Assessment and conviction are being tested in $suitTheme."
            "Eight" -> "Momentum or pressure is accelerating in $suitTheme."
            "Nine" -> "The situation is maturing, and its true weight is visible in $suitTheme."
            "Ten" -> "A cycle is reaching completion in $suitTheme."
            "Page" -> "Curiosity and a new lesson are arriving through $suitTheme."
            "Knight" -> "Strong motion and pursuit are pushing $suitTheme forward."
            "Queen" -> "The mature, embodied side of $suitTheme is your advantage now."
            else -> "Mastery, leadership, and responsibility define this moment in $suitTheme."
        }
        val reversedLead = when (rank) {
            "Ace" -> "The opening is real, but hesitation or blockage is slowing it down."
            "Two" -> "Imbalance or indecision is making the situation harder than it needs to be."
            "Three" -> "Misalignment is interrupting natural progress."
            "Four" -> "Stagnation or overprotection may be replacing healthy stability."
            "Five" -> "The friction is lingering because the deeper issue has not been addressed."
            "Six" -> "Progress is possible, but it may be delayed, uneven, or difficult to trust."
            "Seven" -> "Doubt, scattered effort, or weak boundaries are reducing your leverage."
            "Eight" -> "The pace may be chaotic, blocked, or turned inward."
            "Nine" -> "Anxiety, fatigue, or over-identification with the pressure is clouding judgment."
            "Ten" -> "Completion is near, but the load may be too heavy or the ending resisted."
            "Page" -> "Inexperience, distraction, or emotional immaturity is asking for more grounding."
            "Knight" -> "The drive is present, but it risks becoming reckless, stalled, or misdirected."
            "Queen" -> "Your inner steadiness is being interrupted by self-doubt or emotional imbalance."
            else -> "Authority is slipping into rigidity, passivity, or overcontrol."
        }
        val upright = "$uprightLead Let the lesson of the $rank of $suit guide your next move with awareness."
        val reversed = "$reversedLead The reversed $rank of $suit asks for reflection before action."
        return upright to reversed
    }
}
