package com.capstone.foodtesting.data.model.questionnaire

data class QueryLine(
    val query: String,
    val keywords: List<String>,
    val queryType: QueryType
) {

//    companion object {
//        const val type_restaurant = 1
//        const val type_menu = 2
//        const val type_add = 3
//    }

    enum class QueryType {
        type_restaurant,
        type_menu,
        type_add
    }

}
