package com.gketdev.meitasks.api

import com.gketdev.meitasks.data.ResultResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface TaskApiService {
    @POST("search/")
    suspend fun searchTask(
        @Body filter: RequestBody,
    ): ResultResponse

}