package info.learncoding.todo.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import info.learncoding.todo.data.models.Response
import info.learncoding.todo.data.repositories.auth.AuthRepository
import info.learncoding.todo.data.models.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginResponse = MutableLiveData<State<FirebaseUser>>()
    val loginResponse: LiveData<State<FirebaseUser>> = _loginResponse

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginResponse.postValue(State.Loading)
            when (val response = authRepository.login(email, password)) {
                is Response.Failure -> _loginResponse.postValue(State.Error(response.msg))
                is Response.Success -> _loginResponse.postValue(State.Success(response.data))
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }
}