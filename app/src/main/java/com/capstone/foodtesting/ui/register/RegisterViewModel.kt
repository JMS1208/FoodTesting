package com.capstone.foodtesting.ui.register

import android.icu.util.Calendar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.datastore.UserInfo
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    var name = MutableLiveData("")
    var email = MutableLiveData("")
    var pw = MutableLiveData("")
    var gender = MutableLiveData("")
    var birthYear = MutableLiveData("")
    var birthDay = MutableLiveData("")


    //DataStore
    fun saveUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserInfo(
                UserInfo(
                    name = name.value!!,
                    email = email.value!!,
                    pw = pw.value ?: "",
                    gender = gender.value ?: "",
                    age = Calendar.YEAR - (birthYear.value?.toInt() ?: Calendar.YEAR),
                    birthYear = birthYear.value ?: "",
                    birthDay = birthDay.value ?: "",
                    photoURL = ""
                )
            )
        }
        // Data saving Test
        // Log.d("ViewModel","${name.value!!}")
        // Log.d("ViewModel","Save User Data")
    }

    // Room - Member
    fun insertMember(member: Member) = viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMember()
            repository.insertMember(member)
        }

    val getMember: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), null)

}