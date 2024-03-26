package personal.alexdgarland.advent_of_code.day04

import personal.alexdgarland.advent_of_code.Input
import kotlin.math.pow

data class Card(val name: String, val winningNumbers: List<Int>, val ownedNumbers: List<Int>) {

    fun numberOfMatches(): Int {
        // This isn't operating at a scale where processing is likely to get very expensive,
        // but have decided to do a reasonably efficient anyway (more or less a merge-join)
        // rather than blat it with N^2.
        val winningNumbersSorted = winningNumbers.sorted()
        val ownedNumbersSorted = ownedNumbers.sorted()
        var count = 0
        var winnerIndex = 0
        var ownedIndex = 0
        while(winnerIndex < winningNumbersSorted.size && ownedIndex < ownedNumbersSorted.size) {
            val currentWinner = winningNumbersSorted[winnerIndex]
            val currentOwned = ownedNumbersSorted[ownedIndex]
            if(currentWinner == currentOwned) {
                count++
                winnerIndex++
                ownedIndex++
            }
            else if(currentWinner > currentOwned) {
                ownedIndex++
            }
            else {
                winnerIndex++
            }
        }
        return count
    }

    fun score(): Int {
        val numberOfMatches = numberOfMatches()
        return if(numberOfMatches == 0) 0 else 2.0.pow(numberOfMatches.toDouble() - 1).toInt()
    }

    companion object {

        private fun splitToInts(s: String): List<Int> {
            return s.split(" ").filter{ it.isNotBlank() }.map { it.toInt() }
        }

        fun fromString(str: String): Card {
            val split = str.split(":", "|")
            return Card(split[0], splitToInts(split[1]), splitToInts(split[2]))
        }

    }

}

fun numberOfCardsWon(cards: List<Card>): Int {
    val matchNumbers = cards.map{it.numberOfMatches()}
    val wonCardCounts = MutableList(cards.size) { 1 }

    for(i in cards.lastIndex -1 downTo 0) {
        val numberOfCardsToProcess = matchNumbers[i]
        var count = 0
        for(j in i+1 .. i+numberOfCardsToProcess) {
            count += wonCardCounts[j]
        }
        wonCardCounts[i] += count
    }

    return wonCardCounts.sum()
}


object Solution {

    fun run() {
        val cards = Input.getLines("04").toList().map { Card.fromString(it) }

        val scoringResult = cards.sumOf { it.score() }
        println(" --- $scoringResult --- ")

        val numberOfCardsResult = numberOfCardsWon(cards)
        println(" --- $numberOfCardsResult --- ")
    }

}
