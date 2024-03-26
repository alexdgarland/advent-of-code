package personal.alexdgarland.advent_of_code.day04

import personal.alexdgarland.advent_of_code.Input
import kotlin.math.pow

data class Card(val name: String, val winningNumbers: List<Int>, val ownedNumbers: List<Int>) {

    fun score(): Int {
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
        return if(count == 0) 0 else 2.0.pow(count.toDouble() - 1).toInt()
    }

    companion object {

        private fun splitToInts(s: String): List<Int> {
            return s.split(" ").filter{it.isNotBlank()}.map { it.toInt() }
        }

        fun fromString(str: String): Card {
            val split = str.split(":", "|")
            return Card(split[0], splitToInts(split[1]), splitToInts(split[2]))
        }

    }

}


object Solution {

    fun run() {
        val result = Input.getLines("04").toList()
            .sumOf { Card.fromString(it).score() }

        println(" --- $result --- ")
    }

}
