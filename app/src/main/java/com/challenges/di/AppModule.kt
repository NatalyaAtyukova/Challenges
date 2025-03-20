package com.challenges.di

import android.content.Context
import androidx.room.Room
import com.challenges.data.local.ChallengeDao
import com.challenges.data.local.ChallengeDatabase
import com.challenges.data.repository.ChallengeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
        ).build()
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