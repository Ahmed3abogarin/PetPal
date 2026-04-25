package com.vtol.petpal.domain.usecases.pets

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)