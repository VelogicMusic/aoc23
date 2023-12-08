package solutions

import util.Input

class Day06 : Day(6) {
    override fun solvePart1(input: Input): String {
        val races =
            input.text.split("\n")
                .map { inp -> Regex("\\d+").findAll(inp) }
                .map { regex -> regex.map { r -> r.value.toInt() }.toList() }
                .let { it.first() zip it.last() }
        return races
            .map { (time, distance) ->
                (0..time).filter { (time - it) * it > distance }.size
            }
            .reduce { acc, i -> acc * i }
            .toString()
    }

    override fun solvePart2(input: Input): String {
        return input.text.split("\n")
            .map { inp -> inp.substringAfter(":") }
            .map { inp -> inp.replace("\\s".toRegex(), "") }
            .let { it.first().toLong() to it.last().toLong() }
            .let { (time, distance) ->
                (0..time).count { (time - it) * it > distance }
            }
            .toString()
    }
}
