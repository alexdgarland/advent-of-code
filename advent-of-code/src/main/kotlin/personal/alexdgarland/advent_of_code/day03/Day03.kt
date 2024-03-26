package personal.alexdgarland.advent_of_code.day03

import personal.alexdgarland.advent_of_code.Input

private fun isSymbol(line: String?, charIndex: Int): Boolean {
    if (line == null || charIndex < 0 || charIndex > line.lastIndex) {
        return false
    }
    return line[charIndex] != '.' && !line[charIndex].isDigit()
}

fun getPartNumbersFromLine(grid: List<String>, lineIndex: Int): List<Int> {
    // Vals that are read-only/ fully immutable within scope of function
    val currLine: String = grid[lineIndex]
    val prevLine: String? = if (lineIndex == 0) null else grid[lineIndex-1]
    val nextLine: String? = if (lineIndex == grid.lastIndex) null else grid[lineIndex+1]
    // Immutable refs, mutable objects
    val partsList = mutableListOf<Int>()
    val builder = StringBuilder()
    // Mutable vars
    var inWord = false
    var sawSymbol = false

    fun flush() {
        if(sawSymbol) {
            partsList.add(builder.toString().toInt())
        }
        builder.clear()
        inWord = false
        sawSymbol = false
    }

    for(charIndex in currLine.indices) {
        val currentChar = currLine[charIndex]
        if(inWord) {
            sawSymbol = sawSymbol || isSymbol(prevLine, charIndex) || isSymbol(nextLine, charIndex)
            if(currentChar.isDigit()) {
                // Keep building current part number
                builder.append(currentChar)
            }
            else {
                // Flush current part number to list if valid
                sawSymbol = sawSymbol || isSymbol(currLine, charIndex)
                flush()
            }
        } else {
            if(currentChar.isDigit()) {
                // Start a new part number
                inWord = true
                sawSymbol = isSymbol(prevLine, charIndex - 1) ||
                        isSymbol(currLine, charIndex - 1) ||
                        isSymbol(nextLine, charIndex - 1) ||
                        isSymbol(prevLine, charIndex) ||
                        isSymbol(nextLine, charIndex)
                builder.append(currentChar)
            }
        }
    }
    if (inWord) {
        flush()
    }
    return partsList
}

fun getPartNumbersFromGrid(grid: List<String>): List<Int> {
    return grid.indices.flatMap { index -> getPartNumbersFromLine(grid, index) }
}

object Solution {

    fun run() {
        val grid = Input.getLines("03").toList()
        val result = getPartNumbersFromGrid(grid).sum()
        print(" --- $result --- ")
    }

}
