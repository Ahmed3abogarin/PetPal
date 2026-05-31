package com.vtol.petpal.domain.model.user

data class ProviderInfo(
    val isEmailProvider: Boolean,
    val providerName: String? // null if email/password
)