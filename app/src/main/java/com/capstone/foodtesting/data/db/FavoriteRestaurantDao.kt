package com.capstone.foodtesting.data.db

import androidx.room.*
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRestaurantDao {

    @Query("SELECT * FROM restaurant")
    fun getAllFavoriteRestaurant(): Flow<List<Restaurant>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRestaurant(restaurant: Restaurant)

    @Delete
    suspend fun deleteFavoriteRestaurant(restaurant: Restaurant)

//    @Query("SELECT * FROM restaurant WHERE reg_num = :reg_num")
//    fun doExistsFavoriteRestaurant(reg_num: String): Flow<Restaurant?>

    @Query("SELECT * FROM restaurant WHERE reg_num = :reg_num")
    fun doExistsFavoriteRestaurant(reg_num: String): Restaurant?
}