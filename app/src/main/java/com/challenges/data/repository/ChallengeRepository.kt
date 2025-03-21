package com.challenges.data.repository

import com.challenges.data.local.ChallengeDao
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val challengeDao: ChallengeDao
) {
    fun getActiveChallenges(): Flow<List<Challenge>> = challengeDao.getActiveChallenges()
    
    fun getActiveChallengesByCategory(category: ChallengeCategory): Flow<List<Challenge>> = 
        challengeDao.getActiveChallengesByCategory(category)
    
    fun getFavoriteChallenges(): Flow<List<Challenge>> = challengeDao.getFavoriteChallenges()
    
    suspend fun getRandomChallenge(): Challenge? = challengeDao.getRandomActiveChallenge()
    
    suspend fun toggleFavorite(challenge: Challenge) {
        challengeDao.updateFavoriteStatus(challenge.id, !challenge.isFavorite)
    }
    
    suspend fun insertChallenge(challenge: Challenge) = challengeDao.insertChallenge(challenge)
    
    suspend fun updateChallenge(challenge: Challenge) = challengeDao.updateChallenge(challenge)
    
    suspend fun deleteChallenge(challenge: Challenge) = challengeDao.deleteChallenge(challenge)
} 