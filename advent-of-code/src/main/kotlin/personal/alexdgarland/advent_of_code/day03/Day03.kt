package personal.alexdgarland.advent_of_code.day03

import personal.alexdgarland.advent_of_code.Input

abstract class State(protected val partNumbers: MutableList<Int>) {

    abstract fun handleDigit(digit: Char): State

    abstract fun handleNonDigit(): State

    fun progress(c: Char): State {
        return if(c.isDigit()) handleDigit(c) else handleNonDigit()
    }

    fun finalizePartNumbers(): List<Int> {
        handleNonDigit()    // Just in case a part number runs right up to end of line - force flush of state
        return partNumbers.toList()
    }

}

class OutsideWordState(partNumbers: MutableList<Int>): State(partNumbers) {

    override fun handleDigit(digit: Char): State {
        val newInWordState = InWordState(partNumbers)
        return newInWordState.handleDigit(digit)
    }

    override fun handleNonDigit(): State {
        return this
    }

}

class InWordState(partNumbers: MutableList<Int>): State(partNumbers) {

    private val builder = StringBuilder()

    override fun handleDigit(digit: Char): State {
        builder.append(digit)
        return this
    }

    override fun handleNonDigit(): State {
        partNumbers.add(builder.toString().toInt())
        return OutsideWordState(partNumbers)
    }

}

fun getPartNumberFromLine(line: String): List<Int> {
    var state: State = OutsideWordState(mutableListOf())
    for(c in line) {
        state = state.progress(c)
    }
    return state.finalizePartNumbers()
}

fun getPartNumbersFromGrid(grid: List<String>): List<Int> {
    return grid.flatMap { line -> getPartNumberFromLine(line) }
}

object Solution {

    fun run() {
        val grid = Input.getLines("03").toList()
        val result = getPartNumbersFromGrid(grid).sum()
        print(" --- $result --- ")
    }

}
