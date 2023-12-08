package solutions

import util.Input
import util.Part
import util.PuzzleInput
import util.TestInput
import java.io.File

abstract class Day(val currentDay: Int) {
    /**
     * Get zero padded string of current day
     * */
    private val dayString: String
        get() = currentDay.toString().padStart(2, '0')

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

    companion object {
        const val RESOURCE_DIR = "src/main/resources"
    }
}
