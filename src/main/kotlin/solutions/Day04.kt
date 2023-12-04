package solutions

import kotlin.math.min
import kotlin.math.pow

class Day04(day: Int) : Day(day) {
    override fun solvePart1(input: String): String {
        val commonNumbers = parse(input)
        return commonNumbers
            .filter { numberSet -> numberSet.isNotEmpty() }
            .sumOf { numberSet -> 2.toDouble().pow((numberSet.size - 1).toDouble()).toInt() }
            .toString()
    }

    override fun solvePart2(input: String): String {
        val scratchCardSum = mutableMapOf<Int, Int>()
        val parsedInput = parse(input)
        val maxIndex = parsedInput.size + 1
        for ((i, numberSet) in parsedInput.withIndex()) {
            val index = i + 1
            scratchCardSum[index] = scratchCardSum.getOrDefault(index, 0) + 1
            val multiplier = scratchCardSum.getOrDefault(index, 1)
            for (nextIndex in 1..numberSet.size) {
                val currentIndex = min(index + nextIndex, maxIndex)
                scratchCardSum[currentIndex] = scratchCardSum.getOrDefault(currentIndex, 0) + multiplier
            }
        }
        return scratchCardSum.values.sum().toString()
    }

    private fun getNumbers(input: String): Set<Int> =
        input
            .chunked(3)
            .map { numString -> numString.trim().toInt() }
            .toSet()

    private fun parse(input: String): List<Set<Int>> =
        input.lines()
            .map { line -> line.substringAfter(": ") }
            .map { numbers -> numbers.split("|") }
            .map { (numSet1, numSet2) -> getNumbers(numSet1) intersect getNumbers(numSet2) }
}
