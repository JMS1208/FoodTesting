package com.capstone.foodtesting.data.model.menu


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewMenuList(
    @field:SerializedName("menus")
    val menus: List<Menu>?
)