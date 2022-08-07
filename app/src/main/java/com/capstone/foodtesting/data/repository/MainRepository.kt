package com.capstone.foodtesting.data.repository

import androidx.paging.PagingData
import com.capstone.foodtesting.data.model.Result
import com.capstone.foodtesting.data.model.UnsplashResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MainRepository {

    suspend fun saveLogInState(state: String)

    suspend fun getLogInState(): Flow<String>

    suspend fun searchFoods(
        query: String,
        page: Int,
        per_page: Int,
        order_by: String
    ): Response<UnsplashResponse>

    fun searchFoodsPaging(
        query: String,
        order_by: String
    ): Flow<PagingData<Result>>
}