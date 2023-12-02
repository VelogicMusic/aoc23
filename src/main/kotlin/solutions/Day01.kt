package solutions

class Day01(day: Int) : Day(day) {
    override fun solvePart1(input: String): String = getCalibrationValues(input)

    override fun solvePart2(input: String): String {
        var transformedInput = input
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
