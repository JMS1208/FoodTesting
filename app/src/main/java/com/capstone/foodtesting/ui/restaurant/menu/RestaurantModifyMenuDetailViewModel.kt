package com.capstone.foodtesting.ui.restaurant.menu

import android.net.Uri
import android.os.Message
import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.model.file.process.ImageHashResponse
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RestaurantModifyMenuDetailViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    suspend fun uploadRestaurantImage(uri: Uri): Response<ImageHashResponse> {
        return repository.uploadRestaurantImage(uri)
    }

    suspend fun modifyMenu(
        menu: Menu
    ): Response<MessageResponse> {
        return repository.modifyMenu(menu)
    }
}