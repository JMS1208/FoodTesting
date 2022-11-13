package com.capstone.foodtesting.ui.common.info.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteDialogViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    fun insertFavoriteRestaurant(restaurant: Restaurant) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFavoriteRestaurant(restaurant)
    }

    fun getAllFavoriteRestaurant(): Flow<List<Restaurant>?> {
        return repository.getAllFavoriteRestaurant()
    }

    fun deleteFavoriteRestaurant(restaurant: Restaurant) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFavoriteRestaurant(restaurant)
    }

}