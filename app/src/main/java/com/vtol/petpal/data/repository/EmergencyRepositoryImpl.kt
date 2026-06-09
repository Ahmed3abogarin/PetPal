package com.vtol.petpal.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.domain.repository.EmergencyRepositoryI
import com.vtol.petpal.util.Constants.EMERGENCY_COLLECTION
import com.vtol.petpal.util.Constants.USERS_COLLECTION
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EmergencyRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): EmergencyRepositoryI {

    override fun observeContacts(): Flow<List<EmergencyContact>> =  callbackFlow {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .collection(EMERGENCY_COLLECTION)
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val contacts = snapshot?.documents
                    ?.mapNotNull { it.toObject(EmergencyContact::class.java) }
                    ?: emptyList()
                trySend(contacts)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun addContact(contact: EmergencyContact): Result<Unit> = runCatching {
        val currentUid = auth.currentUser?.uid
            ?: throw Exception("User not found")

        val contactDef = firestore
            .collection(USERS_COLLECTION)
            .document(currentUid)
            .collection(EMERGENCY_COLLECTION)
            .document()

        val newContact = contact.copy(id = contactDef.id)

        contactDef.set(newContact).await()
    }

    override suspend fun deleteContact(contact: EmergencyContact): Result<Unit> = runCatching {
        val currentUid = auth.currentUser?.uid
            ?: throw Exception("User not found")

        firestore
            .collection(USERS_COLLECTION)
            .document(currentUid)
            .collection(EMERGENCY_COLLECTION)
            .document(contact.id)
            .delete()
            .await()
    }

    override suspend fun updateContact(contact: EmergencyContact): Result<Unit> = runCatching {
        val currentUid = auth.currentUser?.uid
            ?: throw Exception("User not found")

        firestore
            .collection(USERS_COLLECTION)
            .document(currentUid)
            .collection(EMERGENCY_COLLECTION)
            .document(contact.id)
            .set(contact)
            .await()
    }
}