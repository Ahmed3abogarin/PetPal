package com.vtol.petpal.data.repository

import com.vtol.petpal.data.remote.GalleryRemoteDataSource
import com.vtol.petpal.domain.model.PetPhoto
import com.vtol.petpal.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow

class GalleryRepositoryImpl(
    private val galleryRemoteDataSource: GalleryRemoteDataSource
): GalleryRepository {
    override fun getPhotos(petId: String): Flow<List<PetPhoto>> =
        galleryRemoteDataSource.getPhotos(petId)

    override suspend fun uploadPhoto(petId: String, image: ByteArray): Result<Unit> =
        galleryRemoteDataSource.uploadPhoto(petId, image)

    override suspend fun deletePhoto(photo: PetPhoto): Result<Unit> =
        galleryRemoteDataSource.deletePhoto(photo)
}