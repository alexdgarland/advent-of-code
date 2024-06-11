package personal.alexdgarland.advent_of_code.day08

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested

class Day08KtTest {

    @Nested
    inner class InstructionResolverTests {

        val resolveInstruction = instructionResolver(listOf(Instruction.L, Instruction.L, Instruction.R))

        @Test
        fun `indexes within length of list return correctly`() {
            assertEquals(Instruction.L, resolveInstruction(1))
            assertEquals(Instruction.L, resolveInstruction(2))
            assertEquals(Instruction.R, resolveInstruction(3))
        }

        @Test
        fun `indexes beyond length of list return correctly`() {
            assertEquals(Instruction.L, resolveInstruction(4))
            assertEquals(Instruction.L, resolveInstruction(5))
            assertEquals(Instruction.R, resolveInstruction(6))
            assertEquals(Instruction.L, resolveInstruction(7))
            assertEquals(Instruction.L, resolveInstruction(8))
            assertEquals(Instruction.R, resolveInstruction(9))
        }

    }

    @Nested
    inner class FullSolutionTests() {

        private val example1 = DirectionInputs(
            listOf(Instruction.R, Instruction.L),
            mapOf(
                "AAA" to LocationNode("BBB", "CCC"),
                "BBB" to LocationNode("DDD", "EEE"),
                "CCC" to LocationNode("ZZZ", "GGG"),
                "DDD" to LocationNode("DDD", "DDD"),
                "EEE" to LocationNode("EEE", "EEE"),
                "GGG" to LocationNode("GGG", "GGG"),
                "ZZZ" to LocationNode("ZZZ", "ZZZ")
            )
        )

        private val example2 = DirectionInputs(
            listOf(Instruction.L, Instruction.L, Instruction.R),
            mapOf(
                "AAA" to LocationNode("BBB", "BBB"),
                "BBB" to LocationNode("AAA", "ZZZ"),
                "ZZZ" to LocationNode("ZZZ", "ZZZ")
            )
        )

        @Test
        fun `solve first example correctly`() {
            assertEquals(2, calculateRequiredNumberOfSteps(example1))
        }

        @Test
        fun `solve second example correctly`() {
            assertEquals(6, calculateRequiredNumberOfSteps(example2))
        }

    }

}
