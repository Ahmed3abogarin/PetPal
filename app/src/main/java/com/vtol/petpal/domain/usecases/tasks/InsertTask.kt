package com.vtol.petpal.domain.usecases.tasks

import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.repository.AppRepository

class InsertTask(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(task: Task){
        appRepository.insertTask(task)
    }
}