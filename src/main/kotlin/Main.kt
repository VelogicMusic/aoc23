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
import solutions.Day14
import solutions.Day15
import solutions.Day16
import util.Executor
import util.InputReader
import util.aocintegration.InputFetcher
import util.aocintegration.RequestHandler
import util.aocintegration.Submitter
import java.io.File

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
            Day14(),
            Day15(),
            Day16(),
        ).associateBy { day -> day.currentDay }

    val cookie =
        runCatching { File("session.cookie").readText().trim() }
            .getOrElse { "".also { println("Error reading session.cookie") } }

    val executor = Executor()
    val requestHandler = RequestHandler(cookie)
    val inputFetcher = InputFetcher(requestHandler)
    val submitter = Submitter(requestHandler, "ns" in arguments.keys || "nosubmit" in arguments.keys)

    val dayInputs =
        (arguments["d"].orEmpty() + arguments["day"].orEmpty())
            .map { it.toInt() }
            .filter { num -> num in solutions.keys }
            .ifEmpty { solutions.keys }.toList()
            .onEach { day -> solutions[day]?.let { d -> executor.addDay(d) } }
            .map { day -> InputReader.getInputs(day, inputFetcher) }

    val partsToRun =
        arguments
            .filter { (key, _) -> key == "p" || key == "part" }
            .flatMap { (_, value) -> value.map { it.toInt() } }
            .ifEmpty { listOf(1, 2) }

    dayInputs
        .flatMap { singleDayInputs -> singleDayInputs.filter { dayInput -> dayInput.part.number in partsToRun } }
        .forEach { input -> executor.addInput(input) }

    executor.execute(submitter)
}

fun argparse(args: List<String>): Map<String, List<String>> =
    args.fold(Pair(emptyMap<String, List<String>>(), "")) { (map, lastKey), argString ->
        if (argString.startsWith("-")) {
            Pair(
                map + (argString.split(Regex("-{1,2}"))[1] to emptyList()),
                argString.split(Regex("-{1,2}"))[1],
            )
        } else {
            Pair(
                map + (lastKey to map.getOrDefault(lastKey, emptyList()) + argString),
                lastKey,
            )
        }
    }.first
