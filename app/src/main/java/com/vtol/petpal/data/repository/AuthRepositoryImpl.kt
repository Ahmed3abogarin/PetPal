package com.vtol.petpal.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.data.local.TasksDB
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.repository.AuthRepository
import com.vtol.petpal.presentation.register.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dataStore: DataStore<Preferences>,
    private val db: TasksDB
) : AuthRepository {


    // Maybe I could create data source so that the repository never touches firebase


    override suspend fun register(
        user: User,
        password: String,
    ): Result<Unit> {
        return runCatching {

            //  Create user in Firebase Auth (WAIT for result)
            val authResult = auth
                .createUserWithEmailAndPassword(user.email, password)
                .await()

            val uid = authResult.user?.uid
                ?: throw IllegalStateException("User UID is null")

            // Create user object with correct UID
            val userWithUid = user.copy(uid = uid)

            // Save user in Firestore
            firestore.collection("Users")
                .document(uid)
                .set(userWithUid)
                .await()
        }
    }


    override suspend fun login(
        email: String,
        password: String,
    ): Result<Unit> {
        return runCatching { auth.signInWithEmailAndPassword(email, password).await() }

    }

    override fun logout() {
        CoroutineScope(Dispatchers.IO).launch{
            db.clearAllTables()
        }
        auth.signOut()
    }

    override fun authState(): Flow<AuthState> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser

            trySend(
                if (user != null) AuthState.Authenticated(user) else AuthState.Unauthenticated
            )
        }
        auth.addAuthStateListener(listener)

        awaitClose { auth.removeAuthStateListener(listener) }

    }

    override fun isCompleted(): Flow<Boolean> =
        dataStore.data.map {
            it[OnboardingPrefs.COMPLETED] ?: false
        }

    override suspend fun setCompleted() {
        dataStore.edit {
            it[OnboardingPrefs.COMPLETED] = true
        }
    }
}