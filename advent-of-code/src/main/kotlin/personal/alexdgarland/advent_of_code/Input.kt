package personal.alexdgarland.advent_of_code

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

object Input {

    fun getLines(dayNumber: String): Stream<String> {
        val inputPath = javaClass.getClassLoader().getResource("day${dayNumber}_input.txt")!!.path
        return Files.lines(Paths.get(inputPath))
    }

}