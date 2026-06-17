package com.vtol.petpal.domain.model

import androidx.annotation.Keep
import java.util.UUID

@Keep
data class PetPhoto(
    val id: String = UUID.randomUUID().toString(),
    val petId: String = "",
    val url: String = "",
    val caption: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)