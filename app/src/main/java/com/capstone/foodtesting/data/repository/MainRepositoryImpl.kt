package com.capstone.foodtesting.data.repository

import android.content.ContentResolver
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.capstone.foodtesting.data.api.*
import com.capstone.foodtesting.data.datastore.LogInStateOptions
import com.capstone.foodtesting.data.db.FoodTestingDatabase
import com.capstone.foodtesting.data.model.file.process.ImageHashResponse
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.kakao.local.KakaoLocalResponse
import com.capstone.foodtesting.data.model.kakao.search.address.AddressSearchResponse
import com.capstone.foodtesting.data.model.kakao.search.address.Document
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.member.Testing
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.naver.geo.NaverGeoResponse
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.data.model.unsplash.UnsplashResponse
import com.capstone.foodtesting.data.paging.AddressSearchPagingSource
import com.capstone.foodtesting.data.paging.NewRestaurantPagingSource
import com.capstone.foodtesting.data.repository.MainRepositoryImpl.PreferencesKeys.CURRENT_LOCATION
import com.capstone.foodtesting.data.repository.MainRepositoryImpl.PreferencesKeys.LOGIN_STATE
import com.capstone.foodtesting.di.AppModule
import com.capstone.foodtesting.util.Constants.PAGING_SIZE

import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

//const val DATASTORE_NAME="USER_INFO"
//val Context.datastore:DataStore<Preferences> by preferencesDataStore(name= DATASTORE_NAME)
@Singleton
class MainRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @AppModule.unsplashApi private val unsplashApi: UnsplashApi,
    @AppModule.kakaoApi private val kakaoLocalApi: KakaoLocalApi,
    @AppModule.kakaoApi private val kakaoAddressSearchApi: KakaoAddressSearchApi,
    @AppModule.foodTestingApi private val foodTestingApi: FoodTestingApi,
    @AppModule.naverApi private val naverGeoApi: NaverGeoApi,
    private val db: FoodTestingDatabase,
    private val contentResolver: ContentResolver
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

    override suspend fun getUserInfo(uuid: String): Response<Testing> {
        return foodTestingApi.getUserInfo(uuid)
    }

    override suspend fun registerUserInfo(
        member: Member
    ): Response<Member> {
        return foodTestingApi.registerUserInfo(member)
    }

    override suspend fun loginUser(email: String, password: String): Response<Member> {
        return foodTestingApi.loginUser(email, password)
    }

    override suspend fun getStoreInfoByCustomerId(uuid: String): Response<List<RestaurantResponse>> {
       return foodTestingApi.getStoreInfoByCustomerId(uuid)
    }

    override suspend fun getStoreInfoByRegNum(reg_num: String): Response<List<RestaurantResponse>> {
        return foodTestingApi.getStoreInfoByRegNum(reg_num)
    }

    override suspend fun getRestaruantByCategory(
        category: String,
        latitude: Double,
        longitude: Double
    ): Response<List<Restaurant>> {
        return foodTestingApi.getRestaruantByCategory(category, latitude, longitude)
    }

    override suspend fun postNewMenu(menu: Menu): Response<Menu> {
        return foodTestingApi.postNewMenu(menu)
    }

    override suspend fun uploadRestaurantImage(imageFile: File): Response<ImageHashResponse> {
        return foodTestingApi.postRestaurantPhoto(
            MultipartBody.Part.createFormData("image", imageFile.name, imageFile.asRequestBody())
        )
    }

    override suspend fun getRestaurantQuestions(
        reg_num: String
    ): Response<List<QueryLine>> {
        return foodTestingApi.getRestaurantQuestions(reg_num)
    }

    override suspend fun postReview(reviewList: List<Review>): Response<List<Review>> {
        return foodTestingApi.postReview(reviewList)
    }

    override suspend fun updateUserInfo(member: Member): Response<Member> {
        return foodTestingApi.updateUserInfo(member)
    }

    //Naver Geo API
    override suspend fun searchGeoInfo(query: String, coordinate: String): Response<NaverGeoResponse> {
        return naverGeoApi.searchGeoInfo(query, coordinate)
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

//    override suspend fun saveUserInfo(userInfo: UserInfo) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getUserInfo(): Flow<UserInfo> {
//        TODO("Not yet implemented")
//    }

    //Room - AddressInfo


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


    //Room - AddressInfo

    override fun getAllAddressInfo(): Flow<List<AddressInfo>> {
        return db.locationDao().getAllAddressInfo()
    }

    override fun getLatestAddressInfo(): Flow<AddressInfo?> {
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

    //Room - Member
    override fun getMember(): Flow<Member?> {
        return db.memberDao().getMember()
    }

    override suspend fun insertMember(member: Member) {
        db.memberDao().insertMember(member)
    }

    override suspend fun deleteAllMember() {
        db.memberDao().deleteAllMember()
    }

    override suspend fun updateMember(nickname: String, gender: Int, birthDate: Date) {
        db.memberDao().updateMember(nickname,gender,birthDate)
    }

    //Room - FavoriteRestaurant
    override fun getAllFavoriteRestaurant(): Flow<List<Restaurant>?> {
        return db.favoriteRestaurantDao().getAllFavoriteRestaurant()
    }

    override suspend fun insertFavoriteRestaurant(restaurant: Restaurant) {
        db.favoriteRestaurantDao().insertFavoriteRestaurant(restaurant)
    }

    override suspend fun deleteFavoriteRestaurant(restaurant: Restaurant) {
        db.favoriteRestaurantDao().deleteFavoriteRestaurant(restaurant)
    }

    override fun doExistsFavoriteRestaurant(reg_num: String): Restaurant? {
        return db.favoriteRestaurantDao().doExistsFavoriteRestaurant(reg_num)
    }


}