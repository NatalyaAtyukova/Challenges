package com.challenges.dailychallenges.data.repository

import android.util.Log
import com.challenges.dailychallenges.data.model.Challenge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val TAG = "FirebaseRepository"
    private val CHALLENGES_COLLECTION = "challenges"
    private val USER_STATS_COLLECTION = "userStats"

    // Получение текущего ID пользователя или "anonymous" при отсутствии авторизации
    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: "anonymous"
    }

    // Основной метод получения всех челленджей
    fun getAllChallenges(): Flow<List<Challenge>> = flow {
        try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val challenges = snapshot.toObjects(Challenge::class.java)
            Log.d(TAG, "Получено ${challenges.size} челленджей из Firestore")
            emit(challenges)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении челленджей: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение челленджей по категории
    fun getChallengesByCategory(category: String): Flow<List<Challenge>> = flow {
        try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("category", category)
                .get()
                .await()
            
            val challenges = snapshot.toObjects(Challenge::class.java)
            Log.d(TAG, "Получено ${challenges.size} челленджей категории $category из Firestore")
            emit(challenges)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении челленджей категории $category: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение активных челленджей
    fun getActiveChallenges(): Flow<List<Challenge>> = flow {
        try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("completed", false)
                .get()
                .await()
            
            val challenges = snapshot.toObjects(Challenge::class.java)
            Log.d(TAG, "Получено ${challenges.size} активных челленджей из Firestore")
            emit(challenges)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении активных челленджей: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение активных челленджей по категории
    fun getActiveChallengesByCategory(category: String): Flow<List<Challenge>> = flow {
        try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("category", category)
                .whereEqualTo("completed", false)
                .get()
                .await()
            
            val challenges = snapshot.toObjects(Challenge::class.java)
            Log.d(TAG, "Получено ${challenges.size} активных челленджей категории $category из Firestore")
            emit(challenges)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении активных челленджей категории $category: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение завершенных челленджей
    fun getCompletedChallenges(): Flow<List<Challenge>> = flow {
        try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("completed", true)
                .get()
                .await()
            
            val challenges = snapshot.toObjects(Challenge::class.java)
            Log.d(TAG, "Получено ${challenges.size} завершенных челленджей из Firestore")
            emit(challenges)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении завершенных челленджей: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение количества выполненных челленджей
    fun getCompletedChallengesCount(): Flow<Int> = flow {
        try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("completed", true)
                .get()
                .await()
            
            val count = snapshot.size()
            Log.d(TAG, "Количество завершенных челленджей: $count")
            emit(count)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении количества завершенных челленджей: ${e.message}", e)
            emit(0)
        }
    }.flowOn(Dispatchers.IO)

    // Получение избранных челленджей
    fun getFavoriteChallenges(): Flow<List<Challenge>> = flow {
        try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("favorite", true)
                .get()
                .await()
            
            val challenges = snapshot.toObjects(Challenge::class.java)
            Log.d(TAG, "Получено ${challenges.size} избранных челленджей из Firestore")
            emit(challenges)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении избранных челленджей: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение пользовательских челленджей
    fun getCustomChallenges(): Flow<List<Challenge>> = flow {
        try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isCustom", true)
                .get()
                .await()
            
            val challenges = snapshot.toObjects(Challenge::class.java)
            Log.d(TAG, "Получено ${challenges.size} пользовательских челленджей из Firestore")
            emit(challenges)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении пользовательских челленджей: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение количества пользовательских челленджей
    suspend fun getCustomChallengesCount(): Int {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isCustom", true)
                .get()
                .await()
            
            val count = snapshot.size()
            Log.d(TAG, "Количество пользовательских челленджей: $count")
            count
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении количества пользовательских челленджей: ${e.message}", e)
            0
        }
    }

    // Поиск челленджей по запросу
    fun searchChallenges(query: String): Flow<List<Challenge>> = flow {
        try {
            val userId = getCurrentUserId()
            val allChallenges = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Challenge::class.java)
            
            val filteredChallenges = allChallenges.filter { challenge ->
                challenge.title.contains(query, ignoreCase = true) || 
                challenge.description.contains(query, ignoreCase = true)
            }
            
            Log.d(TAG, "Найдено ${filteredChallenges.size} челленджей по запросу '$query'")
            emit(filteredChallenges)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при поиске челленджей: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение челленджа по ID
    suspend fun getChallengeById(id: String): Challenge? {
        return try {
            val userId = getCurrentUserId()
            val document = firestore.collection(CHALLENGES_COLLECTION)
                .document(id)
                .get()
                .await()
            
            val challenge = document.toObject(Challenge::class.java)
            if (challenge?.userId == userId) challenge else null
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении челленджа по ID: ${e.message}", e)
            null
        }
    }

    // Вставка нового челленджа
    suspend fun insertChallenge(challenge: Challenge) {
        try {
            val challengeWithUser = if (challenge.userId.isEmpty()) {
                challenge.copy(userId = getCurrentUserId())
            } else {
                challenge
            }
            
            firestore.collection(CHALLENGES_COLLECTION)
                .document(challenge.id)
                .set(challengeWithUser)
                .await()
            
            Log.d(TAG, "Челлендж сохранен с ID: ${challenge.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при сохранении челленджа: ${e.message}", e)
            throw e
        }
    }

    // Обновление челленджа
    suspend fun updateChallenge(challenge: Challenge) {
        try {
            firestore.collection(CHALLENGES_COLLECTION)
                .document(challenge.id)
                .set(challenge)
                .await()
            
            Log.d(TAG, "Челлендж ${challenge.id} обновлен")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при обновлении челленджа: ${e.message}", e)
            throw e
        }
    }

    // Обновление статуса выполнения
    suspend fun updateCompletionStatus(id: String, completed: Boolean, completedDate: Long?) {
        try {
            val userId = getCurrentUserId()
            val updates = hashMapOf<String, Any>(
                "completed" to completed
            )
            
            if (completedDate != null) {
                updates["completedDate"] = completedDate
            } else {
                // Если completedDate равен null и челлендж не выполнен, удаляем поле
                if (!completed) {
                    updates["completedDate"] = FieldValue.delete()
                }
            }
            
            firestore.collection(CHALLENGES_COLLECTION)
                .document(userId)
                .collection("userChallenges")
                .document(id)
                .update(updates)
                .await()
            
            Log.d(TAG, "Статус выполнения челленджа $id обновлен на $completed")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при обновлении статуса выполнения челленджа $id: ${e.message}", e)
            throw e
        }
    }

    // Удаление челленджа
    suspend fun deleteChallenge(challenge: Challenge) {
        try {
            firestore.collection(CHALLENGES_COLLECTION)
                .document(challenge.id)
                .delete()
                .await()
            
            Log.d(TAG, "Челлендж ${challenge.id} удален")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при удалении челленджа: ${e.message}", e)
            throw e
        }
    }

    // Удаление челленджа по ID
    suspend fun deleteChallengeById(challengeId: String) {
        try {
            firestore.collection(CHALLENGES_COLLECTION)
                .document(challengeId)
                .delete()
                .await()
            
            Log.d(TAG, "Челлендж $challengeId удален")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при удалении челленджа по ID: ${e.message}", e)
            throw e
        }
    }

    // Получение общего количества баллов
    fun getTotalPoints(): Flow<Int> = flow {
        try {
            val userId = getCurrentUserId()
            val userStatsDoc = firestore.collection(USER_STATS_COLLECTION)
                .document(userId)
                .get()
                .await()
            
            val points = userStatsDoc.getLong("totalPoints")?.toInt() ?: 0
            Log.d(TAG, "Общее количество баллов: $points")
            emit(points)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении общего количества баллов: ${e.message}", e)
            emit(0)
        }
    }.flowOn(Dispatchers.IO)

    // Инициализация предустановленных челленджей
    suspend fun initializeDefaultChallenges(defaultChallenges: List<Challenge>) {
        try {
            val userId = getCurrentUserId()
            
            // Сначала проверяем, есть ли уже челленджи у пользователя
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            // Если коллекция пуста, добавляем предустановленные челленджи
            if (snapshot.isEmpty) {
                Log.d(TAG, "Инициализация ${defaultChallenges.size} предустановленных челленджей")
                
                for (challenge in defaultChallenges) {
                    val challengeWithId = challenge.copy(
                        id = UUID.randomUUID().toString(),
                        userId = userId
                    )
                    insertChallenge(challengeWithId)
                }
                
                Log.d(TAG, "Предустановленные челленджи успешно инициализированы")
            } else {
                Log.d(TAG, "Инициализация не требуется, у пользователя уже есть ${snapshot.size()} челленджей")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при инициализации предустановленных челленджей: ${e.message}", e)
            throw e
        }
    }

    // Получение количества выполненных челленджей по категории
    suspend fun getCompletedChallengesByCategory(category: String): Int {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("category", category)
                .whereEqualTo("completed", true)
                .get()
                .await()
            
            snapshot.size()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении количества выполненных челленджей по категории: ${e.message}", e)
            0
        }
    }
    
    // Получение количества выполненных челленджей по сложности
    suspend fun getCompletedChallengesByDifficulty(difficulty: String): Int {
        return try {
            val userId = getCurrentUserId()
            val snapshot = firestore.collection(CHALLENGES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("difficulty", difficulty)
                .whereEqualTo("completed", true)
                .get()
                .await()
            
            snapshot.size()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении количества выполненных челленджей по сложности: ${e.message}", e)
            0
        }
    }

    // Обновление общего количества очков
    suspend fun updateTotalPoints(points: Int) {
        try {
            val userId = getCurrentUserId()
            firestore.collection(USER_STATS_COLLECTION)
                .document(userId)
                .update("totalPoints", FieldValue.increment(points.toLong()))
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при обновлении общего количества очков: ${e.message}", e)
        }
    }
} 