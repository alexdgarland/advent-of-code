package personal.alexdgarland.advent_of_code.day04

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04KtTest {

    val cardStringLines = listOf(
        "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
        "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
        "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
        "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
        "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
        "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
    )

    val exampleCards = listOf(
        Card("Card 1", listOf(41, 48, 83, 86, 17), listOf(83, 86, 6, 31, 17,  9, 48, 53)),
        Card("Card 2", listOf(13, 32, 20, 16, 61), listOf(61, 30, 68, 82, 17, 32, 24, 19)),
        Card("Card 3", listOf(1, 21, 53, 59, 44), listOf(69, 82, 63, 72, 16, 21, 14,  1)),
        Card("Card 4", listOf(41, 92, 73, 84, 69), listOf(59, 84, 76, 51, 58,  5, 54, 83)),
        Card("Card 5", listOf(87, 83, 26, 28, 32), listOf(88, 30, 70, 12, 93, 22, 82, 36)),
        Card("Card 6", listOf(31, 18, 13, 56, 72), listOf(74, 77, 10, 23, 35, 67, 36, 11)),
    )

    val expectedScores = listOf(8, 2, 2, 1, 0, 0)

    @Test
    fun canParseCardsFromStringLines() {
        val parsed = cardStringLines.map{Card.fromString(it)}
        assertEquals(exampleCards, parsed)
    }

    @Test
    fun cardsScoreCorrectly() {
        val scores = exampleCards.map{it.score()}
        assertEquals(expectedScores, scores)
    }

    @Test
    fun wonCardsCountIsCorrect() {
        val wonCardCount = numberOfCardsWon(exampleCards)
        assertEquals(30, wonCardCount)
    }

}
