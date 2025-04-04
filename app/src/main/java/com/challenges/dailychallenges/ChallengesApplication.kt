package com.challenges.dailychallenges

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.challenges.dailychallenges.data.repository.ChallengeRepository
import com.challenges.dailychallenges.worker.ChallengeUpdateWorker
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "ChallengesApp"

@HiltAndroidApp
class ChallengesApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    @Inject
    lateinit var challengeRepository: ChallengeRepository
    
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        
        try {
            Log.d(TAG, "Инициализация Firebase...")
            // Убедиться, что Firebase инициализирован корректно
            FirebaseApp.initializeApp(this)
            
            // Инициализация Firebase Firestore
            FirebaseHelper.initFirestore()
            
            // Disable app verification for testing to avoid reCAPTCHA issues
            val auth = FirebaseAuth.getInstance()
            auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
            Log.d(TAG, "Текущий пользователь: ${auth.currentUser?.uid ?: "нет"}")
            
            // Убедимся, что коллекция "challenges" существует
            applicationScope.launch {
                try {
                    FirebaseHelper.ensureChallengesCollectionExists()
                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка при проверке коллекции challenges: ${e.message}", e)
                }
            }
            
            // Синхронизация челленджей с Firebase с обработкой ошибок
            applicationScope.launch {
                try {
                    syncChallengesWithFirebase()
                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка при синхронизации челленджей: ${e.message}", e)
                }
            }
            
            // Настройка периодического обновления челленджей
            try {
                setupPeriodicChallengeUpdate()
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при настройке периодического обновления: ${e.message}", e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Критическая ошибка при инициализации приложения: ${e.message}", e)
        }
    }
    
    private fun syncChallengesWithFirebase() {
        applicationScope.launch {
            try {
                Log.d(TAG, "Начало синхронизации челленджей с Firebase")
                
                // Синхронизируем все челленджи между Firebase и локальной базой данных
                challengeRepository.syncAllChallenges()
                
                // После синхронизации принудительно загружаем данные из Firebase
                val challenges = challengeRepository.getAllChallengesFromFirebase().first()
                Log.d(TAG, "Загружено ${challenges.size} челленджей из Firebase после синхронизации")
                
                // Проверим также наличие челленджей в локальной БД
                val localChallenges = challengeRepository.getChallengesByDate().first()
                Log.d(TAG, "В локальной БД имеется ${localChallenges.size} челленджей")
                
                if (challenges.isEmpty() && localChallenges.isNotEmpty()) {
                    // Если в Firebase пусто, но есть локальные челленджи, пробуем повторно загрузить
                    Log.d(TAG, "Повторная попытка загрузки данных в Firebase")
                    challengeRepository.syncAllChallenges()
                }
            } catch (e: Exception) {
                // Логирование ошибки при синхронизации
                Log.e(TAG, "Ошибка при синхронизации челленджей с Firebase: ${e.message}", e)
            }
        }
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