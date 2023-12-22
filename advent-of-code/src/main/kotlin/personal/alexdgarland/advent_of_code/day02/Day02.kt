package personal.alexdgarland.advent_of_code.day02

import personal.alexdgarland.advent_of_code.Input
import kotlin.math.max

enum class CubeColor {
    RED, GREEN, BLUE
}

val BAG: Map<CubeColor, Int> = mapOf(
    CubeColor.RED to 12,
    CubeColor.GREEN to 13,
    CubeColor.BLUE to 14
)

data class Game(val id: Int, val rounds: List<Map<CubeColor, Int>>) {

    private fun roundPossible(round: Map<CubeColor, Int>, bag: Map<CubeColor, Int>): Boolean {
        return round.entries.all { r -> bag.getOrElse(r.key){0} >= r.value }
    }

    fun possibleWith(bag: Map<CubeColor, Int>): Boolean {
        return rounds.all { r -> roundPossible(r, bag) }
    }

    fun minimumBag(): Map<CubeColor, Int> {
        return rounds.reduce { acc, next ->
            CubeColor.entries.toTypedArray()
                .associateWith { color ->
                    max(acc.getOrElse(color){0}, next.getOrElse(color){0})
                }
        }
    }

}



object Solution {

    private fun parseColorString(colorString: String): Pair<CubeColor, Int> {
        colorString.trim().split(" ").let {split ->
            val color = CubeColor.valueOf(split.last().uppercase())
            val count = split.first().toInt()
            return color to count
        }
    }

    private fun parseRoundString(roundString: String): Map<CubeColor, Int> {
        return roundString.split(",").associate { s -> parseColorString(s) }
    }

    fun parseGameLine(line: String): Game {
        line.split(":").let { split ->
            val id = split.first().split(" ").last().toInt()
            val rounds = split.last().split(";").map { s -> parseRoundString(s) }
            return Game(id, rounds)
        }
    }

    fun run() {
        val result = Input.getLines("02")
            .map { line -> parseGameLine(line) }
//            .filter { game -> game.possibleWith(BAG) }
//            .mapToInt { game -> game.id }
//            .sum()
            .mapToInt { game -> game.minimumBag().values.reduce {acc, next -> acc * next} }
            .sum()
        print(" --- $result --- ")
    }

}
