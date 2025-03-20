package com.challenges.di

import android.content.Context
import androidx.room.Room
import com.challenges.data.local.ChallengeDao
import com.challenges.data.local.ChallengeDatabase
import com.challenges.data.local.DatabaseInitializer
import com.challenges.data.local.DatabaseInitializerRu
import com.challenges.data.repository.ChallengeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideChallengeDatabase(
        @ApplicationContext context: Context
    ): ChallengeDatabase {
        return Room.databaseBuilder(
            context,
            ChallengeDatabase::class.java,
            "challenges_database"
        ).build().also { database ->
            // Populate database with initial data
            CoroutineScope(Dispatchers.IO).launch {
                val dao = database.challengeDao()
                if (dao.getAllChallengesOneShot().isEmpty()) {
                    val challenges = when (Locale.getDefault().language) {
                        "ru" -> DatabaseInitializerRu.initialChallenges
                        else -> DatabaseInitializer.initialChallenges
                    }
                    challenges.forEach { challenge ->
                        dao.insertChallenge(challenge)
                    }
                }
            }
        }
    }
    
    @Provides
    @Singleton
    fun provideChallengeDao(database: ChallengeDatabase): ChallengeDao {
        return database.challengeDao()
    }
    
    @Provides
    @Singleton
    fun provideChallengeRepository(
        dao: ChallengeDao
    ): ChallengeRepository {
        return ChallengeRepository(dao)
    }
} 