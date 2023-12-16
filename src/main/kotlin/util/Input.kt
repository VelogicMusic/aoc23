package util

import util.aocintegration.InputFetcher
import java.io.File
import kotlin.streams.toList

/**
 * Enums for each part
 */
enum class Part {
    PART_1 {
        override val number = 1
        override val dirString = "part1"
    },
    PART_2 {
        override val number = 2
        override val dirString = "part2"
    }, ;

    abstract val number: Int
    abstract val dirString: String
}

/**
 * Base class for inputs
 * @param day [Day] corresponding to input
 * @param part corresponding [Part] for input
 * @param fileName path of the input file
 */
sealed class Input(val day: Int, val part: Part, fileName: String) {
    private val inputReader = File(fileName).bufferedReader()

    val text = inputReader.readText().trim()

    val lines = text.lines()
}

/**
 * Class describing test input
 * Additionally reads the expected result from the fileName.expected file
 */
class TestInput(day: Int, part: Part, fileName: String) : Input(day, part, fileName) {
    private val resultReader = File("$fileName.expected").bufferedReader()
    val expectedResult = resultReader.readText().trim()
}

/**
 * Class describing Puzzle input
 */
class PuzzleInput(day: Int, part: Part, fileName: String) : Input(day, part, fileName)

/**
 * Factory to generate input objects
 */
object InputReader {
    private const val RESOURCE_DIR = "src/main/resources"

    /**
     * Gets all input files for a given [day]
     * @param day specify the date for input files
     */
    fun getInputs(
        day: Int,
        inputFetcher: InputFetcher,
    ): List<Input> {
        val dayString = day.toString().padStart(2, '0')
        val puzzleInputFile = "$RESOURCE_DIR/day$dayString/input"
        if (!File(puzzleInputFile).exists()) {
            inputFetcher.fetchInput(day, puzzleInputFile)
        }
        val puzzleInputs =
            listOf(
                PuzzleInput(day, Part.PART_1, puzzleInputFile),
                PuzzleInput(day, Part.PART_2, puzzleInputFile),
            )

        val testInputs = mutableListOf<TestInput>()
        val testDirectories =
            File("$RESOURCE_DIR/day$dayString")
                .listFiles { pathName -> pathName.isDirectory }
                .let { dirs -> dirs?.toList() ?: emptyList() }
                .filter { dir -> Part.entries.any { part -> dir.endsWith(part.dirString) } }
                .associateWith { dir -> Part.entries.first { part -> dir.endsWith(part.dirString) } }

        for ((filePath, part) in testDirectories) {
            val testInputFiles =
                File(filePath.toString())
                    .listFiles { pathName -> pathName.isFile && !pathName.name.endsWith(".expected") }
                    .let { files -> files?.toList() ?: emptyList() }
            testInputFiles.forEach { file -> testInputs.add(TestInput(day, part, file.path)) }
        }

        return testInputs.toList() + puzzleInputs
    }
}
