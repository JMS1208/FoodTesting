package com.capstone.foodtesting.ui.restaurant.questionnaire.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantQueryRegisterAddPersonallyViewModel @Inject constructor(

): ViewModel() {

    val currentKeywords = MutableLiveData<MutableList<String>>(mutableListOf())

    fun insertKeyword(keyword: String): Boolean {
        return if(currentKeywords.value?.contains(keyword) == true) {
            false
        } else {
            val newKeywords = currentKeywords.value?.toMutableList()
            newKeywords?.let {
                it.add(keyword)
                currentKeywords.postValue(it)
            }

            true
        }
    }

    fun replaceKeywords(keywords: MutableList<String>) {
        clearKeywords()

        currentKeywords.postValue(keywords)
    }

    fun removeKeyword(keyword: String) {
        if(currentKeywords.value?.contains(keyword) == true) {
            val newKeywords = currentKeywords.value?.toMutableList()
            newKeywords?.let{
                it.remove(keyword)
                currentKeywords.postValue(it)
            }

        }
    }

    fun clearKeywords() {
        val newKeywords = mutableListOf<String>()
//        currentKeywords.value = mutableListOf()
        currentKeywords.postValue(newKeywords)
    }
}