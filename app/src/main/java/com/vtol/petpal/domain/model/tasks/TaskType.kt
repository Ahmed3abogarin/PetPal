package com.vtol.petpal.domain.model.tasks

import com.vtol.petpal.R

enum class TaskType(val txt: String, val icon: Int) {
    VET("Vet", R.drawable.ic_task_vet),
    FEED("Food", R.drawable.ic_task_feed),
    MEDICATION("Meds", R.drawable.ic_task_meds),
    WALK("Walk", R.drawable.ic_task_walk),
}


