package personal.alexdgarland.advent_of_code.day02

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import personal.alexdgarland.advent_of_code.day02.Solution.parseGameLine
import java.util.stream.Stream

class Day02KtTest {

    companion object {
        @JvmStatic
        fun provideExampleInputLinesForParsing(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "Game 24: 12 green, 4 red, 2 blue; 8 green, 5 blue; 8 green, 2 blue, 2 red",
                    Game(24, listOf(
                        mapOf(CubeColor.GREEN to 12, CubeColor.RED to 4, CubeColor.BLUE to 2),
                        mapOf(CubeColor.GREEN to 8, CubeColor.BLUE to 5),
                        mapOf(CubeColor.GREEN to 8, CubeColor.BLUE to 2, CubeColor.RED to 2)
                    ))
                ),
                Arguments.of(
                    "Game 25: 3 red, 8 green; 1 red, 4 blue, 1 green; 6 green; 3 blue, 5 green, 3 red; 9 green, 3 blue, 5 red",
                    Game(25, listOf(
                        mapOf(CubeColor.RED to 3, CubeColor.GREEN to 8),
                        mapOf(CubeColor.RED to 1, CubeColor.BLUE to 4, CubeColor.GREEN to 1),
                        mapOf(CubeColor.GREEN to 6),
                        mapOf(CubeColor.BLUE to 3, CubeColor.GREEN to 5, CubeColor.RED to 3),
                        mapOf(CubeColor.GREEN to 9, CubeColor.BLUE to 3, CubeColor.RED to 5)
                    ))
                ),
                Arguments.of(
                    "Game 26: 1 green, 3 red, 2 blue; 7 red, 2 green, 11 blue; 7 blue, 4 red; 11 blue, 1 red, 1 green; 2 green, 10 blue, 1 red; 1 green, 7 red, 7 blue",
                    Game(26, listOf(
                        mapOf(CubeColor.GREEN to 1, CubeColor.RED to 3, CubeColor.BLUE to 2),
                        mapOf(CubeColor.RED to 7, CubeColor.GREEN to 2, CubeColor.BLUE to 11),
                        mapOf(CubeColor.BLUE to 7, CubeColor.RED to 4),
                        mapOf(CubeColor.BLUE to 11, CubeColor.RED to 1, CubeColor.GREEN to 1),
                        mapOf(CubeColor.GREEN to 2, CubeColor.BLUE to 10, CubeColor.RED to 1),
                        mapOf(CubeColor.GREEN to 1, CubeColor.RED to 7, CubeColor.BLUE to 7)
                    ))
                )
            )

        }

        @JvmStatic
        fun provideExamplesForPossibilityTest(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    mapOf<CubeColor, Int>(),
                    false,
                    "Empty bag"
                ),
                Arguments.of(
                    mapOf(CubeColor.GREEN to 12, CubeColor.BLUE to 5),
                    false,
                    "One color entirely missing"
                ),
                Arguments.of(
                    mapOf(CubeColor.GREEN to 12, CubeColor.RED to 4, CubeColor.BLUE to 5),
                    true,
                    "Each color at exact level required"
                ),
                Arguments.of(
                    mapOf(CubeColor.GREEN to 12, CubeColor.RED to 4, CubeColor.BLUE to 2),
                    false,
                    "Some colors don't have sufficient count"
                ),
                Arguments.of(
                    mapOf(CubeColor.GREEN to 100, CubeColor.RED to 100, CubeColor.BLUE to 100),
                    true,
                    "All colors have more than enough"
                ),
            )
        }

    }

    @ParameterizedTest
    @MethodSource("provideExampleInputLinesForParsing")
    fun checkLineParsing(lineInput: String, expectedGame: Game) {
        Assertions.assertEquals(expectedGame, parseGameLine(lineInput))
    }

    private val exampleGameForPossibilityChecks = Game(
        24,
        listOf(
            mapOf(CubeColor.GREEN to 12, CubeColor.RED to 4, CubeColor.BLUE to 2),
            mapOf(CubeColor.GREEN to 8, CubeColor.BLUE to 5),
            mapOf(CubeColor.GREEN to 8, CubeColor.BLUE to 2, CubeColor.RED to 2)
        )
    )

    @ParameterizedTest
    @MethodSource("provideExamplesForPossibilityTest")
    fun checkGamePossibility(bag: Map<CubeColor, Int>, shouldAllowGame: Boolean, testDescription: String) {
        Assertions.assertEquals(
            shouldAllowGame,
            exampleGameForPossibilityChecks.possibleWith(bag),
            "Failed test for [$testDescription]")
    }

}