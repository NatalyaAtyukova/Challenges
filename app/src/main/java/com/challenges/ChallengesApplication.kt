package com.challenges

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.challenges.worker.ChallengeUpdateWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ChallengesApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        setupPeriodicChallengeUpdate()
    }

    private fun setupPeriodicChallengeUpdate() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val updateChallengesRequest = PeriodicWorkRequestBuilder<ChallengeUpdateWorker>(
            1, TimeUnit.DAYS // Update challenges daily
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "challenge_update",
            ExistingPeriodicWorkPolicy.KEEP, // Keep existing if already scheduled
            updateChallengesRequest
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
} 