package com.vtol.petpal.presentation.add_pet

sealed class UiEffects {
    object NavigateUp : UiEffects()
    class ShowToastMessage(val error: String) : UiEffects()
}