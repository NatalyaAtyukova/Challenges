package com.challenges.dailychallenges.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.data.repository.FirebaseRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Named

@HiltWorker
class ChallengeUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val firebaseRepository: FirebaseRepository,
    @Named("english_challenges") private val englishChallenges: List<Challenge>,
    @Named("russian_challenges") private val russianChallenges: List<Challenge>
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "ChallengeUpdateWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Starting challenge update worker")
            
            // Определяем язык устройства
            val isRussian = Locale.getDefault().language == "ru"
            
            // Выбираем список челленджей в зависимости от языка
            val challenges = if (isRussian) russianChallenges else englishChallenges
            
            // Проверяем, есть ли уже челленджи у пользователя
            val userChallenges = firebaseRepository.getAllChallenges().firstOrNull() ?: emptyList()
            
            if (userChallenges.isEmpty()) {
                Log.d(TAG, "No challenges found, populating with default challenges")
                // Если нет челленджей, добавляем дефолтные
                challenges.forEach { challenge ->
                    firebaseRepository.insertChallenge(challenge)
                }
                
                Log.d(TAG, "Added ${challenges.size} default challenges")
            } else {
                Log.d(TAG, "User already has ${userChallenges.size} challenges, skipping population")
            }
            
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error updating challenges: ${e.message}", e)
            Result.failure()
        }
    }
} 