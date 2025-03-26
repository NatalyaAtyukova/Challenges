package com.challenges.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.challenges.data.util.Converters
import java.time.LocalDateTime

@Entity(tableName = "achievements")
@TypeConverters(Converters::class)
data class Achievement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val iconName: String,
    val points: Int = 0,
    val isUnlocked: Boolean = false,
    val unlockedAt: LocalDateTime? = null,
    val condition: AchievementType,
    val threshold: Int,
    val progress: Int = 0
)

enum class AchievementType {
    COMPLETED_CHALLENGES,       // Complete X challenges
    COMPLETED_CATEGORY,         // Complete X challenges of category Y
    COMPLETED_DIFFICULTY,       // Complete X challenges of difficulty Y
    POINTS_EARNED,              // Earn X points
    DAYS_STREAK,                // Complete at least one challenge for X consecutive days
    CREATED_CHALLENGES,         // Create X custom challenges
    COMPLETED_CUSTOM_CHALLENGES // Complete X custom challenges
} 