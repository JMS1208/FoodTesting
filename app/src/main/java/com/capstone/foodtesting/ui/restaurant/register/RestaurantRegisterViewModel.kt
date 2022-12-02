package com.capstone.foodtesting.ui.restaurant.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.model.file.process.ImageHashResponse
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class RestaurantRegisterViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    suspend fun getStoreInfoByRegNum(reg_num: String): Response<List<RestaurantResponse>> {
        return repository.getStoreInfoByRegNum(reg_num)
    }

    suspend fun uploadRestaurantImage(uri: Uri): Response<ImageHashResponse> {

        return repository.uploadRestaurantImage(uri)

    }

    val getMemberInfo: Flow<Member?> = repository.getMember()

    suspend fun registerRestaurantInfo(restaurant: Restaurant): Response<MessageResponse> {
        return repository.registerRestaurant(restaurant)
    }




}