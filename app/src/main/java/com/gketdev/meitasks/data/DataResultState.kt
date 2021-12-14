package com.gketdev.meitasks.data

import java.io.IOException

sealed class DataResultState<out T> {
    class Success<out T>(val data: T) : DataResultState<T>()
    class Error<out T>(
        val error: Throwable? = null,
        val code: Int? = null,
        val message: String? = null
    ) : DataResultState<T>()

    class Offline<out T>(val exception: IOException) : DataResultState<T>()
}