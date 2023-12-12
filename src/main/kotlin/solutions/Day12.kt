package solutions

import util.Input

data class ConditionRecord(val springs: String, val damages: List<Long>) {
    private val cache = HashMap<Pair<String, List<Long>>, Long>()

    fun getCombinationSum(): Long = getCombinations(springs, damages)

    private fun getCombinations(
        s: String,
        d: List<Long>,
    ): Long {
        if (d.isEmpty()) {
            return if (s.any { it == '#' }) 0 else 1
        }
        if (s.isEmpty()) {
            return 0
        }

        return cache.getOrPut(s to d) {
            var result = 0L
            result += if (s.first() != '#') getCombinations(s.drop(1), d) else 0
            if (s.first() != '.' && d.first() <= s.length && s.take(d.first().toInt()).all { it != '.' }) {
                if ((d.first() < s.length && s[d.first().toInt()] != '#') || d.first() == s.length.toLong()) {
                    result += getCombinations(s.drop(d.first().toInt() + 1), d.drop(1))
                }
            }
            result
        }
    }
}

class Day12 : Day(12) {
    override fun solvePart1(input: Input): String {
        val conditionRecords = parse(input)
        return conditionRecords.sumOf { cr -> cr.getCombinationSum() }.toString()
    }

    override fun solvePart2(input: Input): String {
        val conditionRecords = parse(input, false)
        return conditionRecords.sumOf { cr -> cr.getCombinationSum() }.toString()
    }

    private fun parse(
        input: Input,
        part1: Boolean = true,
    ): List<ConditionRecord> {
        return input.lines
            .map { line -> line.split(" ") }
            .map { (springs, damages) ->
                if (part1) springs to damages else "$springs?".repeat(5).dropLast(1) to "$damages,".repeat(5).dropLast(1)
            }
            .map { (springs, damages) ->
                ConditionRecord(springs, damages.split(",").map { it.toLong() })
            }
    }
}
