package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.EmergencyContact
import kotlinx.coroutines.flow.Flow

interface EmergencyRepository {

    fun observeContacts(): Flow<List<EmergencyContact>>

    suspend fun addContact(contact: EmergencyContact): Result<Unit>

    suspend fun deleteContact(contact: EmergencyContact): Result<Unit>

    suspend fun updateContact(contact: EmergencyContact): Result<Unit>
}