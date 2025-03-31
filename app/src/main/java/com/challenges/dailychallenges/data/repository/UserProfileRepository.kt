package com.challenges.dailychallenges.data.repository

import android.net.Uri
import com.challenges.dailychallenges.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {
    fun getUserProfile(userId: String): Flow<UserProfile?> = flow {
        try {
            val doc = firestore.collection("users").document(userId).get().await()
            emit(doc.toObject(UserProfile::class.java))
        } catch (e: Exception) {
            emit(null)
        }
    }

    suspend fun updateProfile(
        displayName: String,
        photoUri: Uri? = null
    ): Result<UserProfile> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            
            // Upload photo if provided
            var photoUrl: String? = null
            if (photoUri != null) {
                val photoRef = storage.reference
                    .child("users")
                    .child(userId)
                    .child("profile_photo.jpg")
                
                photoRef.putFile(photoUri).await()
                photoUrl = photoRef.downloadUrl.await().toString()
            }
            
            // Update profile in Firestore
            val profile = UserProfile(
                uid = userId,
                displayName = displayName,
                photoUrl = photoUrl,
                lastActive = System.currentTimeMillis()
            )
            
            firestore.collection("users")
                .document(userId)
                .set(profile)
                .await()
            
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfilePhoto(photoUri: Uri): Result<String> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            
            val photoRef = storage.reference
                .child("users")
                .child(userId)
                .child("profile_photo.jpg")
            
            photoRef.putFile(photoUri).await()
            val photoUrl = photoRef.downloadUrl.await().toString()
            
            // Update photo URL in Firestore
            firestore.collection("users")
                .document(userId)
                .update("photo_url", photoUrl, "last_updated", com.google.firebase.Timestamp.now())
                .await()
            
            Result.success(photoUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProfilePhoto(): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            
            // Delete photo from Storage
            val photoRef = storage.reference
                .child("users")
                .child(userId)
                .child("profile_photo.jpg")
            
            photoRef.delete().await()
            
            // Update profile in Firestore
            firestore.collection("users")
                .document(userId)
                .update("photo_url", null, "last_updated", com.google.firebase.Timestamp.now())
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementCompletedChallenges(userId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update(
                    "completedChallenges",
                    com.google.firebase.firestore.FieldValue.increment(1L),
                    "lastActive",
                    System.currentTimeMillis()
                )
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementPublishedChallenges(userId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update(
                    "publishedChallenges",
                    com.google.firebase.firestore.FieldValue.increment(1L),
                    "lastActive",
                    System.currentTimeMillis()
                )
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addPoints(points: Int): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
            
            firestore.collection("users")
                .document(userId)
                .update(
                    "total_points",
                    com.google.firebase.firestore.FieldValue.increment(points.toLong()),
                    "last_updated",
                    com.google.firebase.Timestamp.now()
                )
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 