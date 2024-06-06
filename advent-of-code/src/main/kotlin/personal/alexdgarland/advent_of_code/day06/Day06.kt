package personal.alexdgarland.advent_of_code.day06

import personal.alexdgarland.advent_of_code.Input

data class RaceConfig(val availableTime: Long, val distanceToBeat: Long)

object RaceConfigs {

    private fun parseRowPart1(row: String): List<Long> {
        return row.split(" ").filter{it != ""}.drop(1).map{it.toLong()}
    }

    fun fromInputPart1(input: List<String>): List<RaceConfig> {
        val times = parseRowPart1(input[0])
        val distances = parseRowPart1(input[1])
        return times.zip(distances).map{RaceConfig(it.first, it.second)}
    }

    private fun parseRowPart2(row: String): Long {
        return row.split(" ").filter{it != ""}.drop(1).joinToString("").toLong()
    }

    fun fromInputPart2(input: List<String>): RaceConfig {
        return RaceConfig(
            availableTime = parseRowPart2(input[0]),
            distanceToBeat = parseRowPart2(input[1])
        )
    }

}

/***
 * Work out how many ways to beat the current best time.
 *
 * Naively, we go through every every possible split of the total time between charging time and moving time.
 *
 * T = total time
 * C (charging time) can run between 0 and T
 * M (movement time) is T - C (so ranging inversely between T and 0)
 * D will be distance travelled
 * B will be the existing time-to-beat
 *
 * Because C directly correlates (1-1) to speed once movement starts:
 *  distance travelled (D)
 *      = C * M
 *      = C * (T - C)
 *
 * So calculating D = C * (T - C) for 0 >= C <= T, then filtering that list for D > B should give us a correct result.
 *
 * A couple of possible optimizations:
 *
 *  - Very minor (and implemented as trivial) - if either C = 0 or M = 0 (C = T), we will travel zero distance,
 *      so we can in fact just run over the range 1 >= C <= (T - 1)
 *  - Significant if we run for very large numbers
 *      (but not implemented - perf was fine anyway even for the larger part 2) -
 *      any situation where M and C are both greater than the square root of B will let us beat B,
 *      so we can simply count the length of the middle stretch for which this is true (O(1) operation)
 *      and then do actual calculations only on the end sections where this does not hold.
 */
fun calculateNumberOfWaysToBeat(config: RaceConfig): Int {
    return (1 until config.availableTime).map { speedFromChargingTime ->
        val movementTime = config.availableTime - speedFromChargingTime
        speedFromChargingTime * movementTime
    }.count { it > config.distanceToBeat }
}

object Solution {

    fun run() {
        val input = Input.getLines("06").toList()

        val part1Configs = RaceConfigs.fromInputPart1(input)
        val part1Result = part1Configs.map { calculateNumberOfWaysToBeat(it) }.reduce(Int::times)
        println(" --- $part1Result --- ")   // 252000

        val part2SingleConfig = RaceConfigs.fromInputPart2(input)
        val part2Result = calculateNumberOfWaysToBeat(part2SingleConfig)
        println(" --- $part2Result --- ")   // 36992486
    }

}