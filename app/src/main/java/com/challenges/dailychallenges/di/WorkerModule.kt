package com.challenges.dailychallenges.di

import android.content.Context
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.data.repository.FirebaseRepository
import com.challenges.dailychallenges.worker.ChallengeUpdateWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Provides
    @Singleton
    fun provideWorkerFactory(
        firebaseRepository: FirebaseRepository,
        @Named("english_challenges") englishChallenges: List<Challenge>,
        @Named("russian_challenges") russianChallenges: List<Challenge>
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
                    firebaseRepository,
                    englishChallenges,
                    russianChallenges
                )
            else -> null
        }
    }
    
    @Provides
    @Named("english_challenges")
    fun provideEnglishChallenges(): List<Challenge> {
        return listOf(
            Challenge(
                title = "Small Talk with a Colleague",
                description = "Start a casual conversation with a colleague about their day",
                category = "CONVERSATION",
                difficulty = "EASY",
                points = 50
            ),
            Challenge(
                title = "Public Speaking",
                description = "Give a short presentation to a group of people",
                category = "PUBLIC",
                difficulty = "HARD",
                points = 150
            )
            // Добавьте больше челленджей по необходимости
        )
    }

    @Provides
    @Named("russian_challenges")
    fun provideRussianChallenges(): List<Challenge> {
        return listOf(
            Challenge(
                title = "Разговор с коллегой",
                description = "Начните непринужденную беседу с коллегой о его дне",
                category = "CONVERSATION",
                difficulty = "EASY",
                points = 50
            ),
            Challenge(
                title = "Публичное выступление",
                description = "Проведите короткую презентацию для группы людей",
                category = "PUBLIC",
                difficulty = "HARD",
                points = 150
            )
            // Добавьте больше челленджей по необходимости
        )
    }
} 