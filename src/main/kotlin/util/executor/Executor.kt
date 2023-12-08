package util.executor

import solutions.Day
import util.Input
import util.PuzzleInput
import util.TestInput

/**
 *  Store the return value and execution time
 */
data class Output(val returnValue: String, val time: Long)

/**
 * Builder-like class to execute specific days / inputs
 */
class Executor() {
    private val daysToExecute = mutableListOf<Day>()
    private val inputs = mutableListOf<Input>()

    /**
     * Add a [day] to the executor
     */
    fun addDay(day: Day) = daysToExecute.add(day)

    /**
     * Add an [input] to the executor
     */
    fun addInput(input: Input) = inputs.add(input)

    /**
     * Given [Day]s and [Input]s, execute them all in order
     * Executes in order: Days -> Parts -> Test Input
     * If all test inputs succeed, the puzzle input will be run
     */
    fun execute() {
        val toRun =
            daysToExecute
                .sortedBy { day -> day.currentDay }
                .associateWith { day -> inputs.filter { input -> input.day == day.currentDay } }
        for ((day, inputs) in toRun) {
            println("Executing Code for Day ${day.currentDay}")
            for (part in inputs.map { input -> input.part }.toSet()) {
                val passedTests =
                    inputs.filterIsInstance<TestInput>()
                        .filter { input -> input.part == part }
                        .all { input -> runWithLog(day, input) }
                if (passedTests) {
                    inputs.filterIsInstance<PuzzleInput>()
                        .filter { input -> input.part == part }
                        .forEach { input -> runWithLog(day, input) }
                }
                println()
            }
        }
    }

    /**
     * Run solve function for a given [input] on a [day] and log all results/states
     * @param day [Day] to call solve on
     * @param input the [Input] used
     */
    private fun runWithLog(
        day: Day,
        input: Input,
    ): Boolean {
        val result: Result<Output>
        when (input) {
            is TestInput -> {
                print("Running Test Input on Part ${input.part.number}: ")
                result = runInput(day, input)
                when (result) {
                    is Result.Success -> println("Test Passed! (execution time: ${result.value.time}ms)")
                    is Result.Failure -> println(result.reason)
                }
            }
            is PuzzleInput -> {
                print("Running Puzzle Input on Part ${input.part.number}: ")
                result = runInput(day, input)
                when (result) {
                    is Result.Success -> println("${result.value.returnValue} (execution time: ${result.value.time}ms)")
                    else -> throw IllegalStateException("There should not be a failure when running the puzzle input")
                }
            }
        }
        return result.isSuccess()
    }

    /**
     * Runs [input] on a [day] and if the run was successful, stores the return value and execution time in an [Output] object
     */
    private fun runInput(
        day: Day,
        input: Input,
    ): Result<Output> {
        val startTime = System.currentTimeMillis()
        val returnValue = day.solve(input)
        val time = System.currentTimeMillis() - startTime

        return when (input) {
            is PuzzleInput -> Result.success(Output(returnValue, time))
            is TestInput ->
                if (returnValue == input.expectedResult) {
                    Result.success(Output(returnValue, time))
                } else {
                    Result.failure<Output>("Got $returnValue but expected ${input.expectedResult}")
                }
        }
    }
}

