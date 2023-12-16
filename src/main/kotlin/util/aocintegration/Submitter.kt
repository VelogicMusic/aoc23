package util.aocintegration

import util.IOHandler
import util.Part
import util.Result

class Submitter(val requestHandler: RequestHandler, val noSubmit: Boolean) {
    fun submitAnswer(
        day: Int,
        part: Part,
        answer: String,
    ) {
        if (noSubmit) return
        if (!IOHandler.queryYNResponse("Do you want to submit the answer?")) return
        val response = requestHandler.submitAnswer(day, part.number, answer)
        when (response) {
            is Result.Success ->
                if ("correct" in response.value) {
                    println("Submitted correct answer!")
                } else {
                    println("Answer incorrect.")
                }
            is Result.Failure -> println(response.reason)
        }
    }
}
