package com.challenges.data.dao

import androidx.room.*
import com.challenges.data.model.Achievement
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, points DESC")
    fun getAllAchievements(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    fun getUnlockedAchievements(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE isUnlocked = 0 ORDER BY points ASC")
    fun getLockedAchievements(): Flow<List<Achievement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement): Long

    @Update
    suspend fun updateAchievement(achievement: Achievement)

    @Query("UPDATE achievements SET progress = :progress WHERE id = :id")
    suspend fun updateProgress(id: Long, progress: Int)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedAt = :now WHERE id = :id")
    suspend fun unlockAchievement(id: Long, now: java.time.LocalDateTime)

    @Query("SELECT SUM(points) FROM achievements WHERE isUnlocked = 1")
    fun getAchievementPoints(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM achievements WHERE isUnlocked = 1")
    fun getUnlockedAchievementsCount(): Flow<Int>

    @Query("SELECT * FROM achievements WHERE condition = :condition")
    suspend fun getAchievementsByType(condition: com.challenges.data.model.AchievementType): List<Achievement>

    @Query("SELECT * FROM achievements WHERE id = :id")
    suspend fun getAchievementById(id: Long): Achievement?
} 