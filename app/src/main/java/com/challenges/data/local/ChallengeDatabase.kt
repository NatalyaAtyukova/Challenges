package com.challenges.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.model.ChallengeDifficulty

@Database(entities = [Challenge::class], version = 1)
@TypeConverters(Converters::class)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
}

@androidx.room.TypeConverters
class Converters {
    @androidx.room.TypeConverter
    fun fromChallengeCategory(value: ChallengeCategory): String {
        return value.name
    }

    @androidx.room.TypeConverter
    fun toChallengeCategory(value: String): ChallengeCategory {
        return enumValueOf(value)
    }

    @androidx.room.TypeConverter
    fun fromChallengeDifficulty(value: ChallengeDifficulty): String {
        return value.name
    }

    @androidx.room.TypeConverter
    fun toChallengeDifficulty(value: String): ChallengeDifficulty {
        return enumValueOf(value)
    }
} 