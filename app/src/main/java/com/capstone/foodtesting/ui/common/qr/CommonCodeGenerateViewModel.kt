package com.capstone.foodtesting.ui.common.qr

import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.repository.MainRepository
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CommonCodeGenerateViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    suspend fun getStoreInfoByCustomerId(uuid : String): Response<List<RestaurantResponse>> {
        return repository.getStoreInfoByCustomerId(uuid)
    }
}