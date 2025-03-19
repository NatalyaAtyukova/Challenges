package com.challenges

import android.app.Application
import androidx.room.Room
import com.challenges.data.local.ChallengeDatabase

class ChallengesApplication : Application() {
    lateinit var database: ChallengeDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            ChallengeDatabase::class.java,
            "challenges_database"
        ).build()
    }
} 