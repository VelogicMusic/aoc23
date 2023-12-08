package util

import java.io.File
import kotlin.streams.toList

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

sealed class Input(val day: Int, val part: Part, fileName: String) {
    private val inputReader = File(fileName).bufferedReader()

    val text = inputReader.readText().trim()

    val lines = text.lines()
}

class TestInput(day: Int, part: Part, fileName: String) : Input(day, part, fileName) {
    private val resultReader = File("$fileName.expected").bufferedReader()
    val expectedResult = resultReader.readText().trim()
}

class PuzzleInput(day: Int, part: Part, fileName: String) : Input(day, part, fileName)

object InputReader {
    private const val RESOURCE_DIR = "src/main/resources"

    fun getInputs(day: Int): List<Input> {
        val dayString = day.toString().padStart(2, '0')
        val puzzleInputs =
            "$RESOURCE_DIR/day$dayString/input".let { fileName ->
                listOf(PuzzleInput(day, Part.PART_1, fileName), PuzzleInput(day, Part.PART_2, fileName))
            }

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
