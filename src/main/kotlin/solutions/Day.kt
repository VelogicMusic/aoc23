package solutions

import java.io.File
import kotlin.concurrent.fixedRateTimer

abstract class Day(val currentDay: Int) {
    private val dayString: String
        get() = currentDay.toString().padStart(2, '0')

    val testInputs: Map<Int, Map<String, String>> =
        File("src/main/resources/day01").walk().filter { "test" in it.toString() }
            .fold<File, Map<Int, Map<String, String>>>(emptyMap()) { map, filePath ->
                map +
                    Pair(
                        Regex("part(\\d*)").find(filePath.toString())!!.groups[1]!!.component1().toInt(),
                        mapOf(
                            File(filePath.toString())
                                .readText()
                                .split("\n\nExpected Result:\n".toRegex())
                                .map { it.trim() }
                                .zipWithNext().first()
                        ),
                    )
            }

    open val input: String = File("src/main/resources/day$dayString/input").bufferedReader().readText().trim()

    fun solve(
        part: Int,
        input: String,
    ): String =
        when (part) {
            1 -> solvePart1(input)
            2 -> solvePart2(input)
            else -> throw IllegalArgumentException("Part does not exist")
        }

    abstract fun solvePart1(input: String): String

    abstract fun solvePart2(input: String): String
}
