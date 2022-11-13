package com.capstone.foodtesting.ui.common.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

@HiltViewModel
class CommonDashBoardViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {


    val latestAddressInfo: StateFlow<AddressInfo?> = repository.getLatestAddressInfo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    suspend fun getRestaruantByCategory(
        category: String,
        latitude: Double,
        longitude: Double
    ): Response<List<Restaurant>> {
        return repository.getRestaruantByCategory(category, latitude, longitude)
    }
}