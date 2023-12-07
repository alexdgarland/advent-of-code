package personal.alexdgarland.advent_of_code.day01

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class Day01KtTest {

    companion object {
        @JvmStatic
        fun provideExamples(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("1abc2", 12),
                Arguments.of("pqr3stu8vwx", 38),
                Arguments.of("a1b2c3d4e5f", 15),
                Arguments.of("treb7uchet", 77)
            )
        }

    }

    @ParameterizedTest
    @MethodSource("provideExamples")
    fun checkExamples(lineInput: String, expectedCalibrationValue: Int) {
        assertEquals(expectedCalibrationValue, extractCalibrationValue(lineInput))
    }

}
