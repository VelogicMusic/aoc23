package solutions

import util.Input
import java.util.PriorityQueue

enum class TravelDirection { LEFT, RIGHT, UP, DOWN }

data class SeenPoint(val x: Int, val y: Int, val currentDirection: TravelDirection, val travelAmount: Int) {
    fun getNext(
        minTravel: Int,
        maxTravel: Int,
    ): List<SeenPoint> {
        val nextList =
            listOf(
                TravelDirection.LEFT.let { SeenPoint(x - 1, y, it, if (currentDirection == it) travelAmount + 1 else 1) },
                TravelDirection.RIGHT.let { SeenPoint(x + 1, y, it, if (currentDirection == it) travelAmount + 1 else 1) },
                TravelDirection.UP.let { SeenPoint(x, y - 1, it, if (currentDirection == it) travelAmount + 1 else 1) },
                TravelDirection.DOWN.let { SeenPoint(x, y + 1, it, if (currentDirection == it) travelAmount + 1 else 1) },
            )
        return nextList.filter { sp ->
            when (currentDirection) {
                TravelDirection.LEFT -> sp.currentDirection != TravelDirection.RIGHT
                TravelDirection.RIGHT -> sp.currentDirection != TravelDirection.LEFT
                TravelDirection.UP -> sp.currentDirection != TravelDirection.DOWN
                TravelDirection.DOWN -> sp.currentDirection != TravelDirection.UP
            }
        }.filter { sp -> (travelAmount >= minTravel || sp.currentDirection == currentDirection) && sp.travelAmount <= maxTravel }
    }
}

class Day17 : Day(17) {
    override fun solvePart1(input: Input): String {
        val grid = parse(input)
        return modifiedDijkstra(grid, 0, 3).toString()
    }

    override fun solvePart2(input: Input): String {
        val grid = parse(input)
        return modifiedDijkstra(grid, 4, 10).toString()
    }

    private fun modifiedDijkstra(
        grid: List<List<Int>>,
        minTravel: Int,
        maxTravel: Int,
    ): Int {
        val seen = HashSet<SeenPoint>()
        val toVisit = PriorityQueue<Pair<Int, SeenPoint>>(compareBy { it.first })
        .apply { addAll(
            listOf(0 to SeenPoint(0, 0, TravelDirection.RIGHT, 0), 0 to SeenPoint(0, 0, TravelDirection.DOWN, 0))
        ) }
        while (toVisit.isNotEmpty()) {
            val (currentDistance, currentPoint) = toVisit.remove()
            if (currentPoint in seen) continue
            seen.add(currentPoint)
            if (currentPoint.x == grid.first().size - 1 && currentPoint.y == grid.size - 1 && currentPoint.travelAmount >= minTravel) return currentDistance
            currentPoint.getNext(minTravel, maxTravel)
                .filter { p -> p.x in grid.first().indices && p.y in grid.indices }
                .map { p -> currentDistance + grid[p.y][p.x] to p }
                .forEach { p -> toVisit.add(p) }
        }
        return -1
    }

    fun parse(input: Input): List<List<Int>> {
        return input.lines.map { line -> line.map { c -> c.digitToInt() } }
    }
}
