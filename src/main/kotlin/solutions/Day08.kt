package solutions

import kotlin.math.max

class Day08(day: Int) : Day(day) {
    override fun solvePart1(input: String): String {
        val (sequence, mappings) = parse(input)
        return findExit(sequence, mappings, "AAA", "ZZZ").toString()
    }

    override fun solvePart2(input: String): String {
        val (sequence, mappings) = parse(input)
        val currentNodes =
            mappings
                .filterKeys { it.endsWith("A") }.keys
                .associateWith { findExit(sequence, mappings, it, "Z").toLong() }
        return currentNodes.values
            .fold(currentNodes.values.first()) { acc, num -> findLCM(acc, num) }
            .toString()
    }

    private fun findLCM(
        a: Long,
        b: Long,
    ): Long {
        val larger = max(a, b)
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    private fun findExit(
        sequence: String,
        mappings: Map<String, Map<Char, String>>,
        start: String,
        endDelim: String,
    ): Int {
        var counter = 0
        var current = start
        while (true) {
            if (current.endsWith(endDelim)) {
                return counter
            }
            val currentInstruction = sequence[counter % sequence.length]
            current = mappings[current]!![currentInstruction]!!
            counter += 1
        }
    }

    private fun parse(input: String): Pair<String, Map<String, Map<Char, String>>> {
        val charSequence = input.substringBefore("\n\n")
        val mapping = mutableMapOf<String, Map<Char, String>>()
        for (line in input.substringAfter("\n\n").lines()) {
            val (inpString, rest) = line.split(" = (")
            val (left, right) = rest.substringBefore(")").split(", ")
            mapping[inpString] = mapOf('L' to left, 'R' to right)
        }
        return charSequence to mapping.toMap()
    }
}
