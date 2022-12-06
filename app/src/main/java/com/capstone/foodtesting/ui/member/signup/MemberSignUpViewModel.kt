package com.capstone.foodtesting.ui.member.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class MemberSignUpViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val mutableGender: MutableLiveData<Int> = MutableLiveData(0)



    // Room - Member
    fun insertMember(member: Member) = viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMember()
            repository.insertMember(member)
        }

    val getMember: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), null)

    fun updateGender(gender: Int) {
        mutableGender.postValue(gender)
    }

    //Rest Api

//    val mutableSignUpSuccess: MutableLiveData<Member> = MutableLiveData()
//
//    fun registerUserInfo(member: Member) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = repository.registerUserInfo(member.apply {
//                loginTime = null
//            })
//
//            if (result.isSuccessful) {
//                result.body()?.let {
//                    mutableSignUpSuccess.postValue(it)
//                }
//
//            }
//        }
//
//    }

    suspend fun registerUserInfo(member: Member): Response<MessageResponse> {
        member.apply {
            loginTime = null
        }

        return repository.registerUserInfo(member)
    }
}