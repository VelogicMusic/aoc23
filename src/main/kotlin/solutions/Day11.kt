package solutions

import util.Input
import kotlin.math.abs

data class Galaxy(val x: Long, val y: Long) {
    fun minDistance(galaxy: Galaxy): Long {
        return abs(x - galaxy.x) + abs(y - galaxy.y)
    }
}

class Day11 : Day(11) {
    override fun solvePart1(input: Input): String {
        val galaxies = parse(input)
        val alreadySeen = mutableListOf<Galaxy>()
        var sum = 0L
        for (galaxy in galaxies) {
            alreadySeen.add(galaxy)
            sum += galaxies.filter { g -> g !in alreadySeen }.sumOf { g -> galaxy.minDistance(g) }
        }
        return sum.toString()
    }

    override fun solvePart2(input: Input): String {
        val galaxies = parse(input, 1_000_000 - 1)
        val alreadySeen = mutableListOf<Galaxy>()
        var sum = 0L
        for (galaxy in galaxies) {
            alreadySeen.add(galaxy)
            sum += galaxies.filter { g -> g !in alreadySeen }.sumOf { g -> galaxy.minDistance(g) }
        }
        return sum.toString()
    }

    private fun parse(input: Input, dist: Long = 1): List<Galaxy> {
        val galaxies = mutableListOf<Galaxy>()
        val emptyRows = input.lines.withIndex().filter { (_, row) -> row.all { c -> c == '.' } }.map { (index, _) -> index.toLong() }
        val emptyCols =
            (0..<input.lines.first().length).filter { index ->
                input.lines.all { line -> line[index] == '.' }
            }.map { index -> index.toLong() }
        for ((row, line) in input.lines.withIndex()) {
            for ((col, char) in line.withIndex()) {
                if (char == '#') {
                    val actualRow = row + emptyRows.count { i -> i < row } * dist
                    val actualCol = col + emptyCols.count { i -> i < col } * dist
                    galaxies.add(Galaxy(actualCol, actualRow))
                }
            }
        }
        return galaxies.toList()
    }
}
