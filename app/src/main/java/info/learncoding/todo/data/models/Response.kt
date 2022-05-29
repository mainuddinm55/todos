package info.learncoding.todo.data.models

sealed class Response<out T> {
    data class Success<out T>(val data: T, val paginateKey: T? = null) : Response<T>()
    data class Failure(val msg: String?) : Response<Nothing>()
}
