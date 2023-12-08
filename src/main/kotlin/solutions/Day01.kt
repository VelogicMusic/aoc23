package solutions

import util.Input

class Day01 : Day(1) {
    override fun solvePart1(input: Input): String = getCalibrationValues(input.text)

    override fun solvePart2(input: Input): String {
        var transformedInput = input.text
        mapOf(
            "one" to "one1one",
            "two" to "two2two",
            "three" to "three3three",
            "four" to "four4four",
            "five" to "five5five",
            "six" to "six6six",
            "seven" to "seven7seven",
            "eight" to "eight8eight",
            "nine" to "nine9nine",
        ).forEach { (k: String, v: String) ->
            transformedInput = transformedInput.replace(k, v)
        }
        return getCalibrationValues(transformedInput)
    }

    private fun getCalibrationValues(input: String) =
        input.lines()
            .fold(0) { currentSum: Int, line: String ->
                currentSum +
                    (
                        line.first { c -> c.isDigit() }.toString() + line.last { c -> c.isDigit() }.toString()
                    ).toInt()
            }.toString()
}
