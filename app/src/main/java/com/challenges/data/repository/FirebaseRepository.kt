package com.challenges.data.repository

import com.challenges.data.dao.ChallengeDao
import com.challenges.data.model.Challenge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val challengeDao: ChallengeDao
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val challengesCollection = "challenges"
    
    val currentUser: FirebaseUser?
        get() = auth.currentUser
    
    // Аутентификация
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Не удалось авторизоваться"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signUp(email: String, password: String, displayName: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                // Здесь можно обновить профиль пользователя, добавив displayName
                val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                it.updateProfile(profileUpdates).await()
                Result.success(it)
            } ?: Result.failure(Exception("Не удалось создать аккаунт"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun signOut() {
        auth.signOut()
    }
    
    // Работа с челленджами
    suspend fun publishChallenge(challenge: Challenge): Result<Challenge> = withContext(Dispatchers.IO) {
        try {
            // Проверяем, что пользователь авторизован
            val user = auth.currentUser ?: return@withContext Result.failure(Exception("Пользователь не авторизован"))
            
            // Создаем копию челленджа с информацией о пользователе
            val challengeWithUser = challenge.copy(
                userName = user.displayName ?: "Пользователь",
                isCustom = true,
                category = "COMMUNITY"
            )
            
            // Сохраняем в Firestore
            val document = firestore.collection(challengesCollection).document()
            val challengeWithId = challengeWithUser.copy(id = document.id.hashCode().toLong())
            
            document.set(challengeWithId).await()
            
            // Сохраняем локально
            val localId = challengeDao.insertChallenge(challengeWithId)
            
            Result.success(challengeWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getCommunityChallenges(): Flow<List<Challenge>> = flow {
        try {
            // Получаем челленджи сообщества из Firestore
            val snapshot = firestore.collection(challengesCollection)
                .whereEqualTo("category", "COMMUNITY")
                .get()
                .await()
            
            val challenges = snapshot.documents.mapNotNull { document ->
                document.toObject(Challenge::class.java)
            }
            
            emit(challenges)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
    
    suspend fun syncCommunityChallenges() = withContext(Dispatchers.IO) {
        try {
            // Получаем челленджи сообщества из Firestore
            val snapshot = firestore.collection(challengesCollection)
                .whereEqualTo("category", "COMMUNITY")
                .get()
                .await()
            
            val challenges = snapshot.documents.mapNotNull { document ->
                document.toObject(Challenge::class.java)
            }
            
            // Получаем локальные челленджи сообщества
            val localChallenges = challengeDao.getCommunityOneShot()
            
            // Удаляем челленджи, которых нет в Firestore
            val firestoreIds = challenges.map { it.id }
            localChallenges.forEach { localChallenge ->
                if (localChallenge.id !in firestoreIds) {
                    challengeDao.deleteChallenge(localChallenge)
                }
            }
            
            // Добавляем новые челленджи из Firestore
            val localIds = localChallenges.map { it.id }
            challenges.forEach { firestoreChallenge ->
                if (firestoreChallenge.id !in localIds) {
                    challengeDao.insertChallenge(firestoreChallenge)
                }
            }
        } catch (e: Exception) {
            // Логгирование ошибки
        }
    }
} 