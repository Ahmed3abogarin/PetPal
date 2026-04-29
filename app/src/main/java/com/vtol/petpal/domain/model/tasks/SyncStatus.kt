package com.vtol.petpal.domain.model.tasks

enum class SyncStatus {
    SYNCED,       // matches backend
    PENDING,      // created locally, not yet pushed
    MODIFIED,     // edited locally, needs re-push
    DELETED       // soft-deleted, needs delete pushed to backend
}