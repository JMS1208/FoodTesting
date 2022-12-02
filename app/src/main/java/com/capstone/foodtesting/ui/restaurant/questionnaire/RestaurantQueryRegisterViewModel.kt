package com.capstone.foodtesting.ui.restaurant.questionnaire

import android.system.Os.remove
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.questionnaire.QueryLineList
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.repository.MainRepository
import com.capstone.foodtesting.util.Constants.IS_ALREADY_EXISTED
import com.capstone.foodtesting.util.Constants.IS_SUCCESS
import com.capstone.foodtesting.util.Constants.IS_TOO_MUCH_COUNT
import com.capstone.foodtesting.util.Constants.MAX_QUERY_COUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RestaurantQueryRegisterViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _currentQuestionnaire: MutableLiveData<MutableList<QueryLine>> = MutableLiveData()
//    val currentQuestionnaire = MutableLiveData<MutableList<QueryLine>>(mutableListOf())
    val currentQuestionnaire: LiveData<MutableList<QueryLine>> get() = _currentQuestionnaire

    fun loadCurrentQuestionnaire(reg_num: String) = viewModelScope.launch(Dispatchers.IO) {

        try {
            val response = repository.getRestaurantQuestions(reg_num)

            if (response.isSuccessful) {
                response.body()?.let {
                    _currentQuestionnaire.postValue(it.queryLineList.toMutableList())
                }
            }
        } catch (E: Exception) {
            E.printStackTrace()
        }

    }


    fun insertQuery(queryLine: QueryLine): Int {

        currentQuestionnaire.value?.map {
            if(it.uuid == queryLine.uuid) {
                return IS_ALREADY_EXISTED
            }
        }

        return if(currentQuestionnaire.value?.contains(queryLine) == true) {
            IS_ALREADY_EXISTED
        } else if (currentQuestionnaire.value?.size == MAX_QUERY_COUNT) {
            IS_TOO_MUCH_COUNT
        } else {
            val newQuestionnaire = currentQuestionnaire.value?.toMutableList()
            newQuestionnaire?.let {
                it.add(queryLine)
                _currentQuestionnaire.postValue(it)
            }
            IS_SUCCESS
        }
    }

    fun removeQuery(position: Int) {
        currentQuestionnaire.value?.let {
            val newQuestionnaire = it.toMutableList()
            newQuestionnaire.removeAt(position)
            _currentQuestionnaire.postValue(newQuestionnaire)
        }
    }

    fun removeQuery(queryLine: QueryLine) {
        if(currentQuestionnaire.value?.contains(queryLine) == true) {
            val newQuestionnaire = currentQuestionnaire.value?.toMutableList()
            newQuestionnaire?.let {
                it.remove(queryLine)
                _currentQuestionnaire.postValue(it)
            }
        }
    }

    fun swapQuery(fromPosition: Int, toPosition: Int) {
        currentQuestionnaire.value?.let {
            val newQuestionnaire = it.toMutableList()
            Collections.swap(newQuestionnaire,fromPosition,toPosition)
            _currentQuestionnaire.postValue(newQuestionnaire)
        }
    }

    suspend fun registerQuestionnaire(
        queryLineList: List<QueryLine>
    ): Response<MessageResponse> {

        /*
        이때 오는 메시지 종류
        "MESSAGE": "already register overall questions"
        "MESSAGE": "Failed to register custom question"
        "MESSAGE": "Failed to register selected question"
        "MESSAGE": "Success to register all selected questions"
         */

        val questionnaire = QueryLineList(queryLineList)
        return repository.registerQuestionnaire(questionnaire)
    }



}