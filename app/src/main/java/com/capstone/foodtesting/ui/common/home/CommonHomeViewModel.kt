package com.capstone.foodtesting.ui.common.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.member.Testing
import com.capstone.foodtesting.data.model.member.TestingItem
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommonHomeViewModel @Inject constructor(
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
    private val _userInfoResult = MutableLiveData<Testing?>()

    val userInfoResult get() = _userInfoResult

    fun getUserInfo(uuid: String) {
        viewModelScope.launch {
            val response = mainRepository.getUserInfo(uuid)

            if (response.isSuccessful) {
                val member = response.body()
                _userInfoResult.postValue(member)
            }
        }
    }



}