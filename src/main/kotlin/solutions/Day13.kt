package solutions

import util.Input
import kotlin.math.min

data class ReflectionPattern(val pattern: List<String>) {
    fun getTransposition(): ReflectionPattern {
        val transposedPattern = mutableListOf<String>()
        val mergedPattern = pattern.joinToString("")
        for (i in pattern.first().indices) {
            transposedPattern.add(mergedPattern.filterIndexed { index, _ -> index % pattern.first().length == i })
        }
        return ReflectionPattern(transposedPattern)
    }

    fun getHorizontalReflection(smudges: Int = 0): Int {
        for (i in 1..<pattern.size) {
            val minLength = min(i, pattern.size - i)
            val first = pattern.take(i).reversed().take(minLength)
            val last = pattern.drop(i).take(minLength)
            val combined = first.zip(last)
            if (combined.sumOf { (f, l) -> f.zip(l).count { (a, b) -> a != b } } == smudges) {
                return i
            }
        }
        return 0
    }
}

class Day13 : Day(13) {
    override fun solvePart1(input: Input): String {
        val patterns = parse(input)
        return patterns.sumOf { pattern ->
            val res = pattern.getTransposition().getHorizontalReflection()
            if (res == 0) 100 * pattern.getHorizontalReflection() else res
        }.toString()
    }

    override fun solvePart2(input: Input): String {
        val patterns = parse(input)
        return patterns.sumOf { pattern ->
            val res = pattern.getTransposition().getHorizontalReflection(1)
            if (res == 0) 100 * pattern.getHorizontalReflection(1) else res
        }.toString()
    }

    private fun parse(input: Input): List<ReflectionPattern> {
        return input.text.split("\n\n").map { pattern -> ReflectionPattern(pattern.split("\n")) }
    }
}
