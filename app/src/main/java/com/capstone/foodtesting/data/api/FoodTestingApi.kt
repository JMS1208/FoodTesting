package com.capstone.foodtesting.data.api

import com.capstone.foodtesting.data.model.file.process.ImageHashResponse
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.member.Testing
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.menu.NewMenuList
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
import com.capstone.foodtesting.data.model.review.reviews.ReviewsForRestaurantResponse
import com.capstone.foodtesting.data.model.statistics.ReviewStatistics
import com.capstone.foodtesting.data.model.statistics.ReviewStatisticsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface FoodTestingApi {

//    @GET("get/userinfo/")
//    suspend fun getUserInfo(
//        @Query("uuid") uuid: String
//    ): Response<Testing>

    @GET("get/userinfo/{uuid}")
    suspend fun getUserInfo(
        @Path("uuid") uuid: String
    ): Response<Testing>

    //로그인
    @GET("login/email={email}&pw={pw}")
    suspend fun loginUser(
        @Path("email") email: String,
        @Path("pw") password: String
    ): Response<Member>

    @POST("register/userinfo")
    suspend fun registerUserInfo(
        @Body member: Member
    ): Response<MessageResponse>

    //회원정보수정
    @POST("modify/userinfo")
    suspend fun updateUserInfo(
        @Body member: Member
    ): Response<Member>

    //매장 정보 불러오기
    @GET("storeinfo/by-customer-id/{uuid}")
    suspend fun getStoreInfoByCustomerId(
        @Path("uuid") uuid: String
    ): Response<List<RestaurantResponse>>

    @GET("storeinfo/by-registartion-num/{regnum}")
    suspend fun getStoreInfoByRegNum(
        @Path("regnum") reg_num: String
    ): Response<List<RestaurantResponse>>

    //카테고리별 매장 불러오기
    @GET("marketinfo/orderby/distance/{category}/{latitude}&{longitude}")
    suspend fun getRestaruantByCategory(
        @Path("category", encoded = true) category: String,
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): Response<List<Restaurant>>


    //메뉴 등록하기
    @POST("post/new-menu")
    suspend fun postNewMenu(
        @Body menu: Menu
    ): Response<Menu>

    //사진 해시값으로 변경하기
    @Multipart
    @POST("post/img")
    suspend fun postRestaurantPhoto(
        @Part imageFile: MultipartBody.Part
    ): Response<ImageHashResponse>

    //매장의 질문지 가져오기
    @GET("get/selected-questions/{reg_num}")
    suspend fun getRestaurantQuestions(
        @Path("reg_num") reg_num: String
    ): Response<QueryLineList>

    //리뷰 등록하기
    @POST("post/reviews")
    suspend fun postReview(
        @Body reviewList: ReviewList
    ): Response<MessageResponse>

    //디폴트 질문 가져오기
    @GET("get/default/question/{type}")
    suspend fun getDefaultQuestions(
        @Path("type") type: Int
    ): Response<List<QueryLine>?>

    //매장 질문 등록하기
    @POST("post/overall-selected-questions")
    suspend fun registerQuestionnaire(
        @Body queryLineList: QueryLineList
    ): Response<MessageResponse>


    //매장 정보 등록하기 (데이터베이스에 저장하는 과정)
    @POST("register/store")
    suspend fun registerRestaurant(
        @Body restaurantInfo: Restaurant
    ): Response<MessageResponse>

    //통계 정보 가져오기
    @GET("get/review-research/{reg_num}")
    suspend fun getReviewStatistics(
        @Path("reg_num") reg_num: String
    ): Response<ReviewStatisticsResponse>


    //메뉴 삭제
    @DELETE("delete/menu/{reg_num}&{menu_uuid}")
    suspend fun deleteMenu(
        @Path("reg_num") reg_num: String,
        @Path("menu_uuid") menu_uuid: String
    ): Response<MessageResponse>

    //메뉴 수정
    @POST("modify/menu")
    suspend fun modifyMenu(
        @Body menu: Menu
    ): Response<MessageResponse>

    //매장 정보 수정
    @POST("modify/storeinfo")
    suspend fun modifyRestaurantInfo(
        @Body restaurantInfo: Restaurant
    ): Response<MessageResponse>
    /* 오는 메시지
        Success to modify
        Failed to Update - 키 밸류 값 문제
     */

    //신규 등록 매장 불러오기
    @GET("get/main/new-market/{lat}&{lng}")
    suspend fun getNewRestaurantList(
        @Path("lat") y: Double,
        @Path("lng") x: Double
    ): Response<NewRestaurantList>

    //신규 등록 메뉴 가져오기
    @GET("get/main/new-menu")
    suspend fun getNewMenuList(
    ):Response<NewMenuList>

    //내가 쓴 리뷰 목록 가져오기
    @GET("get/review/each/customer/{uuid}")
    suspend fun getMyReviews(
        @Path("uuid") customer_uuid: String
    ): Response<MyReviewResponse>

    //매장에 달린 리뷰들 가져오기
    @GET("get/reviews/written-by-customer/{reg_num}")
    suspend fun getReviewsForRestaurant(
        @Path("reg_num") reg_num: String
    ): Response<ReviewsForRestaurantResponse>


    //리뷰요약 가져오기
    @GET("get/reviewsummary/{reg_num}")
    suspend fun getReviewSummary(
        @Path("reg_num") reg_num: String
    ): Response<MessageResponse>
}