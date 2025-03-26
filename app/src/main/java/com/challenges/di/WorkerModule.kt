package com.challenges.di

import android.content.Context
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.challenges.data.dao.ChallengeDao
import com.challenges.data.model.Challenge
import com.challenges.worker.ChallengeUpdateWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Provides
    @Singleton
    fun provideWorkerFactory(
        challengeDao: ChallengeDao,
        @EnglishChallenges englishChallenges: List<Challenge>,
        @RussianChallenges russianChallenges: List<Challenge>
    ) = object : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ) = when (workerClassName) {
            ChallengeUpdateWorker::class.java.name ->
                ChallengeUpdateWorker(
                    appContext,
                    workerParameters,
                    challengeDao,
                    englishChallenges,
                    russianChallenges
                )
            else -> null
        }
    }
} 