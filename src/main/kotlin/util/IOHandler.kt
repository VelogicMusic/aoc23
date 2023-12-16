package util

object IOHandler {
    fun queryYNResponse(
        query: String,
        defaultNo: Boolean = true,
    ): Boolean {
        println("$query [${if (defaultNo) 'y' else 'Y'}${if (defaultNo) 'N' else 'n'}]")
        val response = runCatching { readln().firstOrNull()?.lowercaseChar() }.getOrElse { null }
        return when (response) {
            null -> if (defaultNo) false else true
            'y' -> true
            'n' -> false
            else -> throw IllegalStateException("Unknown response: $response")
        }
    }
}
