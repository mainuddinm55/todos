package info.learncoding.todo.ui.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import info.learncoding.todo.data.models.Response
import info.learncoding.todo.data.models.Todo
import info.learncoding.todo.data.repositories.auth.AuthRepository
import info.learncoding.todo.data.repositories.todo.TodoRepository
import info.learncoding.todo.data.source.FirestorePagingSource
import info.learncoding.todo.utils.AppConstraint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val todos = Pager(
        PagingConfig(pageSize = AppConstraint.PAGE_SIZE)
    ) {
        FirestorePagingSource(todoRepository.getTodosQuery())
    }.flow.cachedIn(viewModelScope)


    fun deleteTodos(todos: List<Todo>): MutableLiveData<Response<Boolean>> {
        val response = MutableLiveData<Response<Boolean>>()
        viewModelScope.launch(Dispatchers.IO) {
            response.postValue(todoRepository.delete(todos))
        }
        return response
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.logout()
        }
    }

}