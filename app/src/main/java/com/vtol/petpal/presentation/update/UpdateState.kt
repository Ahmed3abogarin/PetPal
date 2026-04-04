package com.vtol.petpal.presentation.update

sealed class UpdateState {
    object Loading : UpdateState()
    object Immediate : UpdateState()
    object Flexible : UpdateState()
    object UpToDate : UpdateState()
}