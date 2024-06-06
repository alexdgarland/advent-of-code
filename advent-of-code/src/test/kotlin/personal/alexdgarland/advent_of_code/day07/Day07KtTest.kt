package personal.alexdgarland.advent_of_code.day07

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

import personal.alexdgarland.advent_of_code.day07.Scoring.identifyHand
import personal.alexdgarland.advent_of_code.day07.Scoring.IdentifiedHand
import personal.alexdgarland.advent_of_code.day07.Scoring.HandType
import personal.alexdgarland.advent_of_code.day07.Scoring.CardType

class Day07KtTest {

    @Nested
    inner class HandIdentificationTests {

        /***
         * Five of a kind, where all five cards have the same label: AAAAA
         */
        @Test
        fun `Five of a kind`() {
            val identifiedHand = identifyHand("AAAAA")
            val expected = IdentifiedHand(
                HandType.FIVE_OF_A_KIND,
                listOf(
                    CardType.ACE,
                    CardType.ACE,
                    CardType.ACE,
                    CardType.ACE,
                    CardType.ACE
                )
            )
            assertEquals(expected, identifiedHand)
        }

        /***
         * Four of a kind, where four cards have the same label and one card has a different label: AA8AA
         */
        @Test
        fun `Four of a kind`() {
            val identifiedHand = identifyHand("AA8AA")
            val expected = IdentifiedHand(
                HandType.FOUR_OF_A_KIND,
                listOf(
                    CardType.ACE,
                    CardType.ACE,
                    CardType.EIGHT,
                    CardType.ACE,
                    CardType.ACE
                )
            )
            assertEquals(expected, identifiedHand)
        }


        /***
         * Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
         */
        @Test
        fun `Full house`() {
            val identifiedHand = identifyHand("23332")
            val expected = IdentifiedHand(
                HandType.FULL_HOUSE,
                listOf(
                    CardType.TWO,
                    CardType.THREE,
                    CardType.THREE,
                    CardType.THREE,
                    CardType.TWO
                )
            )
            assertEquals(expected, identifiedHand)
        }

        /***
         * Three of a kind, where three cards have the same label,
         * and the remaining two cards are each different from any other card in the hand: TTT98
         */
        @Test
        fun `Three of a kind`() {
            val identifiedHand = identifyHand("TTT98")
            val expected = IdentifiedHand(
                HandType.THREE_OF_A_KIND,
                listOf(
                    CardType.TEN,
                    CardType.TEN,
                    CardType.TEN,
                    CardType.NINE,
                    CardType.EIGHT
                )
            )
            assertEquals(expected, identifiedHand)
        }

        /***
         * Two pair, where two cards share one label, two other cards share a second label,
         * and the remaining card has a third label: 23432
         */
        @Test
        fun `Two pair`() {
            val identifiedHand = identifyHand("23432")
            val expected = IdentifiedHand(
                HandType.TWO_PAIR,
                listOf(
                    CardType.TWO,
                    CardType.THREE,
                    CardType.FOUR,
                    CardType.THREE,
                    CardType.TWO
                )
            )
            assertEquals(expected, identifiedHand)
        }

        /***
         * One pair, where two cards share one label,
         * and the other three cards have a different label from the pair and each other: A23A4
         */
        @Test
        fun `One pair`() {
            val identifiedHand = identifyHand("A23A4")
            val expected = IdentifiedHand(
                HandType.ONE_PAIR,
                listOf(
                    CardType.ACE,
                    CardType.TWO,
                    CardType.THREE,
                    CardType.ACE,
                    CardType.FOUR
                )
            )
            assertEquals(expected, identifiedHand)
        }

        /***
         * High card, where all cards' labels are distinct: 23456
         */
        @Test
        fun `High card`() {
            val identifiedHand = identifyHand("23456")
            val expected = IdentifiedHand(
                HandType.HIGH_CARD,
                listOf(
                    CardType.TWO,
                    CardType.THREE,
                    CardType.FOUR,
                    CardType.FIVE,
                    CardType.SIX
                )
            )
            assertEquals(expected, identifiedHand)
        }

    }


}