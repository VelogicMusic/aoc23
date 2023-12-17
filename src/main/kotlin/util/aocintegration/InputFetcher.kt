package util.aocintegration

import util.Result
import java.io.File

class InputFetcher(val requestHandler: RequestHandler) {
    fun fetchInput(
        day: Int,
        filePath: String,
    ) {
        val response = requestHandler.fetchInput(day)
        when (response) {
            is Result.Success -> {
                println("Fetched Input for day $day.")
                writeToFile(filePath, response.value)
            }
            is Result.Failure -> println("Error: Could not fetch input for day $day")
        }
    }

    private fun writeToFile(
        filePath: String,
        content: String,
    ) {
        val directory = filePath.substringBefore("/input")
        File(directory).mkdir()
        File(filePath).bufferedWriter().use { out -> out.write(content) }
    }
}
