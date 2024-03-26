package personal.alexdgarland.advent_of_code.day03

import personal.alexdgarland.advent_of_code.Input

data class Coordinate(val lineIndex: Int, val charIndex: Int) {
    fun outOfBounds(grid: List<String>): Boolean {
        return lineIndex < 0 ||
                lineIndex > grid.lastIndex ||
                charIndex < 0 ||
                charIndex > grid[lineIndex].lastIndex
    }

    override fun toString(): String {
        return "($lineIndex, $charIndex)"
    }
}

data class PartNumberInformation(val number: Int, val gearCandidateCoordinates: Set<Coordinate>) {

    override fun toString(): String {
        return "Part Number $number - coords [${gearCandidateCoordinates.joinToString(", ")}]"
    }
}

fun getPartNumbersFromLine(grid: List<String>, lineIndex: Int): List<PartNumberInformation> {
    // Vals that are read-only/ fully immutable within scope of function
    val currLine: String = grid[lineIndex]
    // Immutable refs, mutable objects
    val partsList = mutableListOf<PartNumberInformation>()
    val builder = StringBuilder()
    val gearCandidateCoordinates = mutableSetOf<Coordinate>()
    // Mutable vars
    var inWord = false
    var sawSymbol = false

    // These "checkForSymbol" functions are a real mess of return values and side effects;
    // it might be overkill for this problem, but handling of state could be a lot clearer and more consistent.

    fun checkForSymbol(coordinate: Coordinate): Boolean {
        if (coordinate.outOfBounds(grid)) {
            return false
        }
        val char = grid[coordinate.lineIndex][coordinate.charIndex]
        if(char == '*') {
            gearCandidateCoordinates.add(coordinate)
            return true
        }
        return char != '.' && !char.isDigit()
    }

    fun checkForSymbols(coordinates: List<Coordinate>) {
        for (coord in coordinates) {
            if (checkForSymbol(coord)) {
                sawSymbol = true
            }
        }
    }

    fun flush() {
        if(sawSymbol) {
            partsList.add(PartNumberInformation(builder.toString().toInt(), gearCandidateCoordinates.toSet()))
        }
        builder.clear()
        gearCandidateCoordinates.clear()
        inWord = false
        sawSymbol = false
    }

    for(charIndex in currLine.indices) {
        val currentChar = currLine[charIndex]
        if(inWord) {
            checkForSymbols(
                listOf(
                    Coordinate(lineIndex-1, charIndex),
                    Coordinate(lineIndex+1, charIndex)
                )
            )
            if(currentChar.isDigit()) {
                // Keep building current part number
                builder.append(currentChar)
            }
            else {
                checkForSymbols(listOf(Coordinate(lineIndex, charIndex)))
                flush()
            }
        } else {
            if(currentChar.isDigit()) {
                // Start a new part number
                inWord = true
                checkForSymbols(
                    listOf(
                        Coordinate(lineIndex-1, charIndex - 1),
                        Coordinate(lineIndex, charIndex - 1),
                        Coordinate(lineIndex+1, charIndex - 1),
                        Coordinate(lineIndex-1, charIndex),
                        Coordinate(lineIndex+1, charIndex)
                    )
                )
                builder.append(currentChar)
            }
        }
    }
    if (inWord) {
        flush()
    }
    return partsList
}

fun getPartNumbersFromGrid(grid: List<String>): List<PartNumberInformation> {
    return grid.indices.flatMap { index -> getPartNumbersFromLine(grid, index) }
}

object Solution {

    fun run() {
        val grid = Input.getLines("03").toList()

        val results = getPartNumbersFromGrid(grid)

        val partNumbersResult = results.sumOf { it.number }
        println(" --- $partNumbersResult --- ")

        val coordsPartDict = mutableMapOf<Coordinate, MutableSet<Int>>()
        for(r in results) {
            for(coord in r.gearCandidateCoordinates) {
                (coordsPartDict.getOrPut(coord){ mutableSetOf() }).add(r.number)
            }
        }
        val gearsResult = coordsPartDict
            .filter { it.value.size == 2 }
            .map{it.value.reduce(Int::times)}
            .sum()
        println(" --- $gearsResult --- ")

    }

}
