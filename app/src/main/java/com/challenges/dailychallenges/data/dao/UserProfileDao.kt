package com.challenges.dailychallenges.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.challenges.dailychallenges.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    
    @Query("SELECT * FROM user_profiles WHERE uid = :uid")
    fun getUserProfile(uid: String): Flow<UserProfile?>
    
    @Query("SELECT * FROM user_profiles WHERE uid = :uid")
    suspend fun getUserProfileOneShot(uid: String): UserProfile?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)
    
    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)
    
    @Query("UPDATE user_profiles SET totalChallenges = :totalChallenges WHERE uid = :uid")
    suspend fun updateTotalChallenges(uid: String, totalChallenges: Int)
    
    @Query("UPDATE user_profiles SET completedChallenges = :completedChallenges WHERE uid = :uid")
    suspend fun updateCompletedChallenges(uid: String, completedChallenges: Int)
    
    @Query("UPDATE user_profiles SET streakDays = :streakDays WHERE uid = :uid")
    suspend fun updateStreakDays(uid: String, streakDays: Int)
    
    @Query("UPDATE user_profiles SET lastActive = :lastActive WHERE uid = :uid")
    suspend fun updateLastActive(uid: String, lastActive: Long)
    
    @Query("DELETE FROM user_profiles WHERE uid = :uid")
    suspend fun deleteUserProfile(uid: String)
} 