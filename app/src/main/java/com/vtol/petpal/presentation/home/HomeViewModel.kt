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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state = _state

    init {
        getTasks()
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            appUseCases.insertTask(task)
        }
    }


    fun getTasks() {
        appUseCases.getTasks().onEach { tasks ->
            _state.value = _state.value.copy(todayTasks = todayTasks(tasks))
            _state.value = _state.value.copy(upcomingTasks = upcomingTasks(tasks))
        }.launchIn(viewModelScope)
    }


    fun todayTasks(tasks: List<Task>) = tasks.filter {
        val taskDate = Instant.ofEpochMilli(it.dateTime).atZone(ZoneId.systemDefault()).toLocalDate()
        taskDate == LocalDate.now()
    }

    fun upcomingTasks(tasks: List<Task>) = tasks.filter {
        val taskDate = Instant.ofEpochMilli(it.dateTime).atZone(ZoneId.systemDefault()).toLocalDate()
        taskDate.isAfter(LocalDate.now())
    }

}

data class HomeState(
    val todayTasks: List<Task> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
)