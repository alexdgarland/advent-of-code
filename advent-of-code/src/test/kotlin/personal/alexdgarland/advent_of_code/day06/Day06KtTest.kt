package personal.alexdgarland.advent_of_code.day06

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05KtTest {

    private val testExamples: List<RaceConfig> = RaceConfigs.fromInputPart1(
        listOf(
            "Time:      7  15   30",
            "Distance:  9  40  200"
        )
    )

    private val expectedNumbersOfWaysToBeat = listOf(4, 8, 9)

    @Test
    fun `test something`() {
        val calculated = testExamples.map { calculateNumberOfWaysToBeat(it) }
        assertEquals(expectedNumbersOfWaysToBeat, calculated)
    }

}