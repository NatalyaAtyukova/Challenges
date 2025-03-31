package com.challenges.dailychallenges.data.repository

import com.challenges.dailychallenges.data.model.CommunityChallenge
import com.challenges.dailychallenges.data.model.Comment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Получение списка челленджей сообщества
    fun getCommunityChallenges(
        category: String? = null,
        sortBy: String = "created_at",
        limit: Long = 20
    ): Flow<List<CommunityChallenge>> = flow {
        try {
            var query = firestore.collection("community_challenges")
                .whereEqualTo("is_reported", false)
                .whereEqualTo("is_verified", true)
            
            if (!category.isNullOrEmpty()) {
                query = query.whereEqualTo("category", category)
            }
            
            when (sortBy) {
                "likes" -> query = query.orderBy("likes_count", Query.Direction.DESCENDING)
                "comments" -> query = query.orderBy("comments_count", Query.Direction.DESCENDING)
                else -> query = query.orderBy("created_at", Query.Direction.DESCENDING)
            }
            
            query = query.limit(limit)
            
            val snapshot = query.get().await()
            val challenges = snapshot.toObjects(CommunityChallenge::class.java)
            emit(challenges)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    // Публикация нового челленджа
    suspend fun publishChallenge(challenge: CommunityChallenge): Result<String> {
        return try {
            val docRef = firestore.collection("community_challenges").document()
            val challengeWithId = challenge.copy(id = docRef.id)
            docRef.set(challengeWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Получение комментариев к челленджу
    fun getComments(challengeId: String): Flow<List<Comment>> = flow {
        try {
            val snapshot = firestore.collection("comments")
                .whereEqualTo("challenge_id", challengeId)
                .whereEqualTo("is_reported", false)
                .orderBy("created_at", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val comments = snapshot.toObjects(Comment::class.java)
            emit(comments)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    // Добавление комментария
    suspend fun addComment(comment: Comment): Result<String> {
        return try {
            val docRef = firestore.collection("comments").document()
            val commentWithId = comment.copy(id = docRef.id)
            docRef.set(commentWithId).await()
            
            // Обновляем счетчик комментариев в челлендже
            firestore.collection("community_challenges")
                .document(comment.challengeId)
                .update("comments_count", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Лайк челленджа
    suspend fun likeChallenge(challengeId: String, userId: String): Result<Unit> {
        return try {
            val likeRef = firestore.collection("likes")
                .document("${challengeId}_${userId}")
            
            val likeDoc = likeRef.get().await()
            if (!likeDoc.exists()) {
                likeRef.set(mapOf(
                    "challenge_id" to challengeId,
                    "user_id" to userId,
                    "created_at" to com.google.firebase.Timestamp.now()
                )).await()
                
                firestore.collection("community_challenges")
                    .document(challengeId)
                    .update(mapOf(
                        "likes_count" to com.google.firebase.firestore.FieldValue.increment(1)
                    ))
                    .await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Удаление лайка
    suspend fun unlikeChallenge(challengeId: String, userId: String): Result<Unit> {
        return try {
            val likeRef = firestore.collection("likes")
                .document("${challengeId}_${userId}")
            
            val likeDoc = likeRef.get().await()
            if (likeDoc.exists()) {
                likeRef.delete().await()
                
                firestore.collection("community_challenges")
                    .document(challengeId)
                    .update(mapOf(
                        "likes_count" to com.google.firebase.firestore.FieldValue.increment(-1)
                    ))
                    .await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Проверка, лайкнул ли пользователь челлендж
    suspend fun isChallengeLiked(challengeId: String, userId: String): Boolean {
        return try {
            val likeDoc = firestore.collection("likes")
                .document("${challengeId}_${userId}")
                .get()
                .await()
            likeDoc.exists()
        } catch (e: Exception) {
            false
        }
    }

    // Жалоба на челлендж
    suspend fun reportChallenge(
        challengeId: String,
        userId: String,
        reason: String
    ): Result<Unit> {
        return try {
            firestore.collection("community_challenges")
                .document(challengeId)
                .update(mapOf(
                    "is_reported" to true,
                    "report_reason" to reason
                ))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Жалоба на комментарий
    suspend fun reportComment(
        commentId: String,
        userId: String,
        reason: String
    ): Result<Unit> {
        return try {
            firestore.collection("comments")
                .document(commentId)
                .update(mapOf(
                    "is_reported" to true,
                    "report_reason" to reason
                ))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 