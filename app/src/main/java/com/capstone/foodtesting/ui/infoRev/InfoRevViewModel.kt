package com.capstone.foodtesting.ui.infoRev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InfoRevViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    fun insertMember(member: Member) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllMember()
        repository.insertMember(member)
    }

    val getMember: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), null)

    fun updateMemeber(nickname:String,gender:Int,birthDate:Date)=viewModelScope.launch(Dispatchers.IO){
        repository.updateMember(nickname,gender,birthDate)
    }
}