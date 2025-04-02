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
            
            // Достижения по сложности (устарело)
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
            ),
            
            // Новые достижения по очкам
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Points Enthusiast",
                description = "Complete challenges worth 50+ points in total",
                iconName = "badge",
                points = 20,
                isUnlocked = false,
                condition = AchievementType.POINTS_LOW.value,
                threshold = 50
            ),
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Points Expert",
                description = "Complete challenges worth 100+ points in total",
                iconName = "star",
                points = 30,
                isUnlocked = false,
                condition = AchievementType.POINTS_MEDIUM.value,
                threshold = 100
            ),
            Achievement(
                id = UUID.randomUUID().toString(),
                title = "Points Master",
                description = "Complete challenges worth 200+ points in total",
                iconName = "trophy",
                points = 50,
                isUnlocked = false,
                condition = AchievementType.POINTS_HIGH.value,
                threshold = 200
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

    // Проверка достижений по количеству очков выполненного челленджа 
    fun checkPointsCompletion(points: Int) {
        Log.d("AchievementRepository", "Checking points completion for points: $points")
        
        try {
            // Get completed challenges with points
            firebaseRepository.getAllCompletedChallenges { result ->
                if (result.isSuccess) {
                    val completedChallenges = result.getOrNull() ?: emptyList()
                    val totalPoints = completedChallenges.sumOf { it.points }
                    
                    Log.d("AchievementRepository", "Total points from completed challenges: $totalPoints")
                    
                    // Check for high points completion (200+)
                    if (totalPoints >= 200) {
                        val achievement = Achievement(
                            title = "Points Master",
                            description = "Complete challenges worth 200+ points in total",
                            iconName = "trophy",
                            points = 50,
                            condition = AchievementType.POINTS_HIGH.value
                        )
                        unlockAchievement(achievement)
                    }
                    
                    // Check for medium points completion (100-199)
                    if (totalPoints >= 100) {
                        val achievement = Achievement(
                            title = "Points Expert",
                            description = "Complete challenges worth 100+ points in total",
                            iconName = "star",
                            points = 30,
                            condition = AchievementType.POINTS_MEDIUM.value
                        )
                        unlockAchievement(achievement)
                    }
                    
                    // Check for low points completion (1-99)
                    if (totalPoints >= 50) {
                        val achievement = Achievement(
                            title = "Points Enthusiast",
                            description = "Complete challenges worth 50+ points in total",
                            iconName = "badge",
                            points = 20,
                            condition = AchievementType.POINTS_LOW.value
                        )
                        unlockAchievement(achievement)
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    Log.e("AchievementRepository", "Failed to get completed challenges: ${exception?.message}", exception)
                }
            }
        } catch (e: Exception) {
            Log.e("AchievementRepository", "Error in checkPointsCompletion: ${e.message}", e)
        }
    }
    
    // Сохраняем для обратной совместимости
    @Deprecated("Используйте checkPointsCompletion вместо этого метода")
    suspend fun checkDifficultyCompletion(difficulty: String) {
        Log.d(TAG, "Проверка достижений по сложности: $difficulty (устарело)")
        // Просто делегируем в новый метод с соответствующим диапазоном очков
        when (difficulty.uppercase()) {
            "HARD" -> checkPointsCompletion(200)
            "MEDIUM" -> checkPointsCompletion(100)
            "EASY" -> checkPointsCompletion(50)
            else -> checkPointsCompletion(10)
        }
    }

    // Разблокировка достижения по его условию
    private fun unlockAchievement(achievement: Achievement) {
        try {
            val userId = firebaseRepository.getCurrentUserId()
            if (userId.isEmpty()) {
                Log.e(TAG, "Не удалось разблокировать достижение: пользователь не авторизован")
                return
            }
            
            // Проверяем, существует ли уже достижение с таким условием
            firestore.collection(ACHIEVEMENTS_COLLECTION)
                .document(userId)
                .collection("userAchievements")
                .whereEqualTo("condition", achievement.condition)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.isEmpty) {
                        // Если достижения с таким условием нет, создаем новое
                        val newAchievement = achievement.copy(
                            id = UUID.randomUUID().toString(),
                            isUnlocked = true,
                            unlockedAt = System.currentTimeMillis(),
                            progress = achievement.threshold
                        )
                        
                        firestore.collection(ACHIEVEMENTS_COLLECTION)
                            .document(userId)
                            .collection("userAchievements")
                            .document(newAchievement.id)
                            .set(newAchievement)
                            .addOnSuccessListener {
                                Log.d(TAG, "Разблокировано новое достижение: ${newAchievement.title}")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Ошибка при сохранении нового достижения: ${e.message}", e)
                            }
                    } else {
                        // Если достижение уже существует, проверяем, разблокировано ли оно
                        val existingAchievement = snapshot.documents.first().toObject(Achievement::class.java)
                        if (existingAchievement != null && !existingAchievement.isUnlocked) {
                            // Если не разблокировано, обновляем его
                            val updates = mapOf(
                                "isUnlocked" to true,
                                "unlockedAt" to System.currentTimeMillis(),
                                "progress" to existingAchievement.threshold
                            )
                            
                            firestore.collection(ACHIEVEMENTS_COLLECTION)
                                .document(userId)
                                .collection("userAchievements")
                                .document(existingAchievement.id)
                                .update(updates)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Разблокировано существующее достижение: ${existingAchievement.title}")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Ошибка при обновлении существующего достижения: ${e.message}", e)
                                }
                        } else {
                            Log.d(TAG, "Достижение уже разблокировано")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Ошибка при проверке существующего достижения: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при разблокировке достижения: ${e.message}", e)
        }
    }
    
    // Синхронизация достижений с Firebase
    suspend fun syncAchievements() {
        try {
            val userId = firebaseRepository.getCurrentUserId()
            if (userId.isEmpty()) {
                Log.e(TAG, "Ошибка при синхронизации достижений: пользователь не авторизован")
                return
            }
            
            Log.d(TAG, "Начало синхронизации достижений")
            
            // Получаем все достижения из Firestore
            val snapshot = firestore.collection(ACHIEVEMENTS_COLLECTION)
                .document(userId)
                .collection("userAchievements")
                .get()
                .await()
            
            val achievements = snapshot.toObjects(Achievement::class.java)
            
            // Проверяем, если нет достижений, создаем дефолтные
            if (achievements.isEmpty()) {
                val defaultAchievements = createDefaultAchievements()
                for (achievement in defaultAchievements) {
                    insertAchievement(achievement)
                }
                Log.d(TAG, "Инициализировано ${defaultAchievements.size} достижений")
            } else {
                Log.d(TAG, "Синхронизировано ${achievements.size} достижений")
            }
            
            // Обновим прогресс достижений
            checkAllAchievements()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при синхронизации достижений: ${e.message}", e)
            throw e
        }
    }
    
    // Проверка всех типов достижений
    private suspend fun checkAllAchievements() {
        try {
            // Проверяем достижения по категориям
            val categories = listOf("CONVERSATION", "VIDEO", "PUBLIC", "CONTENT", "PODCAST", "WRITING")
            for (category in categories) {
                checkCategoryCompletion(category)
            }
            
            // Проверяем достижения по созданию пользовательских челленджей
            checkCustomChallengesCreation()
            
            // Проверяем достижения по очкам
            checkPointsCompletion(0) // Передаем 0, так как внутри метода будет расчет всех очков
            
            Log.d(TAG, "Все типы достижений проверены")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при проверке достижений: ${e.message}", e)
        }
    }
} 