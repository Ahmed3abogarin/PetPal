package com.vtol.petpal.data.mapper

import com.google.gson.Gson
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.tasks.details.FoodDetails
import com.vtol.petpal.domain.model.tasks.details.MedDetails
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.domain.model.tasks.details.WalkDetails

fun Task.toUiModel(gson: Gson): TaskUi {
    val details = when (type) {
        TaskType.FEED -> runCatching { gson.fromJson(details, FoodDetails::class.java) }.getOrNull()
        TaskType.MEDICATION -> runCatching { gson.fromJson(details, MedDetails::class.java) }.getOrNull()
        TaskType.WALK -> runCatching { gson.fromJson(details, WalkDetails::class.java) }.getOrNull()
        TaskType.VET -> runCatching { gson.fromJson(details, VetDetails::class.java) }.getOrNull()
    }
    return TaskUi(
        id = id,
        petId = petId,
        title = title,
        note = note,
        type = type,
        dateTime = dateTime,
        isCompleted = isCompleted,
        repeatInterval = repeatInterval,
        details = details,
        syncStatus = syncStatus
    )
}