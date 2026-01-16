package com.vtol.petpal.util

object PetStoragePaths {
    fun petProfileStoragePath(userId: String, petId: String) : String =
        "pets/$userId/$petId/profile/profile.jpg"
}