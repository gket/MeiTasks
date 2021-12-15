package com.gketdev.meitasks.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskDetail(
    @Json(name = "sections")
    val sections: List<Section>,
    @Json(name = "tasks")
    val tasks: List<Task>,
    @Json(name = "projects")
    val projects: List<Project>
)
