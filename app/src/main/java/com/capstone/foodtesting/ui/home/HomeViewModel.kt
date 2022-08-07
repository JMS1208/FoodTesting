package com.capstone.foodtesting.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.foodtesting.data.model.Result
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    //Paging
    private val _searchPagingResult = MutableStateFlow<PagingData<Result>>(PagingData.empty())
    val searchPagingResult: StateFlow<PagingData<Result>> get() = _searchPagingResult.asStateFlow()

    fun searchFoodsPaging(query: String) {
        viewModelScope.launch {
            mainRepository.searchFoodsPaging(query,"relevant")
                .cachedIn(viewModelScope)
                .collect {
                    _searchPagingResult.value = it
                }
        }
    }
}