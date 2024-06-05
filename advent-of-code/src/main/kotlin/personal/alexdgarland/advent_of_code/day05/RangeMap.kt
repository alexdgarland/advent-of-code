package personal.alexdgarland.advent_of_code.day05

data class RangeMap(val name: String, val entries: List<RangeMapEntry>) {

    fun resolveDestinationNumber(sourceNumber: Long): Long {
        return entries.firstNotNullOfOrNull { it.resolveDestinationNumber(sourceNumber) }?: sourceNumber
    }

    private data class ResolutionState(
        val unresolvedSourceRanges: List<NumericRange>,
        val resolvedDestinationRanges: List<NumericRange> = emptyList(),
    )

    private fun resolve(state: ResolutionState, remainingEntries: List<RangeMapEntry>): ResolutionState {
        if (remainingEntries.isEmpty()) {
            return state
        }
        val nextResults = state.unresolvedSourceRanges.map { remainingEntries.first().resolveRange(it) }
        val nextState = ResolutionState(
            unresolvedSourceRanges = nextResults.flatMap { it.remainingSourceRanges },
            resolvedDestinationRanges = state.resolvedDestinationRanges + nextResults.mapNotNull { it.destinationRange }
        )
        return resolve(nextState, remainingEntries.drop(1))
    }

    fun resolveDestinationRanges(sourceRanges: List<NumericRange>): List<NumericRange> {
        return resolve(ResolutionState(sourceRanges), entries)
            .let { it.unresolvedSourceRanges + it.resolvedDestinationRanges }
    }

}