package com.challenges.dailychallenges.data.util

import androidx.room.TypeConverter
import com.challenges.dailychallenges.data.model.AchievementType
import com.challenges.dailychallenges.data.model.ChallengeCategory
import com.challenges.dailychallenges.data.model.ChallengeDifficulty
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromCategory(value: String?): ChallengeCategory? {
        return value?.let { ChallengeCategory.valueOf(it) }
    }

    @TypeConverter
    fun categoryToString(category: ChallengeCategory?): String? {
        return category?.name
    }

    @TypeConverter
    fun fromDifficulty(value: String?): ChallengeDifficulty? {
        return value?.let { ChallengeDifficulty.valueOf(it) }
    }

    @TypeConverter
    fun difficultyToString(difficulty: ChallengeDifficulty?): String? {
        return difficulty?.name
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun fromAchievementType(value: String?): AchievementType? {
        return value?.let { AchievementType.valueOf(it) }
    }

    @TypeConverter
    fun achievementTypeToString(type: AchievementType?): String? {
        return type?.name
    }
} 