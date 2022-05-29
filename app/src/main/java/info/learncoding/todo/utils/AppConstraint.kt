package info.learncoding.todo.utils

object AppConstraint {
    private const val DATABASE_REF = "todos"

    const val PAGE_SIZE = 10
    const val TODO_CHANGE_LISTENER_KEY = "todo_change"

    fun getDatabaseRef(uid: String): String {
        return "$DATABASE_REF/$uid/$DATABASE_REF"
    }
}