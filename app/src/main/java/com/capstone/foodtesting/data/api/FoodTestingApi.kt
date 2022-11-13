package com.capstone.foodtesting.data.api

import com.capstone.foodtesting.data.model.file.process.ImageHashResponse
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.member.Testing
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.questionnaire.QueryLine
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.model.review.Review
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
    ): Response<Member>

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
        @Path("category") category: String,
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): Response<List<Restaurant>>


    //메뉴 등록하기
    @POST("post/new-menu")
    suspend fun postNewMenu(
        @Body menu: Menu
    ): Response<Menu>

    @Multipart
    @POST("멀티파츠/매장사진")
    suspend fun postRestaurantPhoto(
        @Part imageFile: MultipartBody.Part
    ): Response<ImageHashResponse>

    //매장의 질문지 가져오기
    @GET("get/selected-questions/{reg_num}}")
    suspend fun getRestaurantQuestions(
        @Path("reg_num") reg_num: String
    ): Response<List<QueryLine>>

    //리뷰 등록하기
    @POST("/post/reviews")
    suspend fun postReview(
        @Body reviewList: List<Review>
    ): Response<List<Review>>

}