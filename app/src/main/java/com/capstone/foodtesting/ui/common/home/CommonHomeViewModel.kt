package com.capstone.foodtesting.ui.common.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.member.Testing
import com.capstone.foodtesting.data.model.member.TestingItem
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    fun getMember() = mainRepository.getMember()

    val latestAddressInfo: StateFlow<AddressInfo?> = mainRepository.getLatestAddressInfo().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    private var _newRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> = MutableLiveData()

    val newRestaurantListLiveData: LiveData<List<Restaurant>> get() = _newRestaurantListMutableLiveData

    fun requestHomeRestaurantList(x: String, y: String) = viewModelScope.launch {
        try {
            val latitude = y.toDouble()
            val longitude = x.toDouble()

            val response = mainRepository.getHomeNewRestaurantList(latitude, longitude)

            if(response.isSuccessful) {
                response.body()?.markets?.let {
                    _newRestaurantListMutableLiveData.postValue(it)
                }
            }

        } catch (E: Exception) {
            Log.e("TAG", "${E.message}")
        }

    }

    private var _newMenuListMutableLiveData: MutableLiveData<List<Menu>> = MutableLiveData()

    val newMenuListLiveData: LiveData<List<Menu>> get() = _newMenuListMutableLiveData

    fun fetchNewMenuListLiveData() = viewModelScope.launch {
        val response = mainRepository.getNewMenuList()

        if (response.isSuccessful) {
            response.body()?.menus?.let {
                _newMenuListMutableLiveData.postValue(it)
            }
        }

    }


}