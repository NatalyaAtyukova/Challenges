package com.challenges.dailychallenges.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey
    val uid: String,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val totalChallenges: Int = 0,
    val completedChallenges: Int = 0,
    val publishedChallenges: Int = 0,
    val totalPoints: Int = 0,
    val streakDays: Int = 0,
    val lastActive: Long = System.currentTimeMillis()
) 