/*
package com.challenges.dailychallenges.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.challenges.dailychallenges.data.dao.ChallengeDao
import com.challenges.dailychallenges.data.dao.UserProfileDao
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.data.model.UserProfile
import com.challenges.dailychallenges.data.util.Converters

@Database(
    entities = [Challenge::class, UserProfile::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
    abstract fun userProfileDao(): UserProfileDao
    
    companion object {
        const val DATABASE_NAME = "challenges_database"
        
        val MIGRATION_1_2 = androidx.room.migration.Migration(1, 2) { database ->
            database.execSQL("""
                ALTER TABLE user_profiles
                ADD COLUMN publishedChallenges INTEGER NOT NULL DEFAULT 0
            """)
            database.execSQL("""
                ALTER TABLE user_profiles
                ADD COLUMN totalPoints INTEGER NOT NULL DEFAULT 0
            """)
        }
    }
}
*/ 