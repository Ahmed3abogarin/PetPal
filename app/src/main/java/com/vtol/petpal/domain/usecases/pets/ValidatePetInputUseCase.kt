package com.vtol.petpal.domain.usecases.pets

class ValidatePetInputUseCase {
    fun validateName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "Pet name cannot be empty")
        }
        return ValidationResult(true)
    }

    fun validateSpecie(specie: String): ValidationResult {
        if (specie.isBlank()) {
            return ValidationResult(false, "Select a specie")
        }
        return ValidationResult(true)
    }

    fun validateWeight(weight: String): ValidationResult {
        if (weight.isBlank()) {
            return ValidationResult(false, "Weight cannot be empty")
        }
        if (weight.toDoubleOrNull() == null) {
            return ValidationResult(false, "Please enter a valid number")
        }
        return ValidationResult(true)
    }
}