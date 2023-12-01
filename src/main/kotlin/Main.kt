import solutions.Day
import solutions.Day01

fun main(args: Array<String>) {
    val arguments = argparse(args.toList())
    val solutions: List<Day> =
        listOf(
            Day01(1),
        )

    val runPart =
        arguments
            .filter { (key, _) -> key == "p" || key == "part" }
            .flatMap { (_, value) -> value.map { it.toInt() } }
            .let { it.ifEmpty { listOf(1, 2) } }

    val toRun =
        (arguments["d"].orEmpty() + arguments["day"].orEmpty())
            .map { it.toInt() }
            .let { it.ifEmpty { listOf(1, 2) } }

    val outputs = runDays(toRun, runPart, solutions)

    for ((day, output) in outputs.toSortedMap()) {
        println("Output Day $day:")
        output.forEach {
            println(it)
        }
        println()
    }
}

fun argparse(args: List<String>): Map<String, List<String>> =
    args.fold(Pair(emptyMap<String, List<String>>(), "")) { (map, lastKey), s ->
        if (s.startsWith("-")) {
            Pair(
                map + (s.split(Regex("-{1,2}"))[1] to emptyList()),
                s.split(Regex("-{1,2}"))[1],
            )
        } else {
            Pair(
                map + (lastKey to map.getOrDefault(lastKey, emptyList()) + s),
                lastKey,
            )
        }
    }.first

fun runDays(
    days: List<Int>,
    parts: List<Int>,
    solutions: List<Day>,
): Map<Int, List<String>> {
    val solutionsToRun = solutions.filter { day: Day -> day.currentDay in days }
    return solutionsToRun
        .fold(emptyMap<Int, List<String>>()) { map, elem ->
            var allTestsPassed = true
            map +
                Pair(
                    elem.currentDay,
                    parts.flatMap { partNum: Int ->
                        elem.testInputs[partNum]!!
                            .map { (input, expected) ->
                                elem.solve(partNum, input).let { output: String ->
                                    if (output == expected) {
                                        "Test Passed"
                                    } else {
                                        "Test failed. Output:\n$output\nExpected:\n$expected".also { allTestsPassed = false }
                                    }
                                }
                            } + if (allTestsPassed) listOf("Problem $partNum Output:\n${elem.solve(partNum, elem.input)}") else emptyList()
                    },
                )
        }
}
