package util.aocintegration

import khttp.get
import khttp.post
import util.Result

class RequestHandler(authToken: String) {
    private val cookies = mapOf("session" to authToken)

    fun fetchInput(day: Int): Result<String> {
        val url = "${BASE_URL}/day/$day/input"
        val response = get(url, cookies = cookies)
        return if (response.statusCode == 200) {
            Result.success(response.text)
        } else {
            Result.failure<String>(FAILED_RESPOSE_CODE(response.statusCode))
        }
    }

    fun submitAnswer(
        day: Int,
        part: Int,
        answer: String,
    ): Result<String> {
        val url = "$BASE_URL/day/$day/answer"
        val data = mapOf("level" to part, "answer" to answer)
        val response = post(url, cookies = cookies, data = data)
        if (response.statusCode != 200) {
            return Result.failure<String>(FAILED_RESPOSE_CODE(response.statusCode))
        }
        return Result.success(response.text)
    }

    companion object {
        const val BASE_URL = "https://adventofcode.com/2023"
        val FAILED_RESPOSE_CODE = { responseCode: Int -> "Request failed with response code $responseCode" }
    }
}
