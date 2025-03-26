package com.challenges.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.challenges.data.dao.ChallengeDao
import com.challenges.data.model.Challenge
import com.challenges.di.EnglishChallenges
import com.challenges.di.RussianChallenges
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Locale

@HiltWorker
class ChallengeUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val challengeDao: ChallengeDao,
    @EnglishChallenges private val englishChallenges: List<Challenge>,
    @RussianChallenges private val russianChallenges: List<Challenge>
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            updateChallenges()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    private suspend fun updateChallenges() {
        // Clear existing data
        challengeDao.clearAll()
        
        // Add new data based on current locale
        val challenges = when (Locale.getDefault().language) {
            "ru" -> russianChallenges
            else -> englishChallenges
        }
        
        // Insert challenges
        for (challenge in challenges) {
            challengeDao.insertChallenge(challenge)
        }
    }
} 