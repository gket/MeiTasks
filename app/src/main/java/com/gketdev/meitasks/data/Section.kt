package com.gketdev.meitasks.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Section(
    @Json(name = "id")
    val sectionId: Int,
    @Json(name = "project_id")
    val projectId: Int
)
