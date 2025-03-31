package com.challenges.dailychallenges

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.tasks.await

private const val TAG = "FirebaseHelper"

object FirebaseHelper {
    
    /**
     * Инициализирует Firestore с правильными настройками и обеспечивает
     * правильную работу с коллекцией челленджей
     */
    fun initFirestore() {
        try {
            val firestore = FirebaseFirestore.getInstance()
            
            // Настраиваем Firestore для работы в оффлайн режиме
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build()
            
            firestore.firestoreSettings = settings
            
            Log.d(TAG, "Firestore инициализирован с настройками кэша")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при инициализации Firestore: ${e.message}", e)
        }
    }
    
    /**
     * Проверяет наличие коллекции "challenges" и создает ее, если она не существует
     */
    suspend fun ensureChallengesCollectionExists() {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val challengesCollection = firestore.collection("challenges")
            
            // Проверяем существование коллекции, получив первый документ
            val snapshot = challengesCollection.limit(1).get().await()
            
            if (snapshot.isEmpty) {
                Log.d(TAG, "Коллекция challenges пуста или не существует")
                
                // Создаем пустой документ-заглушку, который можно будет удалить позже
                val placeholderDoc = hashMapOf(
                    "title" to "Placeholder",
                    "description" to "This is a placeholder document to ensure collection exists",
                    "category" to "SYSTEM",
                    "isSystemPlaceholder" to true
                )
                
                challengesCollection.document("placeholder").set(placeholderDoc).await()
                Log.d(TAG, "Создан документ-заглушка в коллекции challenges")
            } else {
                Log.d(TAG, "Коллекция challenges существует")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при проверке/создании коллекции challenges: ${e.message}", e)
        }
    }
} 