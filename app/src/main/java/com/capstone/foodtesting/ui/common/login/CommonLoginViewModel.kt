package com.capstone.foodtesting.ui.common.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class CommonLoginViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    // Room - Member
    fun insertMember(member:Member)=viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllMember()
        repository.insertMember(member)
    }

    val getMember: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), null)

    suspend fun getLogInState() = withContext(Dispatchers.IO) {
        repository.getLogInState().first()
    }

    fun saveLogInState(value: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveLogInState(value)
    }

    //로그인
    suspend fun loginUser(email: String, password: String) = repository.loginUser(email, password)



}