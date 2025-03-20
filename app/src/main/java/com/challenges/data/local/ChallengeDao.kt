package com.challenges.data.local

import androidx.room.*
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges")
    fun getAllChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges")
    suspend fun getAllChallengesOneShot(): List<Challenge>

    @Query("SELECT * FROM challenges WHERE category = :category")
    fun getChallengesByCategory(category: ChallengeCategory): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE isFavorite = 1")
    fun getFavoriteChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomChallenge(): Challenge?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge)

    @Update
    suspend fun updateChallenge(challenge: Challenge)

    @Delete
    suspend fun deleteChallenge(challenge: Challenge)

    @Query("UPDATE challenges SET isFavorite = :isFavorite WHERE id = :challengeId")
    suspend fun updateFavoriteStatus(challengeId: Long, isFavorite: Boolean)
} 