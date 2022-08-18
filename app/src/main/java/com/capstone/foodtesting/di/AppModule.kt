package com.capstone.foodtesting.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.capstone.foodtesting.data.api.KakaoAddressSearchApi
import com.capstone.foodtesting.data.api.KakaoLocalApi
import com.capstone.foodtesting.data.api.UnsplashApi
import com.capstone.foodtesting.data.db.FoodTestingDatabase
import com.capstone.foodtesting.util.Constants.DATABASE_NAME
import com.capstone.foodtesting.util.Constants.DATASTORE_NAME
import com.capstone.foodtesting.util.Constants.KAKAO_BASE_URL
import com.capstone.foodtesting.util.Constants.UNSPLASH_BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME) }
        )
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class kakaoApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class unsplashApi

    @Singleton
    @Provides
    @kakaoApi
    fun provideKakaoApi(
        @kakaoApi retrofit: Retrofit
    ): KakaoLocalApi = retrofit.create(KakaoLocalApi::class.java)

    @Singleton
    @Provides
    @unsplashApi
    fun provideUnsplashApi(
        @unsplashApi retrofit: Retrofit
    ): UnsplashApi = retrofit.create(UnsplashApi::class.java)


    @Singleton
    @Provides
    @kakaoApi
    fun provideKakaoAddressSearchApi(
        @kakaoApi retrofit: Retrofit
    ): KakaoAddressSearchApi = retrofit.create(KakaoAddressSearchApi::class.java)

    @Singleton
    @Provides
    @unsplashApi
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true //클래스에 없는 필드 무시
            explicitNulls = false
        }

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(UNSPLASH_BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

    }

    @Singleton
    @Provides
    @kakaoApi
    fun provideKakaoRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(KAKAO_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    }


    //Room Database
    @Singleton
    @Provides
    fun provideFoodTestingDatabase(@ApplicationContext context: Context): FoodTestingDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FoodTestingDatabase::class.java,
            DATABASE_NAME
        ).build()
    }


}