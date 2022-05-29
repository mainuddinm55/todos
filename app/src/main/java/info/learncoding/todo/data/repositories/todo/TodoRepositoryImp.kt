package info.learncoding.todo.data.repositories.todo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import info.learncoding.todo.data.models.Response
import info.learncoding.todo.data.models.Todo
import info.learncoding.todo.utils.AppConstraint
import kotlinx.coroutines.tasks.await

class TodoRepositoryImp(
    private val db: FirebaseFirestore, private val auth: FirebaseAuth
) : TodoRepository {

    override suspend fun add(todo: Todo): Response<Todo> {
        return try {
            if (isNotLoggedIn()) {
                Response.Failure("Please check login first")
            }
            val id = db.collection(AppConstraint.getDatabaseRef(getUid())).document().id
            todo.id = id
            db.collection(AppConstraint.getDatabaseRef(getUid()))
                .document(id).set(todo).await()
            Response.Success(todo)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e.message)
        }
    }

    override suspend fun delete(todos: List<Todo>): Response<Boolean> {
        return try {
            if (isNotLoggedIn()) {
                Response.Failure("Please check login first")
            }
            todos.forEach {
                db.collection(AppConstraint.getDatabaseRef(getUid()))
                    .document(it.id ?: return Response.Failure("Please give valid todo")).delete()
                    .await()
            }
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e.message)
        }
    }

    override suspend fun update(todo: Todo): Response<Todo> {
        return try {
            if (isNotLoggedIn()) {
                Response.Failure("Please check login first")
            }
            db.collection(AppConstraint.getDatabaseRef(getUid()))
                .document(todo.id ?: return Response.Failure("Please give valid todo")).set(todo)
                .await()
            Response.Success(todo)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e.message)
        }
    }

    override fun getTodosQuery(): Query {
        return db.collection(AppConstraint.getDatabaseRef(getUid()))
            .orderBy("date", Query.Direction.ASCENDING)
            .limit(AppConstraint.PAGE_SIZE.toLong())
    }

    private fun isLoggedIn() = auth.currentUser != null

    private fun isNotLoggedIn() = !isLoggedIn()

    private fun getUid(): String {
        return auth.uid ?: throw Exception("User not logged in")
    }
}