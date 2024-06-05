package personal.alexdgarland.advent_of_code.day05

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

}
