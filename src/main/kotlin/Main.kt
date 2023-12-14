import solutions.Day
import solutions.Day01
import solutions.Day02
import solutions.Day03
import solutions.Day04
import solutions.Day05
import solutions.Day06
import solutions.Day07
import solutions.Day08
import solutions.Day09
import solutions.Day10
import solutions.Day11
import solutions.Day12
import solutions.Day13
import util.Executor
import util.InputReader

fun main(args: Array<String>) {
    val arguments = argparse(args.toList())
    val solutions =
        listOf(
            Day01(),
            Day02(),
            Day03(),
            Day04(),
            Day05(),
            Day06(),
            Day07(),
            Day08(),
            Day09(),
            Day10(),
            Day11(),
            Day12(),
            Day13(),
            Day13(),
        ).mapIndexed { index: Int, day: Day -> index + 1 to day }.toMap()

    val executor = Executor()

    val dayInputs =
        (arguments["d"].orEmpty() + arguments["day"].orEmpty())
            .map { it.toInt() }
            .filter { num -> num in solutions.keys }
            .ifEmpty { solutions.keys }.toList()
            .onEach { day -> executor.addDay(solutions[day]!!) }
            .map { day -> InputReader.getInputs(day) }

    val partsToRun =
        arguments
            .filter { (key, _) -> key == "p" || key == "part" }
            .flatMap { (_, value) -> value.map { it.toInt() } }
            .let { it.ifEmpty { listOf(1, 2) } }

    dayInputs
        .flatMap { singleDayInputs -> singleDayInputs.filter { dayInput -> dayInput.part.number in partsToRun } }
        .forEach { input -> executor.addInput(input) }

    executor.execute()
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
