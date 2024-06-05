package personal.alexdgarland.advent_of_code.day05

data class NumericRange(val start: Long, val length: Long) {
    val end = start + length - 1
}