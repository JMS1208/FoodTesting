package com.capstone.foodtesting.ui.restaurant.register

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.model.file.process.ImageHashResponse
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class RestaurantRegisterViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    suspend fun getStoreInfoByRegNum(reg_num: String): Response<List<RestaurantResponse>> {
        return repository.getStoreInfoByRegNum(reg_num)
    }

    suspend fun uploadRestaurantImage( imageUriPath: String): Response<ImageHashResponse> {

        val imageFile = File(imageUriPath)

        return repository.uploadRestaurantImage(imageFile)

    }

}