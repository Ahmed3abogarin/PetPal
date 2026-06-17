package com.vtol.petpal.util

object AppStoragePaths {
    fun petProfileStoragePath(userId: String, petId: String): String =
        "pets/$userId/$petId/profile/profile.webp"

    fun petGalleryStoragePath(userId: String, petId: String, photoId: String): String =
        "pets/$userId/$petId/gallery/$photoId.webp"

    fun userProfileStoragePath(userId: String): String =
        "profile_images/$userId/profile.webp"

}