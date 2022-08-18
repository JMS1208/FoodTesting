package com.capstone.foodtesting.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.capstone.foodtesting.data.api.KakaoAddressSearchApi
import com.capstone.foodtesting.data.api.KakaoLocalApi
import com.capstone.foodtesting.data.api.UnsplashApi
import com.capstone.foodtesting.data.datastore.LogInStateOptions
import com.capstone.foodtesting.data.db.FoodTestingDatabase
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.kakao.local.KakaoLocalResponse
import com.capstone.foodtesting.data.model.kakao.search.address.AddressSearchResponse
import com.capstone.foodtesting.data.model.kakao.search.address.Document
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.data.model.unsplash.UnsplashResponse
import com.capstone.foodtesting.data.paging.AddressSearchPagingSource
import com.capstone.foodtesting.data.paging.NewRestaurantPagingSource
import com.capstone.foodtesting.data.repository.MainRepositoryImpl.PreferencesKeys.CURRENT_LOCATION
import com.capstone.foodtesting.data.repository.MainRepositoryImpl.PreferencesKeys.LOGIN_STATE
import com.capstone.foodtesting.di.AppModule
import com.capstone.foodtesting.util.Constants.PAGING_SIZE

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @AppModule.unsplashApi private val unsplashApi: UnsplashApi,
    @AppModule.kakaoApi private val kakaoLocalApi: KakaoLocalApi,
    @AppModule.kakaoApi private val kakaoAddressSearchApi: KakaoAddressSearchApi,
    @ApplicationContext private val context : Context,
    private val db: FoodTestingDatabase
): MainRepository{


    //Rest API
    override suspend fun searchFoods(
        query: String,
        page: Int,
        per_page: Int,
        order_by: String
    ): Response<UnsplashResponse> {
        return unsplashApi.getKoreanFoods(
            query = query,
            page = page,
            per_page = per_page,
            order_by = order_by
        )
    }

    override suspend fun searchAddress(
        query: String,
        page: Int,
        size: Int
    ): Response<AddressSearchResponse> {
        return kakaoAddressSearchApi.searchAddress(
            query = query,
            page = page,
            size = size
        )
    }

    override suspend fun convertCoordToAddress(x: String, y: String): Response<KakaoLocalResponse> {
        return kakaoLocalApi.convertCoordToAddress(x,y)
    }


    //Paging
    override fun searchFoodsPaging(query: String, order_by: String): Flow<PagingData<Result>> {
        val pagingSourceFactory = { NewRestaurantPagingSource(unsplashApi, query, order_by) }

        return Pager(
            config = PagingConfig(
                pageSize =  PAGING_SIZE,
                enablePlaceholders = false,
                maxSize =  PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun searchAddressPaging(query: String): Flow<PagingData<Document>> {
        val pagingSourceFactory = { AddressSearchPagingSource(kakaoAddressSearchApi,query) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    //DataStore

    private object PreferencesKeys {
        val LOGIN_STATE  = stringPreferencesKey("login_state")
        val CURRENT_LOCATION = stringPreferencesKey("addressInfo_uuid")

    }

    override suspend fun saveLogInState(state: String) {
        dataStore.edit { prefs->
            prefs[LOGIN_STATE] = state

        }
    }

    override suspend fun getLogInState(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if(exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }

            }
            .map { prefs->
                prefs[LOGIN_STATE] ?: LogInStateOptions.LOGGED_OUT.value

            }
    }

    override suspend fun getCurrentAddressInfoUUID(): Flow<String> {

        return dataStore.data
            .catch { exception ->
                if(exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())

                } else {
                    throw exception
                }
            }
            .map { prefs->

                prefs[CURRENT_LOCATION] ?: ""
            }


    }

    override suspend fun saveCurrentAddressInfoUUID(uuid: String) {
        dataStore.edit { prefs->
            prefs[CURRENT_LOCATION] = uuid
        }
    }


    //Room

    override fun getAllAddressInfo(): Flow<List<AddressInfo>> {
        return db.locationDao().getAllAddressInfo()
    }

    override fun getLatestAddressInfo(): Flow<AddressInfo> {
        return db.locationDao().getLatestAddressInfo()
    }

    override suspend fun insertAddressInfo(addressInfo: AddressInfo) {
        db.locationDao().insertAddressInfo(addressInfo)
    }

    override suspend fun deleteAddressInfo(addressInfo: AddressInfo) {
        db.locationDao().deleteAddressInfo(addressInfo)
    }

    override suspend fun deleteAllAddressInfo() {
        db.locationDao().deleteAllAddressInfo()
    }


}