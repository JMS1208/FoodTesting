package com.capstone.foodtesting.util

import androidx.fragment.app.Fragment
import com.capstone.foodtesting.ui.dashboard.category.*

object Constants {

    const val PERMISSION_REQUEST_CODE = 2001

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
    const val KAKAO_BASE_URL = "https://dapi.kakao.com"

    //데이터베이스 이름
    const val DATABASE_NAME = "foodtesting_db"


    //Paging
    const val  STARTING_PAGE_INDEX = 1
    const val PAGING_SIZE = 10

    //주소검색 딜레이 타임
    const val SEARCH_ADDRESS_DELAY_TIME: Long = 100L

    // google Login
    const val RC_SIGN_IN=1001
}