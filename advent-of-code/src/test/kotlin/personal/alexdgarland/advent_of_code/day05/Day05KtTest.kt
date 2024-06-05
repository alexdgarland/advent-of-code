package personal.alexdgarland.advent_of_code.day05

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day05KtTest {

    @Nested
    inner class RangeMapEntryTests {

        private val rangeMapEntry = RangeMapEntry(1000, 50, 20)

        @Nested
        inner class NumberResolutionTests {

            @Test
            fun `rangeMapEntry successfully resolves number within source range`() {
                val resolved = rangeMapEntry.resolveDestinationNumber(53)
                assertEquals(1003, resolved)
            }

            @Test
            fun `rangeMapEntry returns null for number outside source range`() {
                val resolved = rangeMapEntry.resolveDestinationNumber(73)
                assertEquals(null, resolved)
            }

        }

        @Nested
        inner class RangeResolutionTests {

            private fun runTest(inputRange: NumericRange, expectedResolution: RangeResolution) {
                val actualResolution = rangeMapEntry.resolveRange(inputRange)
                assertEquals(expectedResolution, actualResolution)
            }

            private fun testNonOverlappingRange(inputRange: NumericRange) {
                runTest(
                    inputRange,
                    expectedResolution = RangeResolution(
                        destinationRange = null,
                        remainingSourceRanges = listOf(inputRange)
                    )
                )
            }

            @Test
            fun `Whole input range lower than map source range results in null destination range, full input range returned as unresolved`() {
                testNonOverlappingRange(NumericRange(5, 10))
            }

            @Test
            fun `Whole input range higher than map source range results in null destination range, full input range returned as unresolved`() {
                testNonOverlappingRange(NumericRange(200, 10))
            }

            @Test
            fun `Input range exactly matching map source range results in fully mapped destination, no unresolved`() {
                runTest(
                    inputRange = NumericRange(50, 20),
                    expectedResolution = RangeResolution(
                        destinationRange = NumericRange(1000, 20),
                        remainingSourceRanges = emptyList()
                    )
                )
            }

            @Test
            fun `Input range within map source range results in fully mapped destination, no unresolved`() {
                runTest(
                    inputRange = NumericRange(55, 10),
                    expectedResolution = RangeResolution(
                        destinationRange = NumericRange(1005, 10),
                        remainingSourceRanges = emptyList()
                    )
                )
            }

            @Test
            fun `Input range with same start as map source range and input is shorter results in fully mapped destination, no unresolved`() {
                runTest(
                    inputRange = NumericRange(50, 10),
                    expectedResolution = RangeResolution(
                        destinationRange = NumericRange(1000, 10),
                        remainingSourceRanges = emptyList()
                    )
                )
            }


            @Test
            fun `Input range with same end as map source range and input is shorter results in fully mapped destination, no unresolved`() {
                runTest(
                    inputRange = NumericRange(60, 10),
                    expectedResolution = RangeResolution(
                        destinationRange = NumericRange(1010, 10),
                        remainingSourceRanges = emptyList()
                    )
                )
            }

            @Test
            fun `Start of (but not all) input range overlaps with map source range results in start of range destination-mapped, end unresolved`() {
                runTest(
                    inputRange = NumericRange(60, 20),
                    expectedResolution = RangeResolution(
                        destinationRange = NumericRange(1010, 10),
                        remainingSourceRanges = listOf(NumericRange(70, 10))
                    )
                )
            }

            @Test
            fun `End of (but not all) input range overlaps with map source range results in end of range destination-mapped, start unresolved`() {
                runTest(
                    inputRange = NumericRange(40, 20),
                    expectedResolution = RangeResolution(
                        destinationRange = NumericRange(1000, 10),
                        remainingSourceRanges = listOf(NumericRange(40, 10))
                    )
                )
            }

            @Test
            fun `Middle of input range (not start, end) covered by map source range results in middle destination-mapped, both start and end unresolved`() {
                runTest(
                    inputRange = NumericRange(40, 40),
                    expectedResolution = RangeResolution(
                        destinationRange = NumericRange(1000, 20),
                        remainingSourceRanges = listOf(
                            NumericRange(40, 10),
                            NumericRange(70, 10)
                        )
                    )
                )
            }

        }

    }

    @Nested
    inner class RangeMapTests {

        private val rangeMap = RangeMap(
            "test range map",
            listOf(
                RangeMapEntry(1000, 50, 20),
                RangeMapEntry(2000, 100, 20),
                RangeMapEntry(3000, 150, 20)
            )
        )

        @Nested
        inner class NumberResolutionTests {

            @Test
            fun `rangeMap successfully resolve number when first entry matches`() {
                val resolved = rangeMap.resolveDestinationNumber(53)
                assertEquals(1003, resolved)
            }

            @Test
            fun `rangeMap successfully resolve number when later entry matches`() {
                val resolved = rangeMap.resolveDestinationNumber(153)
                assertEquals(3003, resolved)
            }

            @Test
            fun `rangeMap returns original number when no entry matches`() {
                val resolved = rangeMap.resolveDestinationNumber(203)
                assertEquals(203, resolved)
            }

        }

        @Nested
        inner class RangeResolutionTests {

            private fun assertSameElements(expectedList: List<NumericRange>, actualList: List<NumericRange>) {
                assertEquals(expectedList.sortedBy { it.start }, actualList.sortedBy { it.start })
            }

            @Test
            fun `rangeMap successfully resolves single input range which is fully covered by a rangeMapEntry`() {
                val resolved = rangeMap.resolveDestinationRanges(
                    listOf(
                        NumericRange(55, 10)
                    )
                )
                val expectedList = listOf(
                    NumericRange(1005, 10)
                )
                assertSameElements(expectedList, resolved)
            }

            @Test
            fun `rangeMap successfully resolves single input range which is partially covered by a rangeMapEntry`() {
                val resolved = rangeMap.resolveDestinationRanges(
                    listOf(
                        NumericRange(55, 40)
                    )
                )
                val expectedList = listOf(
                    NumericRange(1005, 15), // Resolved by first rangeMapEntry
                    NumericRange(70, 25)    // Unresolved and passed through
                )
                assertSameElements(expectedList, resolved)
            }

            @Test
            fun `rangeMap successfully resolves single input range which is covered only in the middle by a rangeMapEntry`() {
                val resolved = rangeMap.resolveDestinationRanges(
                    listOf(
                        NumericRange(40, 55)
                    )
                )
                val expectedList = listOf(
                    NumericRange(40, 10),   // Unresolved and passed through
                    NumericRange(1000, 20), // Resolved by first rangeMapEntry
                    NumericRange(70, 25)    // Unresolved and passed through
                )
                assertSameElements(expectedList, resolved)
            }

            @Test
            fun `rangeMap successfully resolves single input range which spans more than one rangeMapEntry`() {
                val resolved = rangeMap.resolveDestinationRanges(
                    listOf(
                        NumericRange(40, 160)
                    )
                )
                val expectedList = listOf(
                    NumericRange(40, 10),   // Unresolved and passed through
                    NumericRange(1000, 20), // Resolved by first rangeMapEntry
                    NumericRange(70, 30),   // Unresolved and passed through
                    NumericRange(2000, 20), // Resolved by second rangeMapEntry
                    NumericRange(120, 30),  // Unresolved and passed through
                    NumericRange(3000, 20), // Resolved by third rangeMapEntry
                    NumericRange(170, 30)   // Unresolved and passed through
                )
                assertSameElements(expectedList, resolved)
            }

            @Test
            fun `rangeMap successfully resolves multiple input ranges`() {
                val resolved = rangeMap.resolveDestinationRanges(
                    listOf(
                        NumericRange(40, 35),
                        NumericRange(90, 40)
                    )
                )
                val expectedList = listOf(
                    // First input range
                    NumericRange(40, 10),   // Unresolved and passed through
                    NumericRange(1000, 20), // Resolved by first rangeMapEntry
                    NumericRange(70, 5),    // Unresolved and passed through
                    // Second input range
                    NumericRange(90, 10),   // Unresolved and passed through
                    NumericRange(2000, 20), // Resolved by second rangeMapEntry
                    NumericRange(120, 10)   // Unresolved and passed through
                )
                assertSameElements(expectedList, resolved)
            }

        }

    }

}
