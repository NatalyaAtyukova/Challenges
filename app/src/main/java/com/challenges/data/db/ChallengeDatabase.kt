package com.challenges.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.challenges.data.dao.ChallengeDao
import com.challenges.data.model.Challenge
import com.challenges.data.util.Converters

@Database(
    entities = [Challenge::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao

    companion object {
        const val DATABASE_NAME = "challenges_db"

        val MIGRATION_1_2 = androidx.room.migration.Migration(1, 2) { database ->
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN points INTEGER NOT NULL DEFAULT 0
            """)
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN isCustom INTEGER NOT NULL DEFAULT 0
            """)
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN isSeasonal INTEGER NOT NULL DEFAULT 0
            """)
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN startDate INTEGER
            """)
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN endDate INTEGER
            """)
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0
            """)
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN notes TEXT
            """)
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0
            """)
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0
            """)
        }

        val MIGRATION_2_3 = androidx.room.migration.Migration(2, 3) { database ->
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN difficulty TEXT
            """)
        }
    }
} 