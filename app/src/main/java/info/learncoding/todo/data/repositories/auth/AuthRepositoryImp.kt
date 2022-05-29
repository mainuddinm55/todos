package info.learncoding.todo.data.repositories.auth

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import info.learncoding.todo.data.models.Response
import kotlinx.coroutines.tasks.await
import kotlin.jvm.Throws

class AuthRepositoryImp(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    override suspend fun registration(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Response<FirebaseUser> {
        try {
            val currentUser = firebaseAuth.currentUser
            return if (currentUser == null) {
                if (validateUser(email, password, confirmPassword)) {
                    val result = firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .await()
                    val profileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    result.user?.updateProfile(profileChangeRequest)?.await()
                    Response.Success(result.user!!)
                } else {
                    Response.Failure("User validation failed")
                }
            } else {
                Response.Success(currentUser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.Failure(e.message)
        }
    }

    override suspend fun login(email: String, password: String): Response<FirebaseUser> {
        return try {
            if (firebaseAuth.currentUser != null) {
                Response.Success(firebaseAuth.currentUser!!)
            }
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Response.Success(result.user!!)
            } else {
                Response.Failure("Invalid email given")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e.message)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    @Throws(Exception::class)
    private fun validateUser(email: String, password: String, confirmPassword: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw Exception("Email is not valid")
        } else if (password.length < 6) {
            throw Exception("Password must 6 or more")
        } else if (password === confirmPassword) {
            throw Exception("Password does not match")
        }
        return true
    }
}