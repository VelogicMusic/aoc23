package solutions

import util.Input
import solutions.Direction

enum class Direction { LEFT, RIGHT, UP, DOWN }

data class Point(val x: Int, val y: Int) {
    fun getNext(direction: Direction): Point {
        return when (direction) {
            Direction.LEFT -> Point(x - 1, y)
            Direction.RIGHT -> Point(x + 1, y)
            Direction.UP -> Point(x, y - 1)
            Direction.DOWN -> Point(x, y + 1)
        }
    }
}

class Day16 : Day(16) {
    override fun solvePart1(input: Input): String {
        val inputGrid = input.lines
        return getEnergizedTiles(inputGrid, Point(0, 0), Direction.RIGHT).toString()
    }

    override fun solvePart2(input: Input): String {
        val maxX = input.lines.first().length
        val maxY = input.lines.size
        return listOf(
            (0..<maxX).maxOf { x -> getEnergizedTiles(input.lines, Point(x, 0), Direction.DOWN) },
            (0..<maxX).maxOf { x -> getEnergizedTiles(input.lines, Point(x, maxX - 1), Direction.UP)},
            (0..<maxY).maxOf { y -> getEnergizedTiles(input.lines, Point(0, y), Direction.RIGHT)},
            (0..<maxY).maxOf { y -> getEnergizedTiles(input.lines, Point(maxX - 1, y), Direction.LEFT)}
        ).max().toString()
    }

    private fun getEnergizedTiles(inputGrid: List<String>, startingPosition: Point, startingDirection: Direction): Int {
        val energizedTiles = mutableSetOf<Point>()
        val cache = HashSet<Pair<Point, Direction>>()
        val toVisit = ArrayDeque<Pair<Point, Direction>>().apply { add(startingPosition to startingDirection) }
        while (toVisit.isNotEmpty()) {
            val (currentPosition, currentDirection) = toVisit.removeFirst()
            if (currentPosition.y < 0 || currentPosition.y >= inputGrid.size || currentPosition.x < 0 || currentPosition.x >= inputGrid.first().length) continue
            if (currentPosition to currentDirection in cache) continue
            energizedTiles.add(currentPosition)
            cache.add (currentPosition to currentDirection)

            when (inputGrid[currentPosition.y][currentPosition.x]) {
                '.' -> toVisit.add(currentPosition.getNext(currentDirection) to currentDirection)
                '-' ->
                    if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
                        toVisit.add(currentPosition.getNext(currentDirection) to currentDirection)
                    } else {
                        toVisit.add(currentPosition.getNext(Direction.LEFT) to Direction.LEFT)
                        toVisit.add(currentPosition.getNext(Direction.RIGHT) to Direction.RIGHT)
                    }
                '|' ->
                    if (currentDirection == Direction.UP || currentDirection == Direction.DOWN) {
                        toVisit.add(currentPosition.getNext(currentDirection) to currentDirection)
                    } else {
                        toVisit.add(currentPosition.getNext(Direction.UP) to Direction.UP)
                        toVisit.add(currentPosition.getNext(Direction.DOWN) to Direction.DOWN)
                    }
                '/' ->
                    when (currentDirection) {
                        Direction.LEFT -> toVisit.add(currentPosition.getNext(Direction.DOWN) to Direction.DOWN)
                        Direction.RIGHT -> toVisit.add(currentPosition.getNext(Direction.UP) to Direction.UP)
                        Direction.UP -> toVisit.add (currentPosition.getNext(Direction.RIGHT) to Direction.RIGHT)
                        Direction.DOWN -> toVisit.add(currentPosition.getNext(Direction.LEFT) to Direction.LEFT)
                    }
                '\\' -> when (currentDirection) {
                        Direction.RIGHT-> toVisit.add(currentPosition.getNext(Direction.DOWN) to Direction.DOWN)
                        Direction.LEFT-> toVisit.add(currentPosition.getNext(Direction.UP) to Direction.UP)
                        Direction.DOWN-> toVisit.add (currentPosition.getNext(Direction.RIGHT) to Direction.RIGHT)
                        Direction.UP -> toVisit.add(currentPosition.getNext(Direction.LEFT) to Direction.LEFT)
                }
                else -> throw IllegalStateException("Invalid direction")
            }
        }
        return energizedTiles.size
    }
}
