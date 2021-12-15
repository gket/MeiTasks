package com.gketdev.meitasks.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gketdev.meitasks.data.DataResultState
import com.gketdev.meitasks.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _viewState = MutableLiveData<HomeViewState>(HomeViewState.InitialLoading)
    val viewState: LiveData<HomeViewState> = _viewState

    fun searchQuery(query: String, status: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.postValue(HomeViewState.RequestLoading)
            repository.searchQuery(query, status).collect {
                when (it) {
                    is DataResultState.Error -> {
                        _viewState.postValue(HomeViewState.Error(it.message))
                    }
                    is DataResultState.Success -> {
                        _viewState.postValue(HomeViewState.Tasks(it.data))
                    }
                }
            }
        }
    }


}