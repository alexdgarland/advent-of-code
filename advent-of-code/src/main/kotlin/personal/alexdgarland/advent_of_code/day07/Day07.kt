package personal.alexdgarland.advent_of_code.day07

import personal.alexdgarland.advent_of_code.Input

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
    TWO(2),
    JOKER( 1)
}

data class IdentifiedHand(val type: HandType, val cards: List<CardType>)

/***
 * Shared logic for hand identification across both parts.
 */
abstract class HandIdentifier {

    abstract fun jCardType(): CardType

    private fun charToCardMap(): Map<Char, CardType> = mapOf(
        'A' to CardType.ACE,
        'K' to CardType.KING,
        'Q' to CardType.QUEEN,
        'J' to jCardType(),
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

    protected data class GroupSizes(val firstLargest: Int, val secondLargest: Int?)

    protected abstract fun getGroupSizes(cards: List<CardType>): GroupSizes

    private fun deriveHandType(cards: List<CardType>): HandType {
        val groupSizes = getGroupSizes(cards)

        if (groupSizes.firstLargest == 5) {
            return HandType.FIVE_OF_A_KIND
        }
        if (groupSizes.secondLargest == null) {
            throw IllegalArgumentException(
                "It should not be possible to have no second group when first group size is less than 5"
            )
        }
        if (groupSizes.firstLargest == 4) {
            return HandType.FOUR_OF_A_KIND
        }
        if (groupSizes.firstLargest == 1) {
            return HandType.HIGH_CARD
        }

        if (groupSizes.firstLargest == 3) {
            return if (groupSizes.secondLargest == 2) {
                HandType.FULL_HOUSE
            } else {
                HandType.THREE_OF_A_KIND
            }
        }
        // Implicitly - if (largestGroupSize == 2) -
        // but need a "fall-through" statement otherwise the compiler complains,
        // even though we know that in practice it's an "exhaustive" match
        return if (groupSizes.secondLargest == 2) {
            HandType.TWO_PAIR
        } else {
            HandType.ONE_PAIR
        }
    }

    fun identifyHand(cardSigns: String): IdentifiedHand {
        val charToCardMap = charToCardMap()
        val cards = cardSigns.map {
            charToCardMap[it] ?: throw IllegalArgumentException("Invalid card-char \"$it\"")
        }
        val type = deriveHandType(cards)
        return IdentifiedHand(type, cards)
    }

}

/***
 * Implementation for part 1, where all cards are treated the same and 'J' -> Jack card.
 */
class Part1HandIdentifier: HandIdentifier() {

    override fun jCardType(): CardType = CardType.JACK

    override fun getGroupSizes(cards: List<CardType>): GroupSizes {
        val groupSizes = cards.groupBy { it }.values.map{ it.size }.sortedDescending()
        return GroupSizes(
            groupSizes.first(),
            groupSizes.getOrNull(1)
        )
    }

}

/***
 * Implementation for part 2, where 'J' -> Joker card which has special logic.
 *
 * The change here turned out to be fairly straightforward and worked straight away once the refactor was done,
 * so have not bothered adding additional unit tests - am being very pragmatic with these given that
 *
 */
class Part2HandIdentifier: HandIdentifier() {

    override fun jCardType(): CardType = CardType.JOKER

    /**
     * This logic could technically be used for both versions (put on the abstract base class,
     * or even make it a simple class/ function which takes jCardType as a parameter) -
     * the part 1 impl simply will never see Joker cards, so will bypass all the "special" logic here -
     * but I think separating it provides some clarity.
     */
    override fun getGroupSizes(cards: List<CardType>): GroupSizes {
        val (jokers, nonJokers) = cards.partition { it == CardType.JOKER }

        // Do the initial grouping excluding any Joker cards
        val nonJokerGroupSizes = nonJokers.groupBy { it }.values.map{ it.size }.sortedDescending()

        return GroupSizes(
            // Add the number of jokers back to whatever the largest non-Joker group is -
            // this is how we use them to get the highest possible hand
            firstLargest = nonJokerGroupSizes.getOrElse(0) { _ -> 0 } + jokers.size,
            secondLargest = nonJokerGroupSizes.getOrNull(1)
        )
    }

}

object Solution {

    data class ParsedInputRow(val cardSigns: String, val bid: Int)

    private fun parseInput(input: List<String>): List<ParsedInputRow> {
        return input.map { line ->
            line.split(" ").let {
                ParsedInputRow(it.first(), it[1].toInt())
            }
        }
    }

    data class HandInfo(val hand: IdentifiedHand, val bid: Int)

    private fun getResult(parsedInputRows: List<ParsedInputRow>, handIdentifier: HandIdentifier): Int {

        val hands = parsedInputRows.map { cfg -> HandInfo(
            handIdentifier.identifyHand(cfg.cardSigns),
            cfg.bid
        )}

        return hands.sortedWith(
            compareBy<HandInfo> { it.hand.type.rankingScore }
                .thenBy { it.hand.cards[0].rankingScore }
                .thenBy { it.hand.cards[1].rankingScore }
                .thenBy { it.hand.cards[2].rankingScore }
                .thenBy { it.hand.cards[3].rankingScore }
                .thenBy { it.hand.cards[4].rankingScore }
        )
            .withIndex()
            .sumOf { it.value.bid * (it.index + 1) }
    }

    private fun part1(parsedInputRows: List<ParsedInputRow>) {
        val result = getResult(parsedInputRows, Part1HandIdentifier())
        println(" --- Part 1: $result --- ")    // 251927063
    }

    private fun part2(parsedInputRows: List<ParsedInputRow>) {
        val result = getResult(parsedInputRows, Part2HandIdentifier())
        println(" --- Part 2: $result --- ")    // 255632664
    }

    fun run() {
        val input = Input.getLines("07").toList()
        val parsedInputRows = parseInput(input)
        part1(parsedInputRows)
        part2(parsedInputRows)
    }

}
