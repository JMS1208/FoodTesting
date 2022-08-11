package com.capstone.foodtesting.ui.login

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.datastore.UserInfo
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
    var name=MutableLiveData("")
    var email=MutableLiveData("")
    var pw=MutableLiveData("")
    var gender=MutableLiveData("")
    var age=MutableLiveData(-1)
    var birthYear=MutableLiveData("")
    var birthDay=MutableLiveData("")
    var photoURL=MutableLiveData("")

    var userInfo=MutableLiveData<UserInfo>()

    //DataStore
    fun saveUserData(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserInfo(
                UserInfo(
                    name = name.value!!,
                    email = email.value!!,
                    pw=pw.value?:"",
                    gender = gender.value?:"",
                    age = age.value?:-1,
                    birthYear = birthYear.value?:"",
                    birthDay = birthDay.value?:"",
                    photoURL = photoURL.value?:""
                )
            )
        }
        // Data saving Test
        Log.d("ViewModel","${name.value!!}")
        Log.d("ViewModel","Save User Data")
    }
    fun retrieveUserData(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserInfo().collect{
                userInfo.postValue(it)
            }
        }
    }
    suspend fun getLogInState() = withContext(Dispatchers.IO) {
        repository.getLogInState().first()
    }

    suspend fun saveLogInState(value: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveLogInState(value)
    }



}