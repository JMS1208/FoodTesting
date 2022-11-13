package com.capstone.foodtesting.ui.common.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class CommonManageViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {


    val latestAddressInfo: StateFlow<AddressInfo?> = repository.getLatestAddressInfo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    val getMemberInfo: Flow<Member?> = repository.getMember()

    fun deleteMemeberInfo() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllMember()
    }

    suspend fun saveLogInState(value:String)=viewModelScope.launch(Dispatchers.IO){
        repository.saveLogInState(value)
    }

    suspend fun getStoreInfoByCustomerId(uuid : String): Response<List<RestaurantResponse>> {
        return repository.getStoreInfoByCustomerId(uuid)
    }


}