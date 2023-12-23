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

    private val expectedPartNumbers = listOf(467, 35, 633, 617, 592, 755, 664, 598)

    @Test
    fun checkGetPartNumbersAgainstExampleGrid() {
        assertEquals(expectedPartNumbers, getPartNumbersFromGrid(exampleGrid))
    }

}