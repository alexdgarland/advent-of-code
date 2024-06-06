package personal.alexdgarland.advent_of_code.day07

import personal.alexdgarland.advent_of_code.Input

data class ParsedInputRow(val cardSigns: String, val bid: Int)

data class HandInfo(val hand: Scoring.IdentifiedHand, val bid: Int)

object InputParsing {

    fun parseInput(input: List<String>): List<ParsedInputRow> {
        return input.map { line ->
            line.split(" ").let {
                ParsedInputRow(it.first(), it[1].toInt())
            }
        }
    }

}

object Scoring {

    enum class HandType(val rankingScore: Int) {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_A_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1)
    }

    enum class CardType(val rankingScore: Int) {
        ACE(14),
        KING(13),
        QUEEN(12),
        JACK(11),
        TEN(10),
        NINE(9),
        EIGHT(8),
        SEVEN(7),
        SIX(6),
        FIVE(5),
        FOUR(4),
        THREE(3),
        TWO(2)
    }

    private val charToCardMap = mapOf(
        'A' to CardType.ACE,
        'K' to CardType.KING,
        'Q' to CardType.QUEEN,
        'J' to CardType.JACK,
        'T' to CardType.TEN,
        '9' to CardType.NINE,
        '8' to CardType.EIGHT,
        '7' to CardType.SEVEN,
        '6' to CardType.SIX,
        '5' to CardType.FIVE,
        '4' to CardType.FOUR,
        '3' to CardType.THREE,
        '2' to CardType.TWO
    )

    private fun deriveHandType(cardSigns: String): HandType {
        val groupSizes = cardSigns.groupBy { it }.values.map{ it.size }.sortedDescending()
        val largestGroupSize = groupSizes.first()
        if (largestGroupSize == 5) {
            return HandType.FIVE_OF_A_KIND
        }
        if (largestGroupSize == 4) {
            return HandType.FOUR_OF_A_KIND
        }
        if (largestGroupSize == 1) {
            return HandType.HIGH_CARD
        }
        val secondLargestGroupSize = groupSizes[1]
        if (largestGroupSize == 3) {
            return if (secondLargestGroupSize == 2) {
                HandType.FULL_HOUSE
            } else {
                HandType.THREE_OF_A_KIND
            }
        }
        // Implicitly - if (largestGroupSize == 2) -
        // but need a "fall-through" statement otherwise the compiler complains,
        // even though we know that in practice it's an "exhaustive" match
        return if (secondLargestGroupSize == 2) {
            HandType.TWO_PAIR
        } else {
            HandType.ONE_PAIR
        }

    }

    data class IdentifiedHand(val type: HandType, val cards: List<CardType>)

    fun identifyHand(cardSigns: String): IdentifiedHand {
        return IdentifiedHand(
            type =  deriveHandType(cardSigns),
            cards =  cardSigns.map {
                charToCardMap[it]?: throw IllegalArgumentException("Invalid card-char \"$it\"")
            }
        )
    }

}

object Solution {

    fun run() {
        val input = Input.getLines("07").toList()

        val configs = InputParsing.parseInput(input)
        val hands = configs.map { cfg -> HandInfo(
            Scoring.identifyHand(cfg.cardSigns),
            cfg.bid
        )}
        val result = hands.sortedWith(
            compareBy<HandInfo> { it.hand.type.rankingScore }
                .thenBy { it.hand.cards[0].rankingScore }
                .thenBy { it.hand.cards[1].rankingScore }
                .thenBy { it.hand.cards[2].rankingScore }
                .thenBy { it.hand.cards[3].rankingScore }
                .thenBy { it.hand.cards[4].rankingScore }
        )
            .withIndex()
            .sumOf { it.value.bid * (it.index + 1) }

        println(" --- $result --- ")    // 251927063
    }

}
