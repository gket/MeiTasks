package com.gketdev.meitasks.source

import android.util.ArrayMap
import com.gketdev.meitasks.api.TaskApiService
import com.gketdev.meitasks.base.BaseCallMapper
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.http.Field
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: TaskApiService) :
    BaseCallMapper() {

    fun searchQuery(query: String, status: Int?) = flow {
        val filteredText = mutableMapOf<String, Any>()
        filteredText["text"] = query
        if (status != null) {
            filteredText["status"] = intArrayOf(status)
        }
        val filteredStr = JSONObject(filteredText as Map<*, *>).toString()
        val jsonParams: MutableMap<String?, Any?> = ArrayMap()
        jsonParams["filter"] = filteredStr
        jsonParams["response_format"] = "object"
        val str = JSONObject(jsonParams).toString().toRequestBody(
            "application/json; charset=utf-8".toMediaTypeOrNull()
        )
        emit(apiCallResponse {
            apiService.searchTask(str).results
        })
    }

}