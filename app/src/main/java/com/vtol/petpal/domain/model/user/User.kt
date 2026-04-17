package com.vtol.petpal.domain.model.user

import androidx.annotation.Keep

@Keep
data class User(
    val uid: String = "",
    val imgPath: String = "",
    val name: String = "",
    val email: String = ""
)
