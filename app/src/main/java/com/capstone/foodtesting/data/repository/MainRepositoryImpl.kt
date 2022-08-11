package com.capstone.foodtesting.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.foodtesting.data.datastore.LogInStateOptions
import com.capstone.foodtesting.data.datastore.UserInfo
import com.capstone.foodtesting.data.repository.MainRepositoryImpl.PreferencesKeys.LOGIN_STATE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val DATASTORE_NAME="USER_INFO"
val Context.datastore:DataStore<Preferences> by preferencesDataStore(name= DATASTORE_NAME)
@Singleton
class MainRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): MainRepository{

    companion object{
        val NAME= stringPreferencesKey("NAME")
        val EMAIL= stringPreferencesKey("EMAIL")
        val PW= stringPreferencesKey("PW")
        val GENDER= stringPreferencesKey("GENDER")
        val AGE= intPreferencesKey("AGE")
        val BIRTH_YEAR= stringPreferencesKey("BIRTH_YEAR")
        val BIRTH_DAY= stringPreferencesKey("BIRTH_DAY")
        val PHOTO_URL= stringPreferencesKey("PHOTO_URL")
    }

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        dataStore.edit { info->
            info[NAME]=userInfo.name
            info[EMAIL]=userInfo.email
            info[PW]=userInfo.pw?:""
            info[GENDER]=userInfo.gender?:""
            info[AGE]=userInfo.age?:-1
            info[BIRTH_YEAR]=userInfo.birthYear?:""
            info[BIRTH_DAY]=userInfo.birthDay?:""
            info[PHOTO_URL]=userInfo.photoURL?:""
        }
    }

    override suspend fun getUserInfo():Flow<UserInfo> {
        return dataStore.data.map{info->
            UserInfo(
                name = info[NAME]!!,
                email = info[EMAIL]!!,
                pw=info[PW]?:"",
                gender = info[GENDER]?:"",
                age = info[AGE]?:-1,
                birthYear = info[BIRTH_YEAR]?:"",
                birthDay = info[BIRTH_DAY]?:"",
                photoURL = info[PHOTO_URL]?:""
            )
        }
    }
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


}