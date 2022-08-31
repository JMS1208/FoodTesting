package com.capstone.foodtesting.ui.login

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.datastore.UserInfo
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.repository.MainRepository
import com.capstone.foodtesting.data.repository.MainRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    // Room - Member
    fun insertMember(member:Member)=viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllMember()
        repository.insertMember(member)
    }

    suspend fun getLogInState() = withContext(Dispatchers.IO) {
        repository.getLogInState().first()
    }

    fun saveLogInState(value: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveLogInState(value)
    }



}