package personal.alexdgarland.advent_of_code.day05

import personal.alexdgarland.advent_of_code.Input

class Day05SolutionRunner(private val seedNumberData: List<Long>, private val resolver: SeedToLocationResolver) {

    fun part1() {
        val result = seedNumberData.minOfOrNull { resolver.resolveLocation(it) }
        println(" --- $result --- ")    // 175622908
    }

    fun part2() {
        val seedNumberRanges = seedNumberData
            .windowed(2, 2)
            .map { NumericRange(it[0], it[1])}
        val result = resolver.resolveLocations(seedNumberRanges).minOfOrNull { it.start }
        println(" --- $result --- ")    // 5200543
    }

}

object Solution {

    fun run() {
        val input = Input.getLines("05").toList()
        val parser = InputParser(input)
        val runner = Day05SolutionRunner(parser.seedNumberData, parser.createResolver())
        runner.part1()
        runner.part2()
    }

}
