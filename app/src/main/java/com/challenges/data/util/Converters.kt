package com.challenges.data.util

import androidx.room.TypeConverter
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.model.ChallengeDifficulty
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
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
} 