package solutions

import util.Input

class Day14 : Day(14) {
    override fun solvePart1(input: Input): String {
        return score(transpose(moveLeft(transpose(input.lines)))).toString()
    }

    override fun solvePart2(input: Input): String {
        return ""
    }

    fun score(input: List<String>): Int {
        return input.withIndex().sumOf { (index, row) -> (input.size - index) * row.count { it == 'O' } }
    }

    fun transpose(input: List<String>): List<String> {
        val transposedPattern = mutableListOf<String>()
        val mergedPattern = input.joinToString("")
        for (i in input.first().indices) {
            transposedPattern.add(mergedPattern.filterIndexed { index, _ -> index % input.first().length == i })
        }
        return transposedPattern
    }

    private fun moveLeft(input: List<String>): List<String> {
        return input
            .map { line ->
                line.split("#").map { it.toList().sorted().reversed().joinToString("") }
            }
            .map { substrings -> substrings.joinToString("#") }
    }
}
