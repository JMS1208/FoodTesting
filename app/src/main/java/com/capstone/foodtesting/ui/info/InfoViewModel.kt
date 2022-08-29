package com.capstone.foodtesting.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {


    val latestAddressInfo: StateFlow<AddressInfo?> = repository.getLatestAddressInfo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    val getMemberInfo: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    fun deleteMemeberInfo()=viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllMember()
    }

    suspend fun saveLogInState(value:String)=viewModelScope.launch(Dispatchers.IO){
        repository.saveLogInState(value)
    }


}