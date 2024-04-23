package personal.alexdgarland.advent_of_code.day05

import personal.alexdgarland.advent_of_code.Input

data class RangeMapEntry(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Int) {

    fun resolveDestinationNumber(sourceNumber: Long): Long? {
        if(sourceNumber >= sourceRangeStart && sourceNumber <= sourceRangeStart + rangeLength) {
            return sourceNumber + (destinationRangeStart - sourceRangeStart)
        }
        return null
    }

    companion object {

        fun fromLine(line: String): RangeMapEntry {
            val split = line.split(" ")
            return RangeMapEntry(
                destinationRangeStart = split.first().toLong(),
                sourceRangeStart = split[1].toLong(),
                rangeLength = split[2].toInt())
        }

    }

}

// TODO - can we make this list NOT mutable when used? This is kind of a hack to build from the input file in the simplest possible way for now
data class RangeMap(val name: String, val entries: MutableList<RangeMapEntry>) {

    fun resolveDestinationNumber(sourceNumber: Long): Long {
        return entries.firstNotNullOfOrNull { it.resolveDestinationNumber(sourceNumber) }?: sourceNumber
    }

}

data class RangeNumber(val value: Long) {

    fun resolveWith(rangeMap: RangeMap): RangeNumber {
        return RangeNumber(rangeMap.resolveDestinationNumber(value))
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

    companion object {

        fun createFrom(input: List<String>): SeedToLocationResolver {
            val rangeMaps = HashMap<String, RangeMap>()
            var currentRangeMap: RangeMap? = null
            input.forEach { line ->
                if (line.isNotEmpty()) {
                    if(line.first().isDigit()) {
                        currentRangeMap?.entries?.add(RangeMapEntry.fromLine(line))
                    }
                    else {
                        val mapName = line.split(" ").first()
                        val newRangeMap = RangeMap(mapName, mutableListOf())
                        rangeMaps[mapName] = newRangeMap
                        currentRangeMap = newRangeMap
                    }
                }
            }

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

    fun run() {
        val input = Input.getLines("05").toList()

        val seedNumbers = input.first().split(" ").drop(1).map { it.toLong() }
        val resolver = SeedToLocationResolver.createFrom(input.subList(2, input.size))

        val result = seedNumbers.minOfOrNull { resolver.resolveLocation(it) }

        println(" --- $result --- ")

    }

}
