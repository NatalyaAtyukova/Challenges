/*
package com.challenges.dailychallenges.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.challenges.dailychallenges.data.dao.AchievementDao
import com.challenges.dailychallenges.data.dao.ChallengeDao
import com.challenges.dailychallenges.data.model.Achievement
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.data.util.Converters

@Database(
    entities = [Challenge::class, Achievement::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
    abstract fun achievementDao(): AchievementDao
}
*/ 