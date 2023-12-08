package solutions

import util.Input
import util.Part

abstract class Day(val currentDay: Int) {
    /**
     * Wrapper function to call respective solve function
     * */
    fun solve(input: Input): String =
        when (input.part) {
            Part.PART_1 -> solvePart1(input)
            Part.PART_2 -> solvePart2(input)
        }

    abstract fun solvePart1(input: Input): String

    abstract fun solvePart2(input: Input): String
}
