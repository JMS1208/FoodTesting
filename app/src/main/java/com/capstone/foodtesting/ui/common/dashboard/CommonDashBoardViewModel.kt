package com.capstone.foodtesting.ui.common.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.repository.MainRepository
import com.capstone.foodtesting.ui.common.dashboard.category.Hilt_WesternFoodFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CommonDashBoardViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {


    private var _allRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val allRestaurantListLiveData: LiveData<List<Restaurant>> get() = _allRestaurantListMutableLiveData

    private var _chineseRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val chineseRestaurantListLiveData: LiveData<List<Restaurant>> get() = _chineseRestaurantListMutableLiveData

    private var _dessertRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val dessertRestaurantListLiveData: LiveData<List<Restaurant>> get() = _dessertRestaurantListMutableLiveData

    private var _fastFoodRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val fastFoodRestaurantListLiveData: LiveData<List<Restaurant>> get() = _fastFoodRestaurantListMutableLiveData

    private var _flourFoodRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val flourFoodRestaurantListLiveData: LiveData<List<Restaurant>> get() = _flourFoodRestaurantListMutableLiveData

    private var _japaneseFoodRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val japaneseFoodRestaurantListLiveData: LiveData<List<Restaurant>> get() = _japaneseFoodRestaurantListMutableLiveData

    private var _koreanFoodRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val koreanFoodRestaurantListLiveData: LiveData<List<Restaurant>> get() = _koreanFoodRestaurantListMutableLiveData

    private var _otherFoodRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val otherFoodRestaurantListLiveData: LiveData<List<Restaurant>> get() = _otherFoodRestaurantListMutableLiveData

    private var _westernFoodRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val westernFoodRestaurantListLiveData: LiveData<List<Restaurant>> get() = _westernFoodRestaurantListMutableLiveData

    private var _allFoodRestaurantListMutableLiveData: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val allFoodRestaurantListLiveData: LiveData<List<Restaurant>> get() = _allFoodRestaurantListMutableLiveData


    fun setupCategoryRestaurantList(latitude: Double, longitude: Double) = viewModelScope.launch {

        val categories = listOf("kf", "cf", "jf", "de", "ff", "sf", "el", "wf", "all")

        for (category in categories) {
            val response = getRestaurantByCategory(category, latitude, longitude)
            handleResponse(category, response)
        }

    }

    fun setupCategoryRestaurantListFromChildFragment(
        category: String,
        latitude: Double,
        longitude: Double
    ) = viewModelScope.launch {
        val response = getRestaurantByCategory(category, latitude, longitude)
        handleResponse(category, response)

    }


    private fun handleResponse(category: String, response: Response<List<Restaurant>>) {
        if (response.isSuccessful) {
            val restaurantList = response.body()

            restaurantList?.let {
                Log.d("TAG", "Response 핸들링 $it")
                when (category) {
                    "kf" -> {
                        _koreanFoodRestaurantListMutableLiveData.postValue(it)
                    }
                    "cf" -> {
                        _chineseRestaurantListMutableLiveData.postValue(it)
                    }
                    "jf" -> {
                        _japaneseFoodRestaurantListMutableLiveData.postValue(it)
                    }
                    "de" -> {
                        _dessertRestaurantListMutableLiveData.postValue(it)
                    }
                    "ff" -> {
                        _fastFoodRestaurantListMutableLiveData.postValue(it)
                    }
                    "sf" -> {
                        _flourFoodRestaurantListMutableLiveData.postValue(it)
                    }
                    "el" -> {
                        _otherFoodRestaurantListMutableLiveData.postValue(it)
                    }
                    "wf" -> {
                        _westernFoodRestaurantListMutableLiveData.postValue(it)
                    }
                    "all" -> {
                        _allFoodRestaurantListMutableLiveData.postValue(it)
                    }
                }
            }

        }

    }


    val latestAddressInfo: StateFlow<AddressInfo?> = repository.getLatestAddressInfo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    private suspend fun getRestaurantByCategory(
        category: String,
        latitude: Double,
        longitude: Double
    ): Response<List<Restaurant>> {
        return repository.getRestaurantByCategory(category, latitude, longitude)
    }
}