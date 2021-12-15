package com.gketdev.meitasks.ui.home

import com.gketdev.meitasks.data.TaskEntity

sealed class HomeViewState {
    object InitialLoading : HomeViewState()
    object RequestLoading : HomeViewState()
    data class Tasks(val list: List<TaskEntity>) : HomeViewState()
    data class Error(val error: String?) : HomeViewState()
}