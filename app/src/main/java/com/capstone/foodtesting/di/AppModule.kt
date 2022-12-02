package com.capstone.foodtesting.di

import android.content.ContentResolver
import android.content.Context
import android.os.Build.VERSION_CODES.M
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.capstone.foodtesting.data.api.*
import com.capstone.foodtesting.data.db.FoodTestingDatabase
import com.capstone.foodtesting.util.Constants.DATABASE_NAME
import com.capstone.foodtesting.util.Constants.DATASTORE_NAME
import com.capstone.foodtesting.util.Constants.FOOD_TESTING_BASE_URL
import com.capstone.foodtesting.util.Constants.KAKAO_BASE_URL
import com.capstone.foodtesting.util.Constants.NAVER_BASE_URL
import com.capstone.foodtesting.util.Constants.UNSPLASH_BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
import retrofit2.converter.gson.GsonConverterFactory
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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class foodTestingApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class naverApi

    @Singleton
    @Provides
    @naverApi
    fun provideNaverApi(
        @naverApi retrofit: Retrofit
    ): NaverGeoApi = retrofit.create(NaverGeoApi::class.java)

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
    @foodTestingApi
    fun provideFoodTestingApi(
        @foodTestingApi retrofit: Retrofit
    ): FoodTestingApi = retrofit.create(FoodTestingApi::class.java)


    @Singleton
    @Provides
    @naverApi
    fun provideNaverRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NAVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

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

    @Singleton
    @Provides
    @foodTestingApi
    fun provideFoodTestingRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(FOOD_TESTING_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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
        ).fallbackToDestructiveMigration().build()
    }


    //Content Resolver
    @Singleton
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

}
