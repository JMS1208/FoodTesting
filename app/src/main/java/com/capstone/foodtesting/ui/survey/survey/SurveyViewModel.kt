package com.capstone.foodtesting.ui.survey.survey

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.util.Constants.IS_ALREADY_EXISTED
import com.capstone.foodtesting.util.Constants.IS_SUCCESS
import com.capstone.foodtesting.util.Constants.IS_TOO_MUCH_COUNT
import com.capstone.foodtesting.util.Constants.MAX_QUERY_COUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(

): ViewModel() {

    val currentQuestionnaire = MutableLiveData<MutableList<QueryLine>>(mutableListOf())

    fun insertQuery(queryLine: QueryLine): Int {
        return if(currentQuestionnaire.value?.contains(queryLine) == true) {
            IS_ALREADY_EXISTED
        } else if (currentQuestionnaire.value?.size == MAX_QUERY_COUNT) {
            IS_TOO_MUCH_COUNT
        } else {
            val newQuestionnaire = currentQuestionnaire.value?.toMutableList()
            newQuestionnaire?.let {
                it.add(queryLine)
                currentQuestionnaire.postValue(it)
            }
            IS_SUCCESS
        }
    }

    fun removeQuery(position: Int) {
        currentQuestionnaire.value?.let {
            val newQuestionnaire = it.toMutableList()
            newQuestionnaire.removeAt(position)
            currentQuestionnaire.postValue(newQuestionnaire)
        }
    }

    fun removeQuery(queryLine: QueryLine) {
        if(currentQuestionnaire.value?.contains(queryLine) == true) {
            val newQuestionnaire = currentQuestionnaire.value?.toMutableList()
            newQuestionnaire?.let {
                it.remove(queryLine)
                currentQuestionnaire.postValue(it)
            }
        }
    }

    fun swapQuery(fromPosition: Int, toPosition: Int) {
        currentQuestionnaire.value?.let {
            val newQuestionnaire = it.toMutableList()
            Collections.swap(newQuestionnaire,fromPosition,toPosition)
            currentQuestionnaire.postValue(newQuestionnaire)
        }
    }




}