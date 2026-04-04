package com.vtol.petpal.presentation.update

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Loading)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()


    fun getMinRequiredVersion() {
        viewModelScope.launch {
            val currentVersion = BuildConfig.VERSION_CODE
            val remoteVersion = appUseCases.getVersion()

            Log.d("TAG", "current version: $currentVersion\n min required version: ${remoteVersion.minVersion}\n latest version: ${remoteVersion.latestVersion}")

            _updateState.value = when {
                currentVersion < remoteVersion.minVersion -> UpdateState.Immediate
                currentVersion < remoteVersion.latestVersion -> UpdateState.Flexible
                else -> UpdateState.UpToDate

            }
        }
    }

    fun dismissFlexibleUpdate(){
        _updateState.value = UpdateState.UpToDate
    }

}