package com.vtol.petpal.presentation.pets.edit


sealed class EditUiEffect {
    object NavigateUp : EditUiEffect()
    class ShowToastMessage(val error: String) : EditUiEffect()
}