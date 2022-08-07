package com.capstone.foodtesting.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.capstone.foodtesting.data.api.UnsplashApi
import com.capstone.foodtesting.data.datastore.LogInStateOptions
import com.capstone.foodtesting.data.model.Result
import com.capstone.foodtesting.data.model.UnsplashResponse
import com.capstone.foodtesting.data.paging.NewRestaurantPagingSource
import com.capstone.foodtesting.data.paging.NewRestaurantPagingSource.Companion.PAGING_SIZE
import com.capstone.foodtesting.data.repository.MainRepositoryImpl.PreferencesKeys.LOGIN_STATE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val api: UnsplashApi
): MainRepository{



    private object PreferencesKeys {
        val LOGIN_STATE  = stringPreferencesKey("login_state")

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

    override suspend fun searchFoods(
        query: String,
        page: Int,
        per_page: Int,
        order_by: String
    ): Response<UnsplashResponse> {
        return api.getKoreanFoods(
            query = query,
            page = page,
            per_page = per_page,
            order_by = order_by
        )
    }

    override fun searchFoodsPaging(query: String, order_by: String): Flow<PagingData<Result>> {
        val pagingSourceFactory = { NewRestaurantPagingSource(api, query, order_by) }

        return Pager(
            config = PagingConfig(
                pageSize =  PAGING_SIZE,
                enablePlaceholders = false,
                maxSize =  PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


}