package com.capstone.foodtesting.ui.restaurant.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RestaurantModifyMenuViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _menuListLiveData: MutableLiveData<List<Menu>> = MutableLiveData(emptyList())
    val menuListLiveData: LiveData<List<Menu>> get() = _menuListLiveData

    suspend fun fetchRestaurantInfoByRegNum(regNum: String) =
        withContext(viewModelScope.coroutineContext) {

            val response = repository.getStoreInfoByRegNum(regNum)

            if (response.isSuccessful) {
                response.body()?.get(0)?.menuList?.let {
                    _menuListLiveData.postValue(it)
                }
                true
            } else {
                false
            }

        }

    suspend fun deleteMenu(regNum: String, menuUUID: String): Response<MessageResponse> {
        return repository.deleteMenu(regNum, menuUUID)
    }

    suspend fun getStoreInfoByRegNum(
        reg_num: String
    ): Response<List<RestaurantResponse>> {
        return repository.getStoreInfoByRegNum(reg_num)
    }

    suspend fun modifyMenu(menu: Menu): Response<MessageResponse> {
        return repository.modifyMenu(menu)
    }

}