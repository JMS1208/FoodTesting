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

    // Room - Member
    fun insertMember(member: Member) = viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMember()
            repository.insertMember(member)
        }

    val getMember: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), null)

}