package info.learncoding.todo.data.repositories.auth

import com.google.firebase.auth.FirebaseUser
import info.learncoding.todo.data.models.Response

interface AuthRepository {

    suspend fun registration(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Response<FirebaseUser>

    suspend fun login(email: String, password: String): Response<FirebaseUser>

    suspend fun logout()

    fun isLoggedIn(): Boolean

}