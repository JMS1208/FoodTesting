package com.capstone.foodtesting.data.repository

import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun saveLogInState(state: String)

    suspend fun getLogInState(): Flow<String>
}