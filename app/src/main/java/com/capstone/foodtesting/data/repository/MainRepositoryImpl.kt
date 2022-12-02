package com.capstone.foodtesting.data.repository

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toFile
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
import com.capstone.foodtesting.data.model.menu.NewMenuList
import com.capstone.foodtesting.data.model.naver.geo.NaverGeoResponse
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.questionnaire.QueryLineList
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.model.restaurant.home.NewRestaurantList
import com.capstone.foodtesting.data.model.restaurant.register.MessageResponse
import com.capstone.foodtesting.data.model.review.QuesAnswer
import com.capstone.foodtesting.data.model.review.Review
import com.capstone.foodtesting.data.model.review.ReviewList
import com.capstone.foodtesting.data.model.review.myreview.MyReviewResponse
import com.capstone.foodtesting.data.model.statistics.ReviewStatisticsResponse
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.data.model.unsplash.UnsplashResponse
import com.capstone.foodtesting.data.paging.AddressSearchPagingSource
import com.capstone.foodtesting.data.paging.NewRestaurantPagingSource
import com.capstone.foodtesting.data.repository.MainRepositoryImpl.PreferencesKeys.CURRENT_LOCATION
import com.capstone.foodtesting.data.repository.MainRepositoryImpl.PreferencesKeys.LOGIN_STATE
import com.capstone.foodtesting.di.AppModule
import com.capstone.foodtesting.util.Constants.PAGING_SIZE
import dagger.hilt.android.qualifiers.ApplicationContext
import gun0912.tedimagepicker.util.ToastUtil.context

import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
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
    @ApplicationContext context: Context,
    private val db: FoodTestingDatabase,
    private val contentResolver: ContentResolver
) : MainRepository {

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
        return kakaoLocalApi.convertCoordToAddress(x, y)
    }

    override suspend fun getUserInfo(uuid: String): Response<Testing> {
        return foodTestingApi.getUserInfo(uuid)
    }

    override suspend fun registerUserInfo(
        member: Member
    ): Response<MessageResponse> {
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

    override suspend fun getRestaurantByCategory(
        category: String,
        latitude: Double,
        longitude: Double
    ): Response<List<Restaurant>> {
        return foodTestingApi.getRestaruantByCategory(category, latitude, longitude)
    }

    override suspend fun postNewMenu(menu: Menu): Response<Menu> {
        return foodTestingApi.postNewMenu(menu)
    }

    override suspend fun uploadRestaurantImage(imageUri: Uri): Response<ImageHashResponse> {


        val bodyPart = try {
            val imageFile = getFile(context, imageUri)
            MultipartBody.Part.createFormData("files", imageFile.name, imageFile.asRequestBody())

        } catch (E: Exception) {
            //val imageFile = File(imageUri.path ?: imageUri.encodedPath ?: imageUri.toFile())
            val imageFile = imageUri.toFile()

            MultipartBody.Part.createFormData("files", imageFile.name, imageFile.asRequestBody())
        }
        //원래는 name 을 "file"로 지정했었음
        return foodTestingApi.postRestaurantPhoto(bodyPart)

    }

    override suspend fun getRestaurantQuestions(
        reg_num: String
    ): Response<QueryLineList> {
        return foodTestingApi.getRestaurantQuestions(reg_num)
    }

    override suspend fun postReview(reviews: List<Review>): Response<MessageResponse> {
        val reviewList = ReviewList(reviews)
        return foodTestingApi.postReview(reviewList)
    }

    override suspend fun updateUserInfo(member: Member): Response<Member> {
        return foodTestingApi.updateUserInfo(member)
    }

    override suspend fun getDefaultQuestions(type: Int): Response<List<QueryLine>?> {
        return foodTestingApi.getDefaultQuestions(type)
    }

    //매장 질문 등록하기
    override suspend fun registerQuestionnaire(
        queryLineList: QueryLineList
    ): Response<MessageResponse> {
        return foodTestingApi.registerQuestionnaire(queryLineList)
    }

    //매장 등록하기 (데이터베이스에 정보 저장)
    override suspend fun registerRestaurant(
        restaurantInfo: Restaurant
    ): Response<MessageResponse> {
        return foodTestingApi.registerRestaurant(restaurantInfo)
    }

    //매장 정보 수정하기
    override suspend fun modifyRestaurantInfo(restaurantInfo: Restaurant): Response<MessageResponse> {
        return foodTestingApi.modifyRestaurantInfo(restaurantInfo)
    }

    //메뉴 삭제
    override suspend fun deleteMenu(reg_num: String, menu_uuid: String): Response<MessageResponse> {
        return foodTestingApi.deleteMenu(reg_num, menu_uuid)
    }

    //메뉴 수정
    override suspend fun modifyMenu(menu: Menu): Response<MessageResponse> {
        return foodTestingApi.modifyMenu(menu)
    }

    //신규등록 매장 가져오기
    override suspend fun getHomeNewRestaurantList(
        y: Double,
        x: Double
    ): Response<NewRestaurantList> {
        return foodTestingApi.getNewRestaurantList(y, x)
    }

    //신규등록 메뉴 가져오기
    override suspend fun getNewMenuList(): Response<NewMenuList> {
        return foodTestingApi.getNewMenuList()
    }

    //통계 정보 가져오기
    override suspend fun getReviewStatistics(reg_num: String): Response<ReviewStatisticsResponse> {
        return foodTestingApi.getReviewStatistics(reg_num)
    }

    //내가 작성한 리뷰 가져오기
    override suspend fun getMyReviews(customer_uuid: String): Response<MyReviewResponse> {
        return foodTestingApi.getMyReviews(customer_uuid)
    }

    //매장에 달린 리뷰 가져오기
    override suspend fun getReviewsForRestaurant(reg_num: String): Response<List<QuesAnswer>> {
        return foodTestingApi.getReviewsForRestaurant(reg_num)
    }


    //Naver Geo API
    override suspend fun searchGeoInfo(
        query: String,
        coordinate: String
    ): Response<NaverGeoResponse> {
        return naverGeoApi.searchGeoInfo(query, coordinate)
    }


    //Paging
    override fun searchFoodsPaging(query: String, order_by: String): Flow<PagingData<Result>> {
        val pagingSourceFactory = { NewRestaurantPagingSource(unsplashApi, query, order_by) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun searchAddressPaging(query: String): Flow<PagingData<Document>> {
        val pagingSourceFactory = { AddressSearchPagingSource(kakaoAddressSearchApi, query) }

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
        val LOGIN_STATE = stringPreferencesKey("login_state")
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
        dataStore.edit { prefs ->
            prefs[LOGIN_STATE] = state

        }
    }

    override suspend fun getLogInState(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }

            }
            .map { prefs ->
                prefs[LOGIN_STATE] ?: LogInStateOptions.LOGGED_OUT.value

            }
    }

    override suspend fun getCurrentAddressInfoUUID(): Flow<String> {

        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())

                } else {
                    throw exception
                }
            }
            .map { prefs ->

                prefs[CURRENT_LOCATION] ?: ""
            }


    }

    override suspend fun saveCurrentAddressInfoUUID(uuid: String) {
        dataStore.edit { prefs ->
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
        db.memberDao().updateMember(nickname, gender, birthDate)
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


    //PhotoFile Processing

    private fun getFile(context: Context, uri: Uri): File {
        val destinationFilename =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                createFileFromStream(
                    ins!!,
                    destinationFilename
                )
            }
        } catch (ex: Exception) {

            ex.printStackTrace()
        }
        return destinationFilename
    }

    private fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {

            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }
}