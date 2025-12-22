package com.vtol.petpal.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state = _state

    init {
        getAllTasks()
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            appUseCases.insertTask(task)
        }
    }

    fun getAllTasks() {

        appUseCases.getTasks().onEach { tasks ->
            _state.value = _state.value.copy(tasks = tasks)
        }.launchIn(viewModelScope)


    }

}

data class HomeState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
)