/*
package com.challenges.dailychallenges.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.challenges.dailychallenges.data.model.Achievement
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<Achievement>>
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 1")
    fun getUnlockedAchievements(): Flow<List<Achievement>>
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 0")
    fun getLockedAchievements(): Flow<List<Achievement>>
    
    @Query("SELECT * FROM achievements WHERE id = :id")
    suspend fun getAchievementById(id: Long): Achievement?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement): Long
    
    @Update
    suspend fun updateAchievement(achievement: Achievement)
    
    @Delete
    suspend fun deleteAchievement(achievement: Achievement)
    
    @Query("DELETE FROM achievements")
    suspend fun clearAll()
    
    @Query("UPDATE achievements SET progress = :progress, isUnlocked = CASE WHEN progress >= threshold THEN 1 ELSE 0 END, unlockedAt = CASE WHEN progress >= threshold AND isUnlocked = 0 THEN datetime('now') ELSE unlockedAt END WHERE id = :id")
    suspend fun updateProgress(id: Long, progress: Int)
    
    @Query("SELECT COUNT(*) FROM achievements WHERE isUnlocked = 1")
    fun getUnlockedAchievementsCount(): Flow<Int>
    
    @Query("SELECT SUM(points) FROM achievements WHERE isUnlocked = 1")
    fun getTotalPoints(): Flow<Int>
    
    @Query("SELECT * FROM achievements WHERE condition = :condition")
    suspend fun getAchievementsByCondition(condition: String): List<Achievement>
    
    @Query("UPDATE achievements SET isUnlocked = 1, unlockedAt = datetime('now') WHERE id = :id")
    suspend fun unlockAchievement(id: Long)
}
*/ 