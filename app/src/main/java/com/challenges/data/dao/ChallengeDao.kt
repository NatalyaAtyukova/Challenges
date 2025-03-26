package com.challenges.data.dao

import androidx.room.*
import com.challenges.data.model.Challenge
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges ORDER BY createdAt DESC")
    fun getAllActiveChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE category = :category ORDER BY createdAt DESC")
    fun getActiveChallengesByCategory(category: String): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE isCustom = 1 ORDER BY createdAt DESC")
    fun getCustomChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE category = 'COMMUNITY' ORDER BY createdAt DESC")
    fun getCommunityChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE category = 'COMMUNITY'")
    suspend fun getCommunityOneShot(): List<Challenge>

    @Query("SELECT * FROM challenges WHERE isSeasonal = 1 AND startDate <= :now AND endDate >= :now ORDER BY createdAt DESC")
    fun getActiveSeasonalChallenges(now: LocalDateTime): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchChallenges(query: String): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges ORDER BY points DESC")
    fun getChallengesByPoints(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges ORDER BY category ASC")
    fun getChallengesByCategory(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges ORDER BY createdAt DESC")
    fun getChallengesByDate(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE id = :id")
    suspend fun getChallengeById(id: Long): Challenge?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge): Long

    @Update
    suspend fun updateChallenge(challenge: Challenge)

    @Delete
    suspend fun deleteChallenge(challenge: Challenge)

    @Query("UPDATE challenges SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    @Query("UPDATE challenges SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletionStatus(id: Long, isCompleted: Boolean)

    @Query("UPDATE challenges SET notes = :notes WHERE id = :id")
    suspend fun updateNotes(id: Long, notes: String?)

    @Query("SELECT SUM(points) FROM challenges WHERE isCompleted = 1")
    fun getTotalPoints(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM challenges WHERE isCompleted = 1")
    fun getCompletedChallengesCount(): Flow<Int>

    @Query("SELECT * FROM challenges")
    suspend fun getAllChallengesOneShot(): List<Challenge>

    @Query("DELETE FROM challenges")
    suspend fun clearAll()
} 