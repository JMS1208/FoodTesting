package com.capstone.foodtesting.util

import androidx.fragment.app.Fragment
import com.capstone.foodtesting.ui.dashboard.category.*

object Constants {

    const val NEED_TO_LOGIN = "need_to_login"
    const val DATASTORE_NAME = "preferences_datastore"
    const val HOME_BANNER_DURATION: Long = 4000 // 홈배너 자동 스크롤 속도
    val categoryList = listOf(
        "한식",
        "중식",
        "일식",
        "양식",
        "패스트푸드",
        "분식",
        "디저트",
        "기타"
        )

    const val UNSPLASH_BASE_URL = "https://api.unsplash.com"
}