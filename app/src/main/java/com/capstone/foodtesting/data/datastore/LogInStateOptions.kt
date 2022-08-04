package com.capstone.foodtesting.data.datastore

enum class LogInStateOptions(val value: String) {
    GOOGLE_SOCIAL_LOGGED_IN("google_social_logged_in"),
    KAKAO_SOCIAL_LOGGED_IN("kakao_social_logged_in"),
    NAVER_SOCIAL_LOGGED_IN("naver_social_logged_in"),
    LOGGED_OUT("logged_out"),
    LOGGED_IN("logged_in")
}