package personal.alexdgarland.advent_of_code.day01

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class Day01KtTest {

    companion object {
        @JvmStatic
        fun provideExamplesPartOne(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("1abc2", 12),
                Arguments.of("pqr3stu8vwx", 38),
                Arguments.of("a1b2c3d4e5f", 15),
                Arguments.of("treb7uchet", 77)
            )
        }

        @JvmStatic
        fun provideExamplesPartTwo(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("two1nine", 29),
                Arguments.of("eightwothree", 83),
                Arguments.of("abcone2threexyz", 13),
                Arguments.of("xtwone3four", 24),
                Arguments.of("4nineeightseven2", 42),
                Arguments.of("zoneight234", 14),
                Arguments.of("7pqrstsixteen", 76)
            )
        }

    }

    @ParameterizedTest
    @MethodSource("provideExamplesPartOne")
    fun checkExamplesPartOne(lineInput: String, expectedCalibrationValue: Int) {
        assertEquals(expectedCalibrationValue, extractCalibrationValue(lineInput))
    }

    @ParameterizedTest
    @MethodSource("provideExamplesPartTwo")
    fun checkExamplesPartTwo(lineInput: String, expectedCalibrationValue: Int) {
        assertEquals(expectedCalibrationValue, extractCalibrationValue(lineInput))
    }

}
