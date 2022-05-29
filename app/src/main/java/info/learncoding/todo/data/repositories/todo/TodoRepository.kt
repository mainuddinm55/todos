package info.learncoding.todo.data.repositories.todo

import com.google.firebase.firestore.Query
import info.learncoding.todo.data.models.Response
import info.learncoding.todo.data.models.Todo

interface TodoRepository {

    suspend fun add(todo: Todo): Response<Todo>

    suspend fun delete(todos: List<Todo>): Response<Boolean>

    suspend fun update(todo: Todo): Response<Todo>

    fun getTodosQuery(): Query
}