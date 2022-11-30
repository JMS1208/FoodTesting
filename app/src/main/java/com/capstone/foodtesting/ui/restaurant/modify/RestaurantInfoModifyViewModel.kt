package com.capstone.foodtesting.ui.restaurant.modify

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.model.file.process.ImageHashResponse
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoModifyViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _complexityLiveData: MutableLiveData<Int> = MutableLiveData()
    val complexityLiveData: LiveData<Int> get() = _complexityLiveData

    fun setComplexity(complexity: Int) {
        _complexityLiveData.postValue(complexity)
    }

    suspend fun uploadRestaurantImage(uri: Uri): Response<ImageHashResponse> {
        return repository.uploadRestaurantImage(uri)
    }

    suspend fun modifyRestaurantInfo(restaurantInfo: Restaurant): Response<MessageResponse> {
        return repository.modifyRestaurantInfo(restaurantInfo )
    }
}