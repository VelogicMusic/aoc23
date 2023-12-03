import solutions.Day
import solutions.Day01
import solutions.Day02
import solutions.Day03

fun main(args: Array<String>) {
    val arguments = argparse(args.toList())
    val solutions: List<Day> =
        listOf(
            Day01(1),
            Day02(2),
            Day03(3),
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
        println("Executing Day $day:")
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
    return solutions
        .filter { day: Day -> day.currentDay in days }
        .fold(emptyMap<Int, List<String>>()) { map, elem -> map + runDay(elem, parts) }
}

fun runDay(
    day: Day,
    parts: List<Int>,
): Pair<Int, List<String>> {
    var allTestsPassed = true
    val outputs = mutableListOf<String>()
    for (partNum in parts) {
        val partOutputs =
            day.testInputs[partNum]
                ?.map { (input, expected) ->
                    day.solve(partNum, input).let { output ->
                        if (output == expected) {
                            "Test Passed"
                        } else {
                            allTestsPassed = false
                            "Test Failed. Output:\n$output\nExpected:\n$expected"
                        }
                    }
                }?.toMutableList() ?: emptyList<String>().toMutableList()

        if (allTestsPassed) {
            partOutputs.add("Problem $partNum Output:\n${day.solve(partNum, day.input)}")
        }

        outputs += partOutputs
    }
    return day.currentDay to outputs.toList()
}
