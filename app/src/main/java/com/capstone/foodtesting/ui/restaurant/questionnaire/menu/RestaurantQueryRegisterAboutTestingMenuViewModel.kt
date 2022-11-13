package com.capstone.foodtesting.ui.restaurant.questionnaire.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantQueryRegisterAboutTestingMenuViewModel @Inject constructor(

): ViewModel() {

    val aboutMenuQueryList: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    fun fetchAboutMenuQueryList() {
        //원래는 백으로부터 받아와야함
        val tmpList = mutableListOf<String>()

        for (i in 0 until 20) {
            tmpList.add("개발 메뉴 관련 질문 아무거나 $i")
        }

        aboutMenuQueryList.postValue(tmpList)
    }



}