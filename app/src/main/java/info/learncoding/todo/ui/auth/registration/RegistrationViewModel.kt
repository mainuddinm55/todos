package info.learncoding.todo.ui.auth.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import info.learncoding.todo.data.models.Response
import info.learncoding.todo.data.repositories.auth.AuthRepository
import info.learncoding.todo.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registrationResponse = MutableLiveData<State<FirebaseUser>>()
    val registrationResponse: LiveData<State<FirebaseUser>> = _registrationResponse

    fun login(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _registrationResponse.postValue(State.Loading)
            when (val response = authRepository.registration(
                name, email, password, confirmPassword
            )) {
                is Response.Failure -> _registrationResponse.postValue(State.Error(response.msg))
                is Response.Success -> _registrationResponse.postValue(State.Success(response.data))
            }
        }
    }
}