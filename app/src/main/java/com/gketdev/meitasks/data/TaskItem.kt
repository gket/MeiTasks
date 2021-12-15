package com.gketdev.meitasks.data

data class TaskItem(
    val task: Task,
    val section: Section?,
    val project: Project?
)