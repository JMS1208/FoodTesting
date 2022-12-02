package com.capstone.foodtesting.ui.restaurant.questionnaire.menu

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
class RestaurantQueryRegisterAboutTestingMenuViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    val aboutMenuQueryList: MutableLiveData<List<QueryLine>> = MutableLiveData(mutableListOf())

    fun fetchAboutMenuQueryList(type: Int) = viewModelScope.launch(Dispatchers.IO) {
        //원래는 백으로부터 받아와야함
        val result = repository.getDefaultQuestions(type)

        if (result.isSuccessful) {

            result.body()?.let {
                aboutMenuQueryList.postValue(it)
            }


        }

    }



}