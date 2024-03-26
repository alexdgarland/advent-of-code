package personal.alexdgarland.advent_of_code.day03

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03KtTest {

    val exampleGrid = listOf(
        "467..114..",
        "...*......",
        "..35..633.",
        "......#...",
        "617*......",
        ".....+.58.",
        "..592.....",
        "......755.",
        "...$.*....",
        ".664.598.."
    )

    private val expectedPartNumbers = listOf(
        PartNumberInformation(467, mutableSetOf(Coordinate(1, 3))),
        PartNumberInformation(35, mutableSetOf(Coordinate(1, 3))),
        PartNumberInformation(633, mutableSetOf<Coordinate>()),
        PartNumberInformation(617, mutableSetOf<Coordinate>()),
        PartNumberInformation(592, mutableSetOf<Coordinate>()),
        PartNumberInformation(755, mutableSetOf(Coordinate(8, 5))),
        PartNumberInformation(664, mutableSetOf<Coordinate>()),
        PartNumberInformation(598, mutableSetOf(Coordinate(8, 5)))
    )

    @Test
    fun checkGetPartNumbersAgainstExampleGrid() {
        assertEquals(expectedPartNumbers, getPartNumbersFromGrid(exampleGrid))
    }

}