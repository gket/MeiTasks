package com.gketdev.meitasks.base

import com.gketdev.meitasks.data.DataResultState
import java.io.IOException

abstract class BaseCallMapper {
    protected suspend fun <T : Any> apiCallResponse(call: suspend () -> T): DataResultState<T> {
        return try {
            val response = call()
            DataResultState.Success(response)
        } catch (exception: IOException) {
            DataResultState.Offline()
        } catch (exception: Throwable) {
            DataResultState.Error(error = exception, message = exception.message.toString())
        }
    }
}