package com.vtol.petpal.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.repository.AuthRepository
import com.vtol.petpal.presentation.register.AuthState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthRepository {


    // Maybe I could create data source so that the repository never touches firebase


    override suspend fun register(
        email: String,
        password: String,
    ): Result<Unit> {
        return runCatching { auth.createUserWithEmailAndPassword(email, password) }
    }

    override suspend fun login(
        email: String,
        password: String,
    ): Result<Unit> {
        return runCatching { auth.signInWithEmailAndPassword(email, password) }

    }

    override fun logout() {
        auth.signOut()
    }

    override suspend fun authState(): Flow<AuthState> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser

            trySend(
                if (user != null) AuthState.Authenticated(user) else AuthState.Unauthenticated
            )
        }
        auth.addAuthStateListener(listener)

        awaitClose { auth.removeAuthStateListener(listener) }

    }

    override fun createUserInfo(user: User) {
        val currentUser = auth.currentUser?.uid
    }
}