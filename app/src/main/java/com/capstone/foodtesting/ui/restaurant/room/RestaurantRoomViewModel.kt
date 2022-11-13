package com.capstone.foodtesting.ui.restaurant.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.kakao.search.address.AddressSearchResponse
import com.capstone.foodtesting.data.model.naver.geo.NaverGeoResponse
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class RestaurantRoomViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    suspend fun getStoreInfoByRegNum(regNum: String): Response<List<RestaurantResponse>> {
        return repository.getStoreInfoByRegNum(regNum)
    }

    fun insertFavoriteRestaurant(restaurant: Restaurant) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFavoriteRestaurant(restaurant)
    }

    fun deleteFavoriteRestaurant(restaurant: Restaurant) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFavoriteRestaurant(restaurant)
    }

//    fun doExistsFavoriteRestaurant(restaurant: Restaurant): StateFlow<Restaurant?> {
//        return repository.doExistsFavoriteRestaurant(restaurant.reg_num).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
//    }

    fun doExistsFavoriteRestaurant(restaurant: Restaurant): Restaurant? {
        return repository.doExistsFavoriteRestaurant(restaurant.reg_num)
    }

    fun getLatestAddressInfo(): Flow<AddressInfo?> {
        return repository.getLatestAddressInfo()
    }


    suspend fun searchGeoInfo(query: String, longitude: String, latitude: String): Response<NaverGeoResponse> {
        val coordinate = "${longitude},${latitude}"
        return repository.searchGeoInfo(query, coordinate)
    }



}