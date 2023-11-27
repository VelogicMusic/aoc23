package solutions

import java.io.File

abstract class Day {
    abstract val currentDay: Int
    abstract val testInput1: String
    abstract val testInput2: String

    val fileReader = { fileName: String -> File("day$currentDay/$fileName").bufferedReader() }

    abstract fun parsePart1(fileName: String): String
    abstract fun parsePart2(fileName: String): String

    abstract fun solvePart1(input: String): String
    abstract fun solvePart2(input: String): String
}
