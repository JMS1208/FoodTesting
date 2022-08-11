package com.capstone.foodtesting.data.repository

import com.capstone.foodtesting.data.datastore.UserInfo
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun saveUserInfo(userInfo: UserInfo)
    suspend fun getUserInfo():Flow<UserInfo>

    suspend fun saveLogInState(state: String)

    suspend fun getLogInState(): Flow<String>
}