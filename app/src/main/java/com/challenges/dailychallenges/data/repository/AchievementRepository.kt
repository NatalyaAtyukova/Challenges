package com.challenges.dailychallenges.data.repository

import android.util.Log
import com.challenges.dailychallenges.data.model.Achievement
import com.challenges.dailychallenges.data.model.AchievementType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseRepository: FirebaseRepository
) {
    private val TAG = "AchievementRepository"
    private val ACHIEVEMENTS_COLLECTION = "achievements"

    // Получение всех достижений пользователя
    fun getAllAchievements(): Flow<List<Achievement>> = flow {
        try {
            val userId = firebaseRepository.getCurrentUserId()
            val snapshot = firestore.collection(ACHIEVEMENTS_COLLECTION)
                .document(userId)
                .collection("userAchievements")
                .get()
                .await()
            
            val achievements = snapshot.toObjects(Achievement::class.java)
            if (achievements.isEmpty()) {
                // Если достижений нет, инициализируем их
                val defaultAchievements = createDefaultAchievements()
                for (achievement in defaultAchievements) {
                    insertAchievement(achievement)
                }
                emit(defaultAchievements)
            } else {
                emit(achievements)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении достижений: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    // Получение достижения по ID
    suspend fun getAchievementById(id: String): Achievement? {
        return try {
            val userId = firebaseRepository.getCurrentUserId()
            val document = firestore.collection(ACHIEVEMENTS_COLLECTION)
                .document(userId)
                .collection("userAchievements")
                .document(id)
                .get()
                .await()
            
            document.toObject(Achievement::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении достижения с ID $id: ${e.message}", e)
            null
        }
    }

    // Добавление нового достижения
    suspend fun insertAchievement(achievement: Achievement): String {
        return try {
            val userId = firebaseRepository.getCurrentUserId()
            val achievementId = if (achievement.id.isEmpty()) UUID.randomUUID().toString() else achievement.id
            val finalAchievement = if (achievement.id.isEmpty()) achievement.copy(id = achievementId) else achievement
            
            firestore.collection(ACHIEVEMENTS_COLLECTION)
                .document(userId)
                .collection("userAchievements")
                .document(achievementId)
                .set(finalAchievement)
                .await()
            
            achievementId
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при добавлении достижения: ${e.message}", e)
            throw e
        }
    }

    // Обновление прогресса достижения
    suspend fun updateAchievementProgress(id: String, progress: Int) {
        try {
            val userId = firebaseRepository.getCurrentUserId()
            val achievement = getAchievementById(id) ?: return
            
            // Проверяем, если прогресс достиг порога, отмечаем как разблокированное
            val isUnlocked = progress >= achievement.threshold
            val unlockedAt = if (isUnlocked && !achievement.isUnlocked) System.currentTimeMillis() else achievement.unlockedAt
            
            val updates = mapOf(
                "progress" to progress,
                "isUnlocked" to isUnlocked,
                "unlockedAt" to unlockedAt
            )
            
            firestore.collection(ACHIEVEMENTS_COLLECTION)
                .document(userId)
                .collection("userAchievements")
                .document(id)
                .update(updates)
                .await()
            
            Log.d(TAG, "Обновлен прогресс достижения $id: $progress/${achievement.threshold}")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при обновлении прогресса достижения $id: ${e.message}", e)
            throw e
        }
    }

    // Проверка и обновление достижений, связанных с завершением челленджей по категории
    suspend fun checkCategoryCompletion(category: String) = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Проверка достижений по категории: $category")
            
            // Получаем количество завершенных челленджей в этой категории
            val completedCount = firebaseRepository.getCompletedChallengesByCategory(category)
            
            // Получаем все достижения типа CATEGORY_COMPLETED
            val achievementsFlow = getAllAchievements()
            val achievements = achievementsFlow.firstOrNull() ?: emptyList()
            
            achievements.forEach { achievement ->
                if (achievement.condition == AchievementType.CATEGORY_COMPLETED.value && achievement.category == category) {
                    updateAchievementProgress(achievement.id, completedCount)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при проверке достижений по категории: ${e.message}", e)
        }
    }

    // Проверка и обновление достижений, связанных с сложностью челленджей
    suspend fun checkDifficultyCompletion(difficulty: String) = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Проверка достижений по сложности: $difficulty")
            
            // Получаем количество завершенных челленджей данной сложности
            val completedCount = firebaseRepository.getCompletedChallengesByDifficulty(difficulty)
            
            // Получаем все достижения типа DIFFICULTY_COMPLETED
            val achievementsFlow = getAllAchievements()
            val achievements = achievementsFlow.firstOrNull() ?: emptyList()
            
            achievements.forEach { achievement ->
                if (achievement.condition == AchievementType.DIFFICULTY_COMPLETED.value && achievement.difficulty == difficulty) {
                    updateAchievementProgress(achievement.id, completedCount)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при проверке достижений по сложности: ${e.message}", e)
        }
    }

    // Проверка и обновление достижений, связанных с созданием пользовательских челленджей
    suspend fun checkCustomChallengesCreation() = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Проверка достижений по созданию пользовательских челленджей")
            
            // Получаем количество созданных пользовательских челленджей
            val customChallengesCount = firebaseRepository.getCustomChallengesCount()
            
            // Получаем все достижения типа CREATED_CHALLENGES
            val achievementsFlow = getAllAchievements()
            val achievements = achievementsFlow.firstOrNull() ?: emptyList()
            
            achievements.forEach { achievement ->
                if (achievement.condition == AchievementType.CREATED_CHALLENGES.value) {
                    updateAchievementProgress(achievement.id, customChallengesCount)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при проверке достижений по созданию челленджей: ${e.message}", e)
        }
    }

    // Создание списка стандартных достижений
    private fun createDefaultAchievements(): List<Achievement> {
        return listOf(
            // Достижения по категориям
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Conversation Starter",
                description = "Complete 5 conversation challenges",
                iconName = "ic_conversation",
                points = 50,
                isUnlocked = false,
                condition = AchievementType.CATEGORY_COMPLETED.value,
                category = "CONVERSATION",
                threshold = 5
            ),
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Content Creator",
                description = "Complete 3 content creation challenges",
                iconName = "ic_content",
                points = 50,
                isUnlocked = false,
                condition = AchievementType.CATEGORY_COMPLETED.value,
                category = "CONTENT",
                threshold = 3
            ),
            
            // Достижения по сложности
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Warming Up",
                description = "Complete 10 easy challenges",
                iconName = "ic_easy",
                points = 50,
                isUnlocked = false,
                condition = AchievementType.DIFFICULTY_COMPLETED.value,
                difficulty = "EASY",
                threshold = 10
            ),
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Challenge Taker",
                description = "Complete 5 medium challenges",
                iconName = "ic_medium",
                points = 100,
                isUnlocked = false,
                condition = AchievementType.DIFFICULTY_COMPLETED.value,
                difficulty = "MEDIUM",
                threshold = 5
            ),
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Challenge Master",
                description = "Complete 3 hard challenges",
                iconName = "ic_hard",
                points = 150,
                isUnlocked = false,
                condition = AchievementType.DIFFICULTY_COMPLETED.value,
                difficulty = "HARD",
                threshold = 3
            ),
            
            // Достижения по созданию челленджей
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Challenge Creator",
                description = "Create your first challenge",
                iconName = "ic_create",
                points = 50,
                isUnlocked = false,
                condition = AchievementType.CREATED_CHALLENGES.value,
                threshold = 1
            ),
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Challenge Designer",
                description = "Create 5 custom challenges",
                iconName = "ic_design",
                points = 100,
                isUnlocked = false,
                condition = AchievementType.CREATED_CHALLENGES.value,
                threshold = 5
            )
        )
    }

    // Получение количества разблокированных достижений
    fun getUnlockedAchievementsCount(): Flow<Int> = flow {
        try {
            val achievementsFlow = getAllAchievements()
            val achievements = achievementsFlow.firstOrNull() ?: emptyList()
            val count = achievements.count { it.isUnlocked }
            emit(count)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении количества разблокированных достижений: ${e.message}", e)
            emit(0)
        }
    }.flowOn(Dispatchers.IO)
    
    // Получение разблокированных достижений
    fun getUnlockedAchievements(): Flow<List<Achievement>> = flow {
        try {
            val achievementsFlow = getAllAchievements()
            val achievements = achievementsFlow.firstOrNull() ?: emptyList()
            emit(achievements.filter { it.isUnlocked })
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении разблокированных достижений: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
    
    // Получение заблокированных достижений
    fun getLockedAchievements(): Flow<List<Achievement>> = flow {
        try {
            val achievementsFlow = getAllAchievements()
            val achievements = achievementsFlow.firstOrNull() ?: emptyList()
            emit(achievements.filter { !it.isUnlocked })
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении заблокированных достижений: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
    
    // Получение общего количества очков за достижения
    fun getAchievementPoints(): Flow<Int> = flow {
        try {
            val achievementsFlow = getAllAchievements()
            val achievements = achievementsFlow.firstOrNull() ?: emptyList()
            val points = achievements.filter { it.isUnlocked }.sumOf { it.points }
            emit(points)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении очков за достижения: ${e.message}", e)
            emit(0)
        }
    }.flowOn(Dispatchers.IO)
    
    // Получение недавно разблокированных достижений
    fun getRecentlyUnlockedAchievements(timeWindowMs: Long = 86400000): Flow<List<Achievement>> = flow {
        try {
            val now = System.currentTimeMillis()
            val achievementsFlow = getAllAchievements()
            val achievements = achievementsFlow.firstOrNull() ?: emptyList()
            
            val recentAchievements = achievements.filter { achievement -> 
                achievement.isUnlocked && 
                achievement.unlockedAt != null && 
                (now - achievement.unlockedAt < timeWindowMs)
            }.sortedByDescending { it.unlockedAt }
            
            emit(recentAchievements)
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении недавно разблокированных достижений: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
} 