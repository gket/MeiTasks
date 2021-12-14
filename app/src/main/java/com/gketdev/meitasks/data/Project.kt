package com.gketdev.meitasks.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Project(
    @Json(name = "id")
    val projectId: Int,
    @Json(name = "name")
    val projectName: String
)
