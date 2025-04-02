package com.challenges.dailychallenges

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.tasks.await

private const val TAG = "FirebaseHelper"

object FirebaseHelper {
    
    private var isInitialized = false
    
    /**
     * Инициализирует Firestore с правильными настройками и обеспечивает
     * правильную работу с коллекцией челленджей
     */
    fun initFirestore() {
        if (isInitialized) {
            Log.d(TAG, "Firestore уже был инициализирован")
            return
        }
        
        try {
            Log.d(TAG, "Начало инициализации Firestore")
            val firestore = FirebaseFirestore.getInstance()
            
            // Настраиваем Firestore для работы в оффлайн режиме
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build()
            
            firestore.firestoreSettings = settings
            
            isInitialized = true
            Log.d(TAG, "Firestore успешно инициализирован с настройками кэша")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при инициализации Firestore: ${e.message}", e)
        }
    }
    
    /**
     * Проверяет наличие коллекции "challenges" и создает ее, если она не существует
     */
    suspend fun ensureChallengesCollectionExists() {
        if (!isInitialized) {
            Log.w(TAG, "Вызов ensureChallengesCollectionExists до инициализации Firestore, инициализируем сейчас")
            initFirestore()
        }
        
        try {
            Log.d(TAG, "Проверка существования коллекции challenges")
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