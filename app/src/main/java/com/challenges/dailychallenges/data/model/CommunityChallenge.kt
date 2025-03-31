package com.challenges.dailychallenges.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class CommunityChallenge(
    @DocumentId
    val id: String = "",
    
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val difficulty: String = "",
    val points: Int = 0,
    
    @PropertyName("user_id")
    val userId: String = "",
    
    @PropertyName("user_name")
    val userName: String = "",
    
    @PropertyName("user_photo_url")
    val userPhotoUrl: String? = null,
    
    @PropertyName("created_at")
    val createdAt: Timestamp = Timestamp.now(),
    
    @PropertyName("likes_count")
    val likesCount: Int = 0,
    
    @PropertyName("comments_count")
    val commentsCount: Int = 0,
    
    @PropertyName("is_verified")
    val isVerified: Boolean = false,
    
    @PropertyName("is_reported")
    val isReported: Boolean = false,
    
    @PropertyName("report_reason")
    val reportReason: String? = null
) 