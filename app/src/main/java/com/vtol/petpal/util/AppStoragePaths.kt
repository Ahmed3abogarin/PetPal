package com.vtol.petpal.util

object AppStoragePaths {
    fun petProfileStoragePath(userId: String, petId: String) : String =
        "pets/$userId/$petId/profile/profile.webp"

    fun userProfileStoragePath(userId: String) : String =
        "profile_images/$userId/profile.webp"
}