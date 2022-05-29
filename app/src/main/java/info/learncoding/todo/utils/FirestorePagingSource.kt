package info.learncoding.todo.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import info.learncoding.todo.data.models.Todo
import kotlinx.coroutines.tasks.await

class FirestorePagingSource(
    private val query: Query
) : PagingSource<QuerySnapshot, Todo>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Todo>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Todo> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleTodo = currentPage.documents.lastOrNull()
                ?: throw Exception("Create your first todo")
            val nextPage = query.startAfter(lastVisibleTodo).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(Todo::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}