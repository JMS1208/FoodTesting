package com.capstone.foodtesting.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.foodtesting.data.model.member.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member)

    @Query("DELETE FROM member")
    suspend fun deleteAllMember()

    @Query("SELECT * FROM member ORDER BY loginTime DESC LIMIT 1")
    fun getMember(): Flow<Member?>

}