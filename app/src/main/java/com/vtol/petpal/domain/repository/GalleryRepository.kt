package com.vtol.petpal.domain.repository

import com.vtol.petpal.domain.model.PetPhoto
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getPhotos(petId: String): Flow<List<PetPhoto>>
    suspend fun deletePhoto(photo: PetPhoto): Result<Unit>
    suspend fun uploadPhoto(petId: String, image: ByteArray): Result<Unit>
}