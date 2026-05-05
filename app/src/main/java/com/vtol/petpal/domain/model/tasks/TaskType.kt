package com.vtol.petpal.domain.model.tasks

import com.vtol.petpal.R

enum class TaskType(val txt: String, val icon: Int) {
    VET("Vet", R.drawable.ic_vets),
    FEED("Food", R.drawable.ic_feed),
    MEDICATION("Medication", R.drawable.ic_pharmacy),
    WALK("Walk", R.drawable.ic_parks),
}


