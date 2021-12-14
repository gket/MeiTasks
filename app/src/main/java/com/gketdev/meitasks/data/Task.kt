package com.gketdev.meitasks.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Task(
    @Json(name = "id")
    val taskId: Int,
    @Json(name = "name")
    val taskName: String,
    @Json(name = "status")
    val status: Int,
    @Json(name = "section_id")
    val sectionId: Int
)
