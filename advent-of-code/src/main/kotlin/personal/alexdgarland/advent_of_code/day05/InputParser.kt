package personal.alexdgarland.advent_of_code.day05

class InputParser(private val input: List<String>) {

    val seedNumberData = input
        .first()
        .split(" ")
        .drop(1)
        .map { it.toLong() }

    private fun createRangeMapEntry(line: String): RangeMapEntry {
        return line.split(" ").let {
            RangeMapEntry(
                destinationRangeStart = it.first().toLong(),
                sourceRangeStart = it[1].toLong(),
                rangeLength = it[2].toLong()
            )
        }
    }

    private fun buildRangeMaps(input: List<String>): Map<String, RangeMap> {
        val rangeMaps = HashMap<String, RangeMap>()
        var currentName: String? = null
        var currentListBuilder = mutableListOf<RangeMapEntry>()
        input.forEach { line ->
            if (line.isNotEmpty()) {
                if(line.first().isDigit()) {
                    currentListBuilder.add(createRangeMapEntry(line))
                }
                else {
                    currentName?.let { name -> rangeMaps[name] = RangeMap(name, currentListBuilder.toList()) }
                    currentName = line.split(" ").first()
                    currentListBuilder = mutableListOf()
                }
            }
        }
        currentName?.let { name -> rangeMaps[name] = RangeMap(name, currentListBuilder.toList()) }
        return rangeMaps
    }

    fun createResolver(): SeedToLocationResolver {
        return buildRangeMaps(input.subList(2, input.size))
            .let { rangeMaps ->
                SeedToLocationResolver(
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