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

enum class ChallengeCategory {
    CONVERSATION, // Разговорные
    VIDEO,       // Видео
    PUBLIC,      // Публичные выступления
    DAILY,       // Ежедневные
    CONTENT,     // Контент
    TEACHING,    // Обучение
    PODCAST,     // Подкасты
    WRITING,     // Письмо
    CONSULTING,  // Консультации
    COMMUNITY    // Пользовательские челленджи сообщества
}

enum class ChallengeDifficulty {
    EASY,    // Легкий
    MEDIUM,  // Средний
    HARD     // Сложный
} 