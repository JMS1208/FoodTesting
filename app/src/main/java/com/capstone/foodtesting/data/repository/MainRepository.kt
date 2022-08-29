package com.capstone.foodtesting.data.repository

import com.capstone.foodtesting.data.datastore.UserInfo
import androidx.paging.PagingData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.capstone.foodtesting.data.datastore.LogInStateOptions
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.kakao.local.KakaoLocalResponse
import com.capstone.foodtesting.data.model.kakao.search.address.AddressSearchResponse
import com.capstone.foodtesting.data.model.kakao.search.address.Document
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.data.model.unsplash.UnsplashResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Query
import java.util.*

interface MainRepository {

    suspend fun saveLogInState(state: String)

    suspend fun getLogInState(): Flow<String>


    //Rest API
    suspend fun searchFoods(
        query: String,
        page: Int,
        per_page: Int,
        order_by: String
    ): Response<UnsplashResponse>

    suspend fun searchAddress(
        query: String,
        page: Int,
        size: Int
    ): Response<AddressSearchResponse>

    suspend fun convertCoordToAddress(x: String, y: String): Response<KakaoLocalResponse>


    //Paging
    fun searchFoodsPaging(
        query: String,
        order_by: String
    ): Flow<PagingData<Result>>

    fun searchAddressPaging(
        query: String,
    ): Flow<PagingData<Document>>


    //DataStore
    suspend fun getCurrentAddressInfoUUID(): Flow<String>

    suspend fun saveCurrentAddressInfoUUID(uuid: String)


    //Room - AddressInfo
    fun getAllAddressInfo(): Flow<List<AddressInfo>>

    fun getLatestAddressInfo(): Flow<AddressInfo>

    suspend fun insertAddressInfo(addressInfo: AddressInfo)

    suspend fun deleteAddressInfo(addressInfo: AddressInfo)

    suspend fun deleteAllAddressInfo()

    //Room - Member
    fun getMember(): Flow<Member?>

    suspend fun insertMember(member: Member)

    suspend fun deleteAllMember()

    suspend fun updateMember(nickname:String,gender:Int,birthDate:Date)




}