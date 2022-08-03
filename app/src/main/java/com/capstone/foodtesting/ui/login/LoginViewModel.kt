package com.capstone.foodtesting.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.repository.MainRepository
import com.capstone.foodtesting.data.repository.MainRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {


    //DataStore
    suspend fun getLogInState() = withContext(Dispatchers.IO) {
        repository.getLogInState().first()
    }

    suspend fun saveLogInState(value: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveLogInState(value)
    }



}