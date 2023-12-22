package personal.alexdgarland.advent_of_code.day01

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

private data class DigitSearchResult(val foundDigitChar: Char?, val advanceBy: Int = 1)

private data class DigitNameMapping(val name: String, private val digit: Char) {
    val searchResult = DigitSearchResult(digit, name.length)
}

private val digitNameMappings = listOf(
    DigitNameMapping("one",  '1'),
    DigitNameMapping("two", '2'),
    DigitNameMapping("three", '3'),
    DigitNameMapping("four", '4'),
    DigitNameMapping("five",'5'),
    DigitNameMapping("six", '6'),
    DigitNameMapping("seven", '7'),
    DigitNameMapping("eight", '8'),
    DigitNameMapping("nine", '9')
)

private val NO_DIGIT_FOUND = DigitSearchResult(null)

private fun searchForDigit(line: String, i: Int): DigitSearchResult {
    if (line[i].isDigit()) {
        return DigitSearchResult(line[i])
    }
    // This isn't the absolute _most_ efficient - technically we could build a one-off prefix tree - but given that
    //  a) this only multiplies n by a constant factor, doesn't affect big-O and
    //  b) we're not marked on performance for AoC
    //  ...doesn't seem worth the effort
    for (mapping in digitNameMappings) {
        if(line.startsWith(prefix = mapping.name, startIndex = i)) {
            return mapping.searchResult
        }
    }
    return NO_DIGIT_FOUND
}

fun extractCalibrationValue(line: String): Int {
    var firstDigit: Char? = null
    var lastDigit: Char? = null
    var i = 0
    while(i <= line.lastIndex) {
        val searchResult = searchForDigit(line, i)
        searchResult.foundDigitChar?.let { foundDChar ->
            if(firstDigit == null) {
                firstDigit = foundDChar
            }
            lastDigit = foundDChar
        }
        i += searchResult.advanceBy
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
