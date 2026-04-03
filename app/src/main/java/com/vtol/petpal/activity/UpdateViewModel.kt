package com.vtol.petpal.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Update
import com.vtol.petpal.BuildConfig
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val appUseCases: AppUseCases
): ViewModel() {

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Loading)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()



    fun getMinRequiredVersion(){
        viewModelScope.launch {
            val currentVersion = BuildConfig.VERSION_CODE
            val remoteVersion = appUseCases.getVersion()

            _updateState.value = when {
                currentVersion < remoteVersion.minVersion -> UpdateState.Immediate
                currentVersion < remoteVersion.latestVersion -> UpdateState.Flexible
                else -> UpdateState.UpToDate

            }

        }
    }

}

sealed class UpdateState{
    object Loading: UpdateState()
    object Immediate: UpdateState()
    object Flexible: UpdateState()
    object UpToDate: UpdateState()
}