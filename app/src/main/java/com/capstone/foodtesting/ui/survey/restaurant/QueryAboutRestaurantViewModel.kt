package com.capstone.foodtesting.ui.survey.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QueryAboutRestaurantViewModel @Inject constructor(

): ViewModel() {

    val aboutRestaurantQueryList: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    fun fetchAboutRestaurantQueryList() {
        //원래는 백으로부터 받아와야함
        val tmpList = mutableListOf<String>()

        for (i in 0 until 20) {
            tmpList.add("매장 관련 질문 아무거나 $i")
        }

        aboutRestaurantQueryList.postValue(tmpList)
    }
}