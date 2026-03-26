package com.example.tarotreader.utils

import com.example.tarotreader.BuildConfig
import com.example.tarotreader.data.TarotSpread
import com.example.tarotreader.data.TarotSpreadCard
import com.example.tarotreader.data.TarotSpreadReading
import kotlin.random.Random

object TarotSpreadEngine {

    fun drawReading(spread: TarotSpread): TarotSpreadReading {
        val deck = TarotDeck.getDeck()
        val selectedCards = deck.take(spread.cardCount)
        val spreadCards = spread.positions.zip(selectedCards).map { (position, card) ->
            TarotSpreadCard(
                position = position,
                card = card,
                isReversed = Random.nextBoolean()
            )
        }
        val source = if (BuildConfig.TAROTAP_API_KEY.isBlank()) {
            "Offline spread"
        } else {
            "Offline spread (Tarotap key detected; live provider ready for endpoint hookup)"
        }

        return TarotSpreadReading(
            spread = spread,
            source = source,
            headline = spread.prompt,
            determination = buildDetermination(spread, spreadCards),
            cards = spreadCards
        )
    }

    fun buildPositionMeaning(spreadCard: TarotSpreadCard): String {
        val orientation = if (spreadCard.isReversed) "Reversed" else "Upright"
        return "${spreadCard.position} - $orientation\n${meaningFor(spreadCard)}"
    }

    fun titleFor(spreadCard: TarotSpreadCard): String {
        return if (spreadCard.isReversed) {
            "${spreadCard.card.name} (Reversed)"
        } else {
            spreadCard.card.name
        }
    }

    fun meaningFor(spreadCard: TarotSpreadCard): String {
        if (!spreadCard.isReversed) {
            return spreadCard.card.meaning
        }
        return spreadCard.card.reversedMeaning
            ?: "The energy of ${spreadCard.card.name} is turned inward or delayed, asking for reflection before action."
    }

    private fun buildDetermination(
        spread: TarotSpread,
        cards: List<TarotSpreadCard>
    ): String {
        if (cards.isEmpty()) return spread.prompt

        return when (spread) {
            TarotSpread.DAILY -> {
                val card = cards.first()
                val tone = if (card.isReversed) "Move slowly and stay reflective." else "Lean in and trust the invitation."
                "${titleFor(card)} sets the tone for today. ${meaningFor(card)} $tone"
            }
            TarotSpread.PAST_PRESENT_FUTURE -> {
                val past = cards[0]
                val present = cards[1]
                val future = cards[2]
                "What shaped this moment was ${titleFor(past)}. Right now ${titleFor(present)} is the strongest influence, and the path ahead points toward ${titleFor(future)}. The determination here is to learn from the past without repeating it, respond honestly to the present, and let the future unfold through one clear next step."
            }
            TarotSpread.LOVE -> {
                val you = cards[0]
                val them = cards[1]
                val next = cards[2]
                "Your emotional center is showing up as ${titleFor(you)}, while the connection around you carries the tone of ${titleFor(them)}. The best determination is to stop guessing and take the next step in the spirit of ${titleFor(next)}: honest, measured, and emotionally clear."
            }
            TarotSpread.CAREER -> {
                val path = cards[0]
                val challenge = cards[1]
                val opportunity = cards[2]
                "Your work path is currently shaped by ${titleFor(path)}, but the friction point is ${titleFor(challenge)}. The opening is ${titleFor(opportunity)}. The determination here is to work with what is already developing, address the real blocker directly, and move toward the opportunity with practical discipline."
            }
            TarotSpread.CELTIC_CROSS_LITE -> {
                val present = cards.getOrNull(0)?.let { titleFor(it) } ?: "the present"
                val challenge = cards.getOrNull(1)?.let { titleFor(it) } ?: "the challenge"
                val foundation = cards.getOrNull(2)?.let { titleFor(it) } ?: "the foundation"
                val guidance = cards.getOrNull(3)?.let { titleFor(it) } ?: "guidance"
                val outcome = cards.getOrNull(4)?.let { titleFor(it) } ?: "the outcome"
                "At the center of this situation is $present, crossed by $challenge. Underneath it all is $foundation, which explains why this pattern has taken hold. Guidance arrives through $guidance, and the likely result leans toward $outcome. The determination is to work from the root cause first, accept the challenge as useful information, and let the guidance shape the outcome rather than trying to force it."
            }
            TarotSpread.CELTIC_CROSS -> {
                val present = cards.getOrNull(0)?.let { titleFor(it) } ?: "the present"
                val challenge = cards.getOrNull(1)?.let { titleFor(it) } ?: "the challenge"
                val foundation = cards.getOrNull(2)?.let { titleFor(it) } ?: "the foundation"
                val recentPast = cards.getOrNull(3)?.let { titleFor(it) } ?: "the recent past"
                val consciousGoal = cards.getOrNull(4)?.let { titleFor(it) } ?: "the conscious goal"
                val nearFuture = cards.getOrNull(5)?.let { titleFor(it) } ?: "the near future"
                val self = cards.getOrNull(6)?.let { titleFor(it) } ?: "the self"
                val environment = cards.getOrNull(7)?.let { titleFor(it) } ?: "the environment"
                val hopesAndFears = cards.getOrNull(8)?.let { titleFor(it) } ?: "hopes and fears"
                val outcome = cards.getOrNull(9)?.let { titleFor(it) } ?: "the outcome"
                "The heart of the matter stands in $present and is being tested by $challenge. Its deeper root is $foundation, while $recentPast shows what has just been moving out of frame. You are consciously reaching toward $consciousGoal, and $nearFuture suggests the next shift already approaching. Your current posture is $self, while the outer field around you reflects $environment. $hopesAndFears reveals the emotional charge around this question, and $outcome shows the direction things are heading if the pattern continues. The determination is to stop treating the challenge as separate from the lesson, ground yourself in the foundation of the situation, and move toward the conscious goal with awareness of both your inner stance and the surrounding influences."
            }
        }
    }
}
