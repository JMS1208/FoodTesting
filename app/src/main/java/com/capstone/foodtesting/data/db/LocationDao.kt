package com.capstone.foodtesting.data.db

import androidx.room.*
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddressInfo(addressInfo: AddressInfo)

    @Delete
    suspend fun deleteAddressInfo(addressInfo: AddressInfo)

    @Query("DELETE FROM address_info")
    suspend fun deleteAllAddressInfo()

    @Query("SELECT * FROM address_info ORDER BY date DESC")
    fun getAllAddressInfo(): Flow<List<AddressInfo>>

    @Query("SELECT * FROM address_info ORDER BY date DESC LIMIT 1")
    fun getLatestAddressInfo(): Flow<AddressInfo?>

}