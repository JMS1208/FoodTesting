package com.capstone.foodtesting.data.repository

import android.net.Uri
import androidx.paging.PagingData
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
import com.capstone.foodtesting.data.model.review.myreview.MyReviewResponse
import com.capstone.foodtesting.data.model.statistics.ReviewStatistics
import com.capstone.foodtesting.data.model.statistics.ReviewStatisticsResponse
import com.capstone.foodtesting.data.model.unsplash.Result
import com.capstone.foodtesting.data.model.unsplash.UnsplashResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.*

interface MainRepository {
//    suspend fun saveUserInfo(userInfo: UserInfo)
//    suspend fun getUserInfo():Flow<UserInfo>

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



    //Server API
    suspend fun getUserInfo(
        uuid: String
    ): Response<Testing>

    suspend fun registerUserInfo(
        member: Member
    ): Response<MessageResponse>

    suspend fun loginUser(
        email: String,
        password: String
    ): Response<Member>

    suspend fun getStoreInfoByCustomerId(
        uuid: String
    ): Response<List<RestaurantResponse>>

    suspend fun getStoreInfoByRegNum(
        reg_num: String
    ): Response<List<RestaurantResponse>>

    suspend fun getRestaurantByCategory(
        category: String,
        latitude: Double,
        longitude: Double
    ): Response<List<Restaurant>>

    suspend fun postNewMenu(
        menu: Menu
    ): Response<Menu>

    suspend fun uploadRestaurantImage(
        imageUri: Uri
    ): Response<ImageHashResponse>

    suspend fun getRestaurantQuestions(
        reg_num: String
    ): Response<QueryLineList>

    suspend fun postReview(
        reviewList: List<Review>
    ): Response<MessageResponse>

    suspend fun updateUserInfo(
        member: Member
    ): Response<Member>

    //디폴트 질문 가져오기
    suspend fun getDefaultQuestions(
        type: Int
    ): Response<List<QueryLine>?>

    //매장 질문 등록하기
    suspend fun registerQuestionnaire(
        queryLineList: QueryLineList
    ): Response<MessageResponse>

    //매장 등록하기 (데이터베이스에 정보 저장)
    suspend fun registerRestaurant(
        restaurantInfo: Restaurant
    ): Response<MessageResponse>

    //매장 정보 수정하기
    suspend fun modifyRestaurantInfo(
        restaurantInfo: Restaurant
    ): Response<MessageResponse>

    //메뉴 삭제
    suspend fun deleteMenu(
        reg_num: String,
        menu_uuid: String
    ): Response<MessageResponse>

    //메뉴 수정
    suspend fun modifyMenu(
        menu: Menu
    ): Response<MessageResponse>

    //신규등록 매장목록 가져오기
    suspend fun getHomeNewRestaurantList(
        y: Double,
        x: Double
    ): Response<NewRestaurantList>

    //신규등록 메뉴리스트 가져오기
    suspend fun getNewMenuList(
    ):Response<NewMenuList>


    //통계 정보 가져오기
    suspend fun getReviewStatistics(
        reg_num: String
    ): Response<ReviewStatisticsResponse>

    //작성한 리뷰 가져오기
    suspend fun getMyReviews(
        customer_uuid: String
    ): Response<MyReviewResponse>

    //매장에 달린 리뷰들 가져오기
    suspend fun getReviewsForRestaurant(
        reg_num: String
    ): Response<List<QuesAnswer>>









    //Naver Geo API
    suspend fun searchGeoInfo(
        query: String,
        coordinate: String
    ): Response<NaverGeoResponse>



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

    fun getLatestAddressInfo(): Flow<AddressInfo?>

    suspend fun insertAddressInfo(addressInfo: AddressInfo)

    suspend fun deleteAddressInfo(addressInfo: AddressInfo)

    suspend fun deleteAllAddressInfo()

    //Room - Member
    fun getMember(): Flow<Member?>

    suspend fun insertMember(member: Member)

    suspend fun deleteAllMember()

    suspend fun updateMember(nickname:String,gender:Int,birthDate:Date)

    //Room - FavoriteRestaurant
    fun getAllFavoriteRestaurant(): Flow<List<Restaurant>?>

    suspend fun insertFavoriteRestaurant(restaurant: Restaurant)

    suspend fun deleteFavoriteRestaurant(restaurant: Restaurant)

    fun doExistsFavoriteRestaurant(reg_num: String): Restaurant?
}