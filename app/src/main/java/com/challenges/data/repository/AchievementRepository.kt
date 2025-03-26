package com.challenges.data.repository

import com.challenges.data.dao.AchievementDao
import com.challenges.data.dao.ChallengeDao
import com.challenges.data.model.Achievement
import com.challenges.data.model.AchievementType
import com.challenges.data.model.Challenge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepository @Inject constructor(
    private val achievementDao: AchievementDao,
    private val challengeDao: ChallengeDao
) {
    // Get all achievements
    fun getAllAchievements(): Flow<List<Achievement>> {
        return achievementDao.getAllAchievements()
    }
    
    // Get unlocked achievements
    fun getUnlockedAchievements(): Flow<List<Achievement>> {
        return achievementDao.getUnlockedAchievements()
    }
    
    // Get locked achievements
    fun getLockedAchievements(): Flow<List<Achievement>> {
        return achievementDao.getLockedAchievements()
    }
    
    // Get achievement points
    fun getAchievementPoints(): Flow<Int?> {
        return achievementDao.getAchievementPoints()
    }
    
    // Get count of unlocked achievements
    fun getUnlockedAchievementsCount(): Flow<Int> {
        return achievementDao.getUnlockedAchievementsCount()
    }
    
    // Insert or update achievement
    suspend fun insertAchievement(achievement: Achievement): Long {
        return achievementDao.insertAchievement(achievement)
    }
    
    // Update achievement progress
    suspend fun updateAchievementProgress(achievementId: Long, progress: Int) {
        achievementDao.updateProgress(achievementId, progress)
        
        // Check if achievement should be unlocked
        val achievement = achievementDao.getAchievementById(achievementId)
        if (achievement != null && !achievement.isUnlocked && progress >= achievement.threshold) {
            achievementDao.unlockAchievement(achievementId, LocalDateTime.now())
        }
    }
    
    // Check and update achievements based on completed challenge
    suspend fun checkAchievements(challenge: Challenge) {
        // Check COMPLETED_CHALLENGES achievements
        val completedCount = challengeDao.getCompletedChallengesCount().first()
        updateAchievementsByType(AchievementType.COMPLETED_CHALLENGES, completedCount)
        
        // Check COMPLETED_CATEGORY achievements
        if (challenge.category.isNotEmpty()) {
            val category = challenge.category
            val categoryAchievements = achievementDao.getAchievementsByType(AchievementType.COMPLETED_CATEGORY)
            categoryAchievements.forEach { achievement ->
                val categoryCompletedCount = challengeDao.getAllChallengesOneShot().count { 
                    it.isCompleted && it.category == category 
                }
                updateAchievementProgress(achievement.id, categoryCompletedCount)
            }
        }
        
        // Check COMPLETED_DIFFICULTY achievements
        if (challenge.difficulty != null) {
            val difficulty = challenge.difficulty
            val difficultyAchievements = achievementDao.getAchievementsByType(AchievementType.COMPLETED_DIFFICULTY)
            difficultyAchievements.forEach { achievement ->
                val difficultyCompletedCount = challengeDao.getAllChallengesOneShot().count { 
                    it.isCompleted && it.difficulty == difficulty 
                }
                updateAchievementProgress(achievement.id, difficultyCompletedCount)
            }
        }
        
        // Check POINTS_EARNED achievements
        val pointsEarned = challengeDao.getTotalPoints().first() ?: 0
        updateAchievementsByType(AchievementType.POINTS_EARNED, pointsEarned)
        
        // Check COMPLETED_CUSTOM_CHALLENGES achievements
        if (challenge.isCustom) {
            val customCompletedCount = challengeDao.getAllChallengesOneShot().count { it.isCompleted && it.isCustom }
            updateAchievementsByType(AchievementType.COMPLETED_CUSTOM_CHALLENGES, customCompletedCount)
        }
    }
    
    // Initialize default achievements if needed
    suspend fun initializeDefaultAchievements() {
        val achievements = achievementDao.getAllAchievements().first()
        if (achievements.isEmpty()) {
            // Completed Challenges achievements
            insertAchievement(Achievement(
                title = "Начинающий",
                description = "Выполнить 5 челленджей",
                iconName = "emoji_events",
                points = 50,
                condition = AchievementType.COMPLETED_CHALLENGES,
                threshold = 5
            ))
            
            insertAchievement(Achievement(
                title = "Уверенный",
                description = "Выполнить 20 челленджей",
                iconName = "workspace_premium",
                points = 100,
                condition = AchievementType.COMPLETED_CHALLENGES,
                threshold = 20
            ))
            
            insertAchievement(Achievement(
                title = "Мастер челленджей",
                description = "Выполнить 50 челленджей",
                iconName = "military_tech",
                points = 200,
                condition = AchievementType.COMPLETED_CHALLENGES,
                threshold = 50
            ))
            
            // Points achievements
            insertAchievement(Achievement(
                title = "Первые очки",
                description = "Набрать 500 очков",
                iconName = "toll",
                points = 50,
                condition = AchievementType.POINTS_EARNED,
                threshold = 500
            ))
            
            insertAchievement(Achievement(
                title = "Коллекционер очков",
                description = "Набрать 2000 очков",
                iconName = "diamond",
                points = 100,
                condition = AchievementType.POINTS_EARNED,
                threshold = 2000
            ))
            
            // Category achievements
            insertAchievement(Achievement(
                title = "Разговорчивый",
                description = "Выполнить 10 разговорных челленджей",
                iconName = "chat",
                points = 75,
                condition = AchievementType.COMPLETED_CATEGORY,
                threshold = 10
            ))
            
            // Difficulty achievements
            insertAchievement(Achievement(
                title = "Храбрый",
                description = "Выполнить 5 сложных челленджей",
                iconName = "fitness_center",
                points = 100,
                condition = AchievementType.COMPLETED_DIFFICULTY,
                threshold = 5
            ))
            
            // Custom challenges
            insertAchievement(Achievement(
                title = "Креативщик",
                description = "Выполнить 3 пользовательских челленджа",
                iconName = "brush",
                points = 75,
                condition = AchievementType.COMPLETED_CUSTOM_CHALLENGES,
                threshold = 3
            ))
        }
    }
    
    private suspend fun updateAchievementsByType(type: AchievementType, progress: Int) {
        val achievements = achievementDao.getAchievementsByType(type)
        
        achievements.forEach { achievement ->
            updateAchievementProgress(achievement.id, progress)
        }
    }
} 