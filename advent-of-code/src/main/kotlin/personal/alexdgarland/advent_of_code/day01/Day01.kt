package personal.alexdgarland.advent_of_code.day01

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream


fun extractCalibrationValue(line: String): Int {
    var firstDigit: Char? = null
    var lastDigit: Char? = null
    for (c in line) {
        if(c.isDigit()) {
            if(firstDigit == null) {
                firstDigit = c
            }
            lastDigit = c
        }
    }
    return "${firstDigit!!}${lastDigit!!}".toInt()
}

object Solution {

    fun run() {
        val inputPath = javaClass.getClassLoader().getResource("day01_input.txt")!!.path
        val lines: Stream<String> = Files.lines(Paths.get(inputPath))
        val result = lines.mapToInt { l -> extractCalibrationValue(l) }.sum()
        print(" --- $result --- ")
    }

}
