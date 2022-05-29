package info.learncoding.todo.ui.add

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import info.learncoding.todo.R
import info.learncoding.todo.data.models.Response
import info.learncoding.todo.data.models.Todo
import info.learncoding.todo.data.repositories.todo.TodoRepository
import info.learncoding.todo.data.models.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = AddTodoFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _todoResponse = MutableLiveData<State<Todo>>()
    val todoResponse: LiveData<State<Todo>> = _todoResponse

    var title: String = args.todo?.title ?: ""

    val menuClickedListener = Toolbar.OnMenuItemClickListener {
        if (it.itemId == R.id.action_add) {
            addTodo()
            return@OnMenuItemClickListener true
        }
        return@OnMenuItemClickListener false
    }


    private fun addTodo() {
        if (title.isEmpty()) {
            _todoResponse.postValue(State.Error("Please write something"))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _todoResponse.postValue(State.Loading)
                val response = if (args.todo == null) {
                    todoRepository.add(Todo("", title, Date()))
                } else {
                    val todo = args.todo
                    todo?.title = title
                    todoRepository.update(todo!!)
                }
                when (response) {
                    is Response.Failure -> _todoResponse.postValue(State.Error(response.msg))
                    is Response.Success -> _todoResponse.postValue(State.Success(response.data))
                }
            }
        }
    }
}