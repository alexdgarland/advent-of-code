package personal.alexdgarland.advent_of_code.day05

import personal.alexdgarland.advent_of_code.Input

data class NumericRange(val start: Long, val length: Long) {
    val end = start + length - 1
}

data class RangeResolution(
    val destinationRange: NumericRange?,
    val remainingSourceRanges: List<NumericRange>
)

data class RangeMapEntry(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {

    private val offSet = destinationRangeStart - sourceRangeStart

    private val sourceRange = NumericRange(sourceRangeStart, rangeLength)

    fun resolveDestinationNumber(sourceNumber: Long): Long? {
        if(sourceNumber >= sourceRange.start && sourceNumber <= sourceRange.end) {
            return sourceNumber + offSet
        }
        return null
    }

    fun resolveRange(input: NumericRange): RangeResolution {
        val overlapStart = maxOf(sourceRange.start, input.start)
        val overlapEnd = minOf(sourceRange.end, input.end)

        if (overlapStart > overlapEnd) {
            return RangeResolution(null,  listOf(input))
        }

        val destinationRange = NumericRange(overlapStart + offSet, (overlapEnd - overlapStart) + 1)

        val remainingSourceRanges = mutableListOf<NumericRange>()
        if (input.start < overlapStart) {
            remainingSourceRanges.add(NumericRange(input.start, overlapStart - input.start))
        }
        if (input.end > overlapEnd) {
            remainingSourceRanges.add(NumericRange(overlapEnd + 1, input.end - overlapEnd))
        }

        return RangeResolution(destinationRange, remainingSourceRanges)
    }

    companion object {

        fun fromLine(line: String): RangeMapEntry {
            val split = line.split(" ")
            return RangeMapEntry(
                destinationRangeStart = split.first().toLong(),
                sourceRangeStart = split[1].toLong(),
                rangeLength = split[2].toLong())
        }

    }

}

data class RangeMap(val name: String, val entries: List<RangeMapEntry>) {

    fun resolveDestinationNumber(sourceNumber: Long): Long {
        return entries.firstNotNullOfOrNull { it.resolveDestinationNumber(sourceNumber) }?: sourceNumber
    }

    private data class ResolutionState(
        val unresolvedSourceRanges: List<NumericRange>,
        val resolvedDestinationRanges: List<NumericRange> = emptyList(),
    )

    private fun recurseRangeResolution(state: ResolutionState, remainingEntries: List<RangeMapEntry>): ResolutionState {
        if (remainingEntries.isEmpty()) {
            return state
        }
        val nextResults = state.unresolvedSourceRanges.map { remainingEntries.first().resolveRange(it) }
        val nextState = ResolutionState(
            unresolvedSourceRanges = nextResults.flatMap { it.remainingSourceRanges },
            resolvedDestinationRanges = state.resolvedDestinationRanges + nextResults.mapNotNull { it.destinationRange }
        )
        return recurseRangeResolution(nextState, remainingEntries.drop(1))
    }

    fun resolveDestinationRanges(sourceRanges: List<NumericRange>): List<NumericRange> {
        return recurseRangeResolution(ResolutionState(sourceRanges), entries)
            .let { it.unresolvedSourceRanges + it.resolvedDestinationRanges }
    }

}

data class SeedToLocationResolver(
    val seedToSoilRangeMap: RangeMap,
    val soilToFertilizerMap: RangeMap,
    val fertilizerToWaterMap: RangeMap,
    val waterToLightMap: RangeMap,
    val lightToTemperatureMap: RangeMap,
    val temperatureToHumidityMap: RangeMap,
    val humidityToLocationMap: RangeMap
) {

    data class RangeNumber(val value: Long) {
        fun resolveWith(rangeMap: RangeMap): RangeNumber {
            return RangeNumber(rangeMap.resolveDestinationNumber(value))
        }
    }

    fun resolveLocation(seedNumber: Long): Long {
        return RangeNumber(seedNumber)
            .resolveWith(seedToSoilRangeMap)
            .resolveWith(soilToFertilizerMap)
            .resolveWith(fertilizerToWaterMap)
            .resolveWith(waterToLightMap)
            .resolveWith(lightToTemperatureMap)
            .resolveWith(temperatureToHumidityMap)
            .resolveWith(humidityToLocationMap)
            .value
    }

    data class NumericRanges(val ranges: List<NumericRange>) {
        fun resolveWith(rangeMap: RangeMap): NumericRanges {
            return NumericRanges(rangeMap.resolveDestinationRanges(this.ranges))
        }
    }

    fun resolveLocations(seedNumberRanges: List<NumericRange>): List<NumericRange> {
        return NumericRanges(seedNumberRanges)
            .resolveWith(seedToSoilRangeMap)
            .resolveWith(soilToFertilizerMap)
            .resolveWith(fertilizerToWaterMap)
            .resolveWith(waterToLightMap)
            .resolveWith(lightToTemperatureMap)
            .resolveWith(temperatureToHumidityMap)
            .resolveWith(humidityToLocationMap)
            .ranges
    }

    companion object {

        fun createFrom(input: List<String>): SeedToLocationResolver {
            val rangeMaps = HashMap<String, RangeMap>()
            var currentName: String? = null
            var currentListBuilder = mutableListOf<RangeMapEntry>()
            input.forEach { line ->
                if (line.isNotEmpty()) {
                    if(line.first().isDigit()) {
                        currentListBuilder.add(RangeMapEntry.fromLine(line))
                    }
                    else {
                        currentName?.let { name -> rangeMaps[name] = RangeMap(name, currentListBuilder.toList()) }
                        currentName = line.split(" ").first()
                        currentListBuilder = mutableListOf()
                    }
                }
            }
            currentName?.let { name -> rangeMaps[name] = RangeMap(name, currentListBuilder) }

            return SeedToLocationResolver(
                seedToSoilRangeMap = rangeMaps["seed-to-soil"]!!,
                soilToFertilizerMap = rangeMaps["soil-to-fertilizer"]!!,
                fertilizerToWaterMap = rangeMaps["fertilizer-to-water"]!!,
                waterToLightMap = rangeMaps["water-to-light"]!!,
                lightToTemperatureMap = rangeMaps["light-to-temperature"]!!,
                temperatureToHumidityMap = rangeMaps["temperature-to-humidity"]!!,
                humidityToLocationMap = rangeMaps["humidity-to-location"]!!
            )

        }

    }
}


object Solution {

    private fun part1(seedNumberData: List<Long>, resolver: SeedToLocationResolver) {
        val result = seedNumberData.minOfOrNull { resolver.resolveLocation(it) }
        println(" --- $result --- ")    // 175622908
    }

    private fun part2(seedNumberData: List<Long>, resolver: SeedToLocationResolver) {
        val seedNumberRanges = seedNumberData
            .windowed(2, 2)
            .map { NumericRange(it[0], it[1])}

        val result = resolver.resolveLocations(seedNumberRanges).minOfOrNull { it.start }

        println(" --- $result --- ")    // 5200543
    }

    fun run() {
        val input = Input.getLines("05").toList()

        val seedNumberData = input
            .first()
            .split(" ")
            .drop(1)
            .map { it.toLong() }

        val resolver = SeedToLocationResolver.createFrom(input.subList(2, input.size))

        part1(seedNumberData, resolver)
        part2(seedNumberData, resolver)
    }

}
