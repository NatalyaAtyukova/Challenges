package com.challenges.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.challenges.data.util.Converters
import java.time.LocalDateTime

@Entity(tableName = "challenges")
@TypeConverters(Converters::class)
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String? = null,
    val points: Int = 0, // Очки за выполнение
    val isCustom: Boolean = false, // Является ли челлендж пользовательским
    val isSeasonal: Boolean = false, // Является ли челлендж сезонным
    val startDate: LocalDateTime? = null, // Дата начала для сезонных челленджей
    val endDate: LocalDateTime? = null, // Дата окончания для сезонных челленджей
    val isFavorite: Boolean = false,
    val isCompleted: Boolean = false,
    val notes: String? = null, // Заметки пользователя
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val userName: String? = null // Имя пользователя, создавшего челлендж
)

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