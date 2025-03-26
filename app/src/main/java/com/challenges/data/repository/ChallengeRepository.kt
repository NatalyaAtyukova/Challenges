package com.challenges.data.repository

import com.challenges.data.dao.ChallengeDao
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val challengeDao: ChallengeDao
) {
    fun getAllActiveChallenges(): Flow<List<Challenge>> = challengeDao.getAllActiveChallenges()
    
    fun getActiveChallengesByCategory(category: String): Flow<List<Challenge>> = 
        challengeDao.getActiveChallengesByCategory(category)
    
    fun getFavoriteChallenges(): Flow<List<Challenge>> = challengeDao.getFavoriteChallenges()
    
    fun getCustomChallenges(): Flow<List<Challenge>> = challengeDao.getCustomChallenges()
    
    fun getActiveSeasonalChallenges(now: LocalDateTime): Flow<List<Challenge>> = 
        challengeDao.getActiveSeasonalChallenges(now)
    
    fun searchChallenges(query: String): Flow<List<Challenge>> = challengeDao.searchChallenges(query)
    
    fun getChallengesByPoints(): Flow<List<Challenge>> = challengeDao.getChallengesByPoints()
    
    fun getChallengesByCategory(): Flow<List<Challenge>> = challengeDao.getChallengesByCategory()
    
    fun getChallengesByDate(): Flow<List<Challenge>> = challengeDao.getChallengesByDate()
    
    suspend fun getChallengeById(id: Long): Challenge? = challengeDao.getChallengeById(id)
    
    suspend fun insertChallenge(challenge: Challenge): Long = challengeDao.insertChallenge(challenge)
    
    suspend fun updateChallenge(challenge: Challenge) = challengeDao.updateChallenge(challenge)
    
    suspend fun deleteChallenge(challenge: Challenge) = challengeDao.deleteChallenge(challenge)
    
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = 
        challengeDao.updateFavoriteStatus(id, isFavorite)
    
    suspend fun updateCompletionStatus(id: Long, isCompleted: Boolean) = 
        challengeDao.updateCompletionStatus(id, isCompleted)
    
    suspend fun updateNotes(id: Long, notes: String?) = challengeDao.updateNotes(id, notes)
    
    fun getTotalPoints(): Flow<Int?> = challengeDao.getTotalPoints()
    
    fun getCompletedChallengesCount(): Flow<Int> = challengeDao.getCompletedChallengesCount()
    
    suspend fun getCustomChallengesCount(): Int {
        return challengeDao.getAllChallengesOneShot().count { it.isCustom }
    }
    
    suspend fun getRandomChallenge(): Challenge? {
        val challenges = challengeDao.getAllChallengesOneShot()
        return challenges.filter { !it.isCompleted }.randomOrNull()
    }
} 