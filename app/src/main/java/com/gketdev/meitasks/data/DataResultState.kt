package com.gketdev.meitasks.data

sealed class DataResultState<out T> {
    class Success<T>(val data: T) : DataResultState<T>()
    class Error<T>(
        val error: Throwable? = null,
        val code: Int? = null,
        val message: String? = null
    ) : DataResultState<T>()
    class Offline<T> : DataResultState<T>()
}