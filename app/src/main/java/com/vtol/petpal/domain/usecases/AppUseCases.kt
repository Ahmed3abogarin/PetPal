package com.vtol.petpal.domain.usecases

data class AppUseCases(
    val addPet: AddPet,
    val getPets: GetPets,
    val getPet: GetPet,
)