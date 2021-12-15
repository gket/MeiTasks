package com.gketdev.meitasks.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val taskId: Int,
    val taskName: String,
    val status: Int,
    val projectName: String
) : Parcelable