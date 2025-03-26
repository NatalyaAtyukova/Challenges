package com.challenges.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.challenges.data.dao.AchievementDao
import com.challenges.data.dao.ChallengeDao
import com.challenges.data.model.Achievement
import com.challenges.data.model.Challenge
import com.challenges.data.util.Converters

@Database(
    entities = [Challenge::class, Achievement::class],
    version = 5,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
    abstract fun achievementDao(): AchievementDao

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
        
        val MIGRATION_3_4 = androidx.room.migration.Migration(3, 4) { database ->
            database.execSQL("""
                ALTER TABLE challenges
                ADD COLUMN userName TEXT
            """)
        }
        
        val MIGRATION_4_5 = androidx.room.migration.Migration(4, 5) { database ->
            // Create the achievements table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS achievements (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    title TEXT NOT NULL,
                    description TEXT NOT NULL,
                    iconName TEXT NOT NULL,
                    points INTEGER NOT NULL DEFAULT 0,
                    isUnlocked INTEGER NOT NULL DEFAULT 0,
                    unlockedAt INTEGER,
                    condition TEXT NOT NULL,
                    threshold INTEGER NOT NULL,
                    progress INTEGER NOT NULL DEFAULT 0
                )
            """)
        }
    }
} 