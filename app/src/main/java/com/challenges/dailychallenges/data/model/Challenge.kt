package com.challenges.dailychallenges.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.challenges.dailychallenges.data.util.Converters

@Entity(tableName = "challenges")
@TypeConverters(Converters::class)
data class Challenge(
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = ChallengeCategory.OTHER.value,
    val difficulty: String? = "EASY",
    val points: Int = 10,
    val isCustom: Boolean = false,
    val isSeasonal: Boolean = false,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val favorite: Boolean = false,
    val completed: Boolean = false,
    val notes: String? = null,
    val createdDate: Long = System.currentTimeMillis(),
    val completedDate: Long? = null,
    val userId: String = "",
    val userName: String? = null
) {
    constructor() : this(
        id = "",
        title = "",
        description = "",
        category = ChallengeCategory.OTHER.value,
        difficulty = "EASY",
        points = 10,
        isCustom = false,
        isSeasonal = false,
        startDate = null,
        endDate = null,
        favorite = false,
        completed = false,
        notes = null,
        createdDate = System.currentTimeMillis(),
        completedDate = null,
        userId = "",
        userName = null
    )
}

enum class ChallengeCategory(val value: String, val displayName: String) {
    CONVERSATION("CONVERSATION", "Разговор"),
    VIDEO("VIDEO", "Видео"),
    PUBLIC("PUBLIC", "Публичное выступление"),
    DAILY("DAILY", "Ежедневное"),
    CONTENT("CONTENT", "Контент"),
    TEACHING("TEACHING", "Обучение"),
    PODCAST("PODCAST", "Подкаст"),
    WRITING("WRITING", "Письмо"),
    CONSULTING("CONSULTING", "Консультация"),
    COMMUNITY("COMMUNITY", "Сообщество"),
    FITNESS("FITNESS", "Фитнес"),
    MINDFULNESS("MINDFULNESS", "Осознанность"),
    SOCIAL("SOCIAL", "Социальное"),
    CREATIVITY("CREATIVITY", "Творчество"),
    LEARNING("LEARNING", "Обучение"),
    OTHER("OTHER", "Другое");
    
    companion object {
        fun fromString(value: String): ChallengeCategory {
            return values().find { it.name == value || it.value == value } ?: OTHER
        }
        
        fun getDisplayName(value: String): String {
            return fromString(value).displayName
        }
    }
}

enum class ChallengeDifficulty {
    EASY,    // Легкий
    MEDIUM,  // Средний
    HARD     // Сложный
} 