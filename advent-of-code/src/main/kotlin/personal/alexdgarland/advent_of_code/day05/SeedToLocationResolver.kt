package personal.alexdgarland.advent_of_code.day05

data class SeedToLocationResolver(
    val seedToSoilRangeMap: RangeMap,
    val soilToFertilizerMap: RangeMap,
    val fertilizerToWaterMap: RangeMap,
    val waterToLightMap: RangeMap,
    val lightToTemperatureMap: RangeMap,
    val temperatureToHumidityMap: RangeMap,
    val humidityToLocationMap: RangeMap
) {

    private abstract class FluentMapResolver<A>(val value: A) {
        abstract fun resolveWith(rangeMap: RangeMap): FluentMapResolver<A>
    }

    private fun <A> resolveChain(fluentMapResolver: FluentMapResolver<A>): A {
        return fluentMapResolver
            .resolveWith(seedToSoilRangeMap)
            .resolveWith(soilToFertilizerMap)
            .resolveWith(fertilizerToWaterMap)
            .resolveWith(waterToLightMap)
            .resolveWith(lightToTemperatureMap)
            .resolveWith(temperatureToHumidityMap)
            .resolveWith(humidityToLocationMap)
            .value
    }

    private class RangeNumber(number: Long): FluentMapResolver<Long>(number) {
        override fun resolveWith(rangeMap: RangeMap): RangeNumber {
            return RangeNumber(rangeMap.resolveDestinationNumber(value))
        }
    }

    fun resolveLocation(seedNumber: Long): Long {
        return resolveChain(RangeNumber(seedNumber))
    }

    private class NumericRanges(val ranges: List<NumericRange>): FluentMapResolver<List<NumericRange>>(ranges) {
        override fun resolveWith(rangeMap: RangeMap): NumericRanges {
            return NumericRanges(rangeMap.resolveDestinationRanges(this.ranges))
        }
    }

    fun resolveLocations(seedNumberRanges: List<NumericRange>): List<NumericRange> {
        return resolveChain(NumericRanges(seedNumberRanges))
    }

}
