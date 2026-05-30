package com.vtol.petpal.domain.usecases.user

import android.net.Uri
import com.vtol.petpal.domain.repository.UserRepository
import com.vtol.petpal.domain.util.ImageCompressor
import javax.inject.Inject

class UpdateUserImageUseCase @Inject constructor(
    private val repository: UserRepository,
    private val imageCompressor: ImageCompressor
) {

    suspend operator fun invoke(uri: Uri): Result<String>  {
        val image = imageCompressor.compress(uri)
        return repository.updateUserProfileImage(image)
    }
}