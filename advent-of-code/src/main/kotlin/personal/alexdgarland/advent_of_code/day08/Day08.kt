package personal.alexdgarland.advent_of_code.day08

import personal.alexdgarland.advent_of_code.Input

enum class Instruction { L, R }

fun instructionResolver(instructions: List<Instruction>): (Int) -> Instruction {
    return {currentNumberOfSteps: Int -> instructions[(currentNumberOfSteps - 1) % instructions.size] }
}

data class LocationNode(val left: String, val right: String) {

    fun getNextKey(instruction: Instruction): String {
        return if(instruction == Instruction.L) left else right
    }

}

data class DirectionInputs(val instructions: List<Instruction>, val nodes: Map<String, LocationNode>)

fun parseInput(lines: List<String>): DirectionInputs {
    val instructions = lines.first().map { char -> Instruction.valueOf(char.toString()) }
    val nodes = lines.drop(2).associate { row ->
        row.take(3) to LocationNode(row.slice(7..9), row.slice(12..14))
    }
    return DirectionInputs(instructions, nodes)
}

fun calculateRequiredNumberOfSteps(inputs: DirectionInputs): Int {
    val resolveInstruction = instructionResolver(inputs.instructions)
    var currentNode: LocationNode = inputs.nodes["AAA"]!!
    var currentNumberOfSteps = 1
    while (true) {
        val nextInstruction = resolveInstruction(currentNumberOfSteps)
        val nextKey = currentNode.getNextKey(nextInstruction)
        if (nextKey == "ZZZ") {
            return currentNumberOfSteps
        }
        currentNode = inputs.nodes[nextKey]!!
        currentNumberOfSteps++
    }
}

object Solution {

    fun run() {
        val input = Input.getLines("08").toList()
        val directionInputs = parseInput(input)
        val result = calculateRequiredNumberOfSteps(directionInputs)
        println(" --- Result: $result --- ")    // 13771
    }

}
