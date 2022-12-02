package com.capstone.foodtesting.ui.restaurant.questionnaire.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantQueryRegisterAboutRestaurantViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    val aboutRestaurantQueryList: MutableLiveData<List<QueryLine>> = MutableLiveData(mutableListOf())

    fun fetchAboutRestaurantQueryList(type: Int) = viewModelScope.launch(Dispatchers.IO) {
        //원래는 백으로부터 받아와야함

        val result = repository.getDefaultQuestions(type)

        if (result.isSuccessful) {
            result.body()?.let {
                aboutRestaurantQueryList.postValue(it)
            }

        }


    }
}