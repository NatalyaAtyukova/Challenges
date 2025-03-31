/*
package com.challenges.dailychallenges.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.challenges.dailychallenges.data.model.Challenge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges ORDER BY createdDate DESC")
    fun getAllChallenges(): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE id = :id")
    suspend fun getChallengeById(id: String): Challenge?
    
    @Query("SELECT * FROM challenges WHERE category = :category")
    fun getChallengesByCategory(category: String): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchChallenges(query: String): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE completed = 1")
    fun getCompletedChallenges(): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE completed = 0")
    fun getUncompletedChallenges(): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE userId = :userId")
    fun getChallengesByUserId(userId: String): Flow<List<Challenge>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge)
    
    @Update
    suspend fun updateChallenge(challenge: Challenge)
    
    @Delete
    suspend fun deleteChallenge(challenge: Challenge)
    
    @Query("DELETE FROM challenges WHERE id = :id")
    suspend fun deleteChallengeById(id: String)
    
    @Query("UPDATE challenges SET completed = :completed, completedDate = :completedDate WHERE id = :id")
    suspend fun updateCompletionStatus(id: String, completed: Boolean, completedDate: Long?)
    
    @Query("SELECT COUNT(*) FROM challenges WHERE completed = 1")
    fun getCompletedChallengesCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM challenges WHERE category = :category")
    suspend fun getChallengesCountByCategory(category: String): Int
    
    @Query("SELECT * FROM challenges")
    suspend fun getAllChallengesAsList(): List<Challenge>
    
    @Query("SELECT * FROM challenges WHERE completed = 0")
    fun getAllActiveChallenges(): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE category = :category AND completed = 0")
    fun getActiveChallengesByCategory(category: String): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE favorite = 1")
    fun getFavoriteChallenges(): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE isCustom = 1")
    fun getCustomChallenges(): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE isSeasonal = 1 AND startDate <= :now AND (endDate IS NULL OR endDate >= :now)")
    fun getActiveSeasonalChallenges(now: Long): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges ORDER BY points DESC")
    fun getChallengesByPoints(): Flow<List<Challenge>>
    
    @Query("SELECT * FROM challenges ORDER BY createdDate DESC")
    fun getChallengesByDate(): Flow<List<Challenge>>
    
    @Query("UPDATE challenges SET favorite = NOT favorite WHERE id = :id")
    suspend fun toggleFavorite(id: String)
    
    @Query("UPDATE challenges SET notes = :notes WHERE id = :id")
    suspend fun updateNotes(id: String, notes: String?)
    
    @Query("DELETE FROM challenges")
    suspend fun clearAll()
    
    @Query("SELECT SUM(points) FROM challenges WHERE completed = 1")
    fun getTotalPoints(): Flow<Int?>
}
*/ 