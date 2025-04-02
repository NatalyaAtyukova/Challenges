package com.challenges.dailychallenges.data.repository

import android.util.Log
import com.challenges.dailychallenges.data.model.Challenge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    private val TAG = "ChallengeRepository"
    // Делегирование основных методов в FirebaseRepository
    
    fun getAllChallenges(): Flow<List<Challenge>> = firebaseRepository.getAllChallenges()
    
    fun getChallengesByCategory(category: String): Flow<List<Challenge>> = 
        firebaseRepository.getChallengesByCategory(category)
    
    fun getCustomChallenges(): Flow<List<Challenge>> = firebaseRepository.getCustomChallenges()
    
    fun getActiveChallengesByCategory(category: String): Flow<List<Challenge>> = 
        firebaseRepository.getActiveChallengesByCategory(category)
    
    fun getCompletedChallengesCount(): Flow<Int> = firebaseRepository.getCompletedChallengesCount()
    
    fun getTotalPoints(): Flow<Int> = firebaseRepository.getTotalPoints()

    suspend fun getChallengeById(id: String): Challenge? = firebaseRepository.getChallengeById(id)
    
    suspend fun insertChallenge(challenge: Challenge) = firebaseRepository.insertChallenge(challenge)
    
    suspend fun updateChallenge(challenge: Challenge) = firebaseRepository.updateChallenge(challenge)
    
    suspend fun deleteChallenge(challenge: Challenge) = firebaseRepository.deleteChallenge(challenge)
    
    suspend fun deleteChallengeById(id: String) = firebaseRepository.deleteChallengeById(id)

    fun searchChallenges(query: String): Flow<List<Challenge>> = firebaseRepository.searchChallenges(query)
    
    // Новые методы для синхронизации
    suspend fun syncAllChallenges() {
        Log.d(TAG, "Начало синхронизации всех челленджей")
        try {
            // Синхронизация просто запрашивает актуальные данные из Firestore
            // Так как мы храним данные только в Firebase, фактической синхронизации не требуется
            val challenges = firebaseRepository.getAllChallenges().first()
            Log.d(TAG, "Синхронизировано ${challenges.size} челленджей")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при синхронизации челленджей: ${e.message}", e)
        }
    }
    
    fun getAllChallengesOneTime(): Flow<List<Challenge>> = firebaseRepository.getAllChallengesOneTime()
    
    fun getAllChallengesFromFirebase(): Flow<List<Challenge>> {
        // Теперь используем одноразовый запрос для более надежной загрузки
        return firebaseRepository.getAllChallengesOneTime()
    }
    
    fun getChallengesByDate(): Flow<List<Challenge>> {
        // Этот метод фактически возвращает все челленджи, так как сортировка по дате
        // уже реализована в FirebaseRepository.getAllChallenges()
        return firebaseRepository.getAllChallengesOneTime()
    }
} 