package com.challenges.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: ChallengeCategory,
    val difficulty: ChallengeDifficulty,
    val isFavorite: Boolean = false,
    val isActive: Boolean = true,
    val activationDate: Long = System.currentTimeMillis(),
    val expirationDate: Long = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000 // 7 days by default
)

enum class ChallengeCategory {
    CONVERSATION, // Разговорные
    VIDEO,       // Видео
    PUBLIC,      // Публичные выступления
    DAILY       // Ежедневные
}

enum class ChallengeDifficulty {
    EASY,    // Легкий
    MEDIUM,  // Средний
    HARD     // Сложный
} 