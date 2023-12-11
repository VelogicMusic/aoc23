package util

/**
 * Simple Result Monad Implementation used to get results when executing functions
 */
sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()

    data class Failure(val reason: String) : Result<Nothing>()

    companion object {
        fun <T> of(block: () -> T): Result<T> =
            try {
                Success(block())
            } catch (exception: Exception) {
                Failure(exception.message ?: "Unknown error")
            }

        fun <T> success(value: T) = Success(value)

        fun <T> failure(reason: String) = Failure(reason)
    }
}

fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> =
    when (this) {
        is Result.Success -> apply { action(value) }
        is Result.Failure -> this
    }

fun <T> Result<T>.onError(action: (Result.Failure) -> Unit): Result<T> =
    when (this) {
        is Result.Success -> this
        is Result.Failure -> apply { action(this) }
    }

fun <T> Result<T>.isSuccess(): Boolean =
    when (this) {
        is Result.Success -> true
        is Result.Failure -> false
    }
