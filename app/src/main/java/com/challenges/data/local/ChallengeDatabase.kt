package com.challenges.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.model.ChallengeDifficulty

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new columns
        database.execSQL("ALTER TABLE challenges ADD COLUMN isActive INTEGER NOT NULL DEFAULT 1")
        database.execSQL("ALTER TABLE challenges ADD COLUMN activationDate INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
        database.execSQL("ALTER TABLE challenges ADD COLUMN expirationDate INTEGER NOT NULL DEFAULT ${System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000}")
    }
}

@Database(entities = [Challenge::class], version = 2)
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