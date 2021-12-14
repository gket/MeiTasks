package com.gketdev.meitasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val taskId: Int,
    val taskName: String,
    val status: Int,
    val projectName: String
)