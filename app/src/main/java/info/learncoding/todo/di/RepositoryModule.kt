package info.learncoding.todo.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import info.learncoding.todo.data.repositories.auth.AuthRepository
import info.learncoding.todo.data.repositories.auth.AuthRepositoryImp
import info.learncoding.todo.data.repositories.todo.TodoRepository
import info.learncoding.todo.data.repositories.todo.TodoRepositoryImp
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImp(auth)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: FirebaseFirestore, auth: FirebaseAuth): TodoRepository {
        return TodoRepositoryImp(db, auth)
    }
}