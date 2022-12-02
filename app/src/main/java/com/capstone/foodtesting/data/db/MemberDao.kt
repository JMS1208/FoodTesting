package com.capstone.foodtesting.data.db

import androidx.room.*
import com.capstone.foodtesting.data.model.member.Member
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member)

    @Query("DELETE FROM member")
    suspend fun deleteAllMember()

    @Query("SELECT * FROM member ORDER BY loginTime DESC LIMIT 1")
    fun getMember(): Flow<Member?>

    @Query("UPDATE member SET nickName= :nickname, gender= :gender, birthDate= :birthDate")
    suspend fun updateMember(nickname:String,gender:Int,birthDate:Date)
}