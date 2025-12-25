package com.vtol.petpal.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getTasks()
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            appUseCases.insertTask(task)
        }
    }


    fun getTasks() {
        _state.update { it.copy(isLoading = true) }
        appUseCases.getTasks().onEach { tasks ->
            _state.update {

                val total = tasks.size
                val completed = tasks.count { task -> task.isCompleted }

                val progress = if (total > 0) completed.toFloat() / total else 0f

                it.copy(
                    todayTasks = todayTasks(tasks),
                    upcomingTasks = upcomingTasks(tasks),
                    completedCount = completed,
                    progress = progress,
                    total = total,
                    percentage = (progress * 100).toInt()
                )
            }

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

    val completedCount: Int = 0,
    val total: Int = 0,
    val progress: Float = 0f,
    val percentage: Int = 0
)